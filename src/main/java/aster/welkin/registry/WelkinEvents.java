package aster.welkin.registry;

import aster.welkin.Welkin;
import aster.welkin.api.WardedBlocksState;
import aster.welkin.api.WelkinUtil;
import aster.welkin.api.XpUtils;
import aster.welkin.api.state.WeatherManager;
import aster.welkin.block.entity.AgoniteTransmuterEntity;
import aster.welkin.cc.LastDeathSourceComponent;
import aster.welkin.cc.WelkinEntityCC;
import aster.welkin.item.WardstoneItem;
import aster.welkin.item.baton.ConductorBatonItem;
import aster.welkin.packet.WelkinPackets;
import aster.welkin.recipes.AlchemyRecipeManager;
import aster.welkin.recipes.AlchemyReloadListener;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class WelkinEvents {
    public static void register(){
        PlayerBlockBreakEvents.BEFORE.register((World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity entity) ->{
            if (world.isClient) return true;
            if (!world.getDimension().ultrawarm()) return true;
            if (!state.isOpaqueFullCube(world, pos)) return true;
            if (WardstoneItem.hasParticularWardstoneActive(player, (WardstoneItem) WelkinItems.MAGMATIC_WARDSTONE)){
                List<BlockPos> lavaPositions = new ArrayList<>();
                for (BlockPos iterator1 : WelkinUtil.getAdjacent(pos)){
                    if (world.getBlockState(iterator1).isOf(Blocks.LAVA)) lavaPositions.add(iterator1);
                }
                for (BlockPos iterator2: lavaPositions){
                    boolean shouldReplaceTheLava = true;
                    for (BlockPos iterator3: WelkinUtil.getAdjacent(iterator2)){
                        if (iterator3 == pos) continue;
                        if (!world.getBlockState(iterator3).isOpaqueFullCube(world, iterator3)) shouldReplaceTheLava = false;
                    }
                    if (shouldReplaceTheLava){
                        world.setBlockState(pos, WelkinBlocks.PUMICE.getDefaultState());
                    }
                }
            }

            return true;

        });

        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {

            if (!(entity instanceof ServerPlayerEntity player)) return true;
            if (!WardstoneItem.hasParticularWardstoneActive(player, (WardstoneItem) WelkinItems.DREAMSHIELD_WARDSTONE)) return true;
            if (player.hurtTime > 0) return false;

            if (player.getHealth() - amount <= 0){
                int shield = XpUtils.getPlayerXP(player);
                int toBlock = Math.round(amount * 10);
                if (shield > toBlock) {
                    XpUtils.subtractPlayerXP(player, toBlock);
                    player.setHealth(1F);
                    player.hurtTime = player.maxHurtTime;
                    return false; // cancel the damage entirely
                }
                return true;
            }



            return true; // not enough XP, take the hit
        });

        //don't open guis with the baton please
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            ItemStack stack = player.getStackInHand(hand);

            if (stack.getItem() instanceof ConductorBatonItem baton) {
                ItemUsageContext ctx = new ItemUsageContext(world, player, hand, stack, hitResult);
                return baton.useOnBlock(ctx);
            }

            return ActionResult.PASS;
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;


            server.execute(() -> server.execute(() -> { // 1 tick delay (important)

                ServerWorld world = player.getServerWorld();

                WardedBlocksState state = WardedBlocksState.get(world);

                syncWardsToPlayer(player, state.getAllPositions());

            }));
        });

        ServerLivingEntityEvents.ALLOW_DAMAGE.register(((entity, source, amount) -> {
            stockpileDamage(entity, amount);
            return true;
        }));

       ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
           RegistryEntry<DamageType> entry = damageSource.getTypeRegistryEntry();
           Optional<RegistryKey<DamageType>> key = entry.getKey();
           key.ifPresent(damageTypeRegistryKey -> WelkinEntityCC.LAST_DEATH_SOURCE.get(entity).setType(damageTypeRegistryKey));
       });

       ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, amount) ->{
           RegistryEntry<DamageType> entry = source.getTypeRegistryEntry();
           Optional<RegistryKey<DamageType>> key = entry.getKey();

           if (key.isPresent()){
               boolean isSame = WelkinEntityCC.LAST_DEATH_SOURCE.get(entity).isThisTheStoredDamageType(key.get());
               return !isSame;
           }

           return true;
       });


        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) ->{
            WardedBlocksState wards = WardedBlocksState.get(world);
            return !wards.isWarded(pos, world.getBlockState(pos));
        });

        ServerTickEvents.END_WORLD_TICK.register(world -> WardedBlocksState.get(world).validate(world));
        ServerTickEvents.END_WORLD_TICK.register(world -> WeatherManager.getServerState(world).tick(world));
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            Welkin.LOGGER.info("Starting the Great Work...");
            long seed = server.getOverworld().getSeed();
            AlchemyRecipeManager.setSeed(seed);
            AlchemyReloadListener.injectRecipes(server);
        });

        // 3. Runtime Reloads: Fires whenever a player runs the /reload command
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, serverResourceManager) -> {
                long seed = server.getOverworld().getSeed();
                Welkin.LOGGER.info("Rediscovering the Philosophers Stone...");
                AlchemyRecipeManager.setSeed(seed);
                AlchemyReloadListener.injectRecipes(server);

        });


    }



    public static void stockpileDamage(LivingEntity entity, float amount) {
        if (entity.getWorld().isClient) return;
        ServerWorld world = (ServerWorld) entity.getWorld();
        BlockPos center = entity.getBlockPos();
        int radius = 5;

        // Iterate over a cube around the player
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = center.add(x, y, z);
                    BlockEntity be = world.getBlockEntity(pos);

                    if (be instanceof AgoniteTransmuterEntity controller) {
                        controller.addToStockpile(amount);
                    }
                }
            }
        }


    }

    public static void syncWardsToPlayer(ServerPlayerEntity player, Collection<BlockPos> positions) {

        if (positions.isEmpty()) return;

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeInt(positions.size());
        buf.writeBoolean(true); // all are warded

        for (BlockPos pos : positions) {
            buf.writeLong(pos.asLong());
        }

        ServerPlayNetworking.send(player, WelkinPackets.SYNC_WARDS, buf);
    }

}
