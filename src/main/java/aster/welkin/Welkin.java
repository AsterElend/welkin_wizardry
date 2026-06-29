package aster.welkin;


import aster.welkin.api.WardedBlocksState;
import aster.welkin.block.entity.AgoniteTransmuterEntity;
import aster.welkin.client.WelkinState;
import aster.welkin.jsonstuff.RecyclerReloadListener;
import aster.welkin.packet.ServerCutsceneManager;
import aster.welkin.packet.WelkinPackets;
import aster.welkin.registry.*;
import aster.welkin.registry.world.DimensionStuff;
import aster.welkin.registry.world.trees.LoomFoliagePlacers;
import aster.welkin.registry.world.trees.LoomTrunkPlacers;
import aster.welkin.sound.ModSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static aster.welkin.packet.ServerCutsceneManager.LOCKED_PLAYERS;

public class Welkin implements ModInitializer {
	public static final String MOD_ID = "welkin";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Identifier id(String it){
		return new Identifier(MOD_ID, it);
	}



	@Override
	public void onInitialize() {
		ModItems.registerModItems();


		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModItemGroups.registerItemGroups();
		ModRecipes.register();

		ModBlocks.registerModBlocks();
		ModSounds.registerSounds();
		ModBlockEntities.registerBlockEntities();
		LoomFluids.invoke();
		LoomTrunkPlacers.init();
		LoomFoliagePlacers.init();
		DimensionStuff.init();
		WelkinPackets.registerServer();
		ServerPlayNetworking.registerGlobalReceiver(WelkinPackets.STOP_CUTSCENE, (server, player, handler, buf, responseSender) -> {
			server.execute(() -> {
				// Unlock the player when the client reports the rotation is finished
				LOCKED_PLAYERS.remove(player.getUuid());
			});
		});

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				if (LOCKED_PLAYERS.contains(player.getUuid())) {
					player.setVelocity(0, 0, 0);
					player.networkHandler.syncWithPlayerPosition(); // Forces client sync
				}
			}
		});
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.player;


			server.execute(() -> server.execute(() -> { // 1 tick delay (important)

				ServerWorld world = player.getServerWorld();

				WardedBlocksState state = WardedBlocksState.get(world);

				syncToPlayer(player, state.getAllPositions());

			}));
		});

		ServerLivingEntityEvents.ALLOW_DAMAGE.register(((entity, source, amount) -> {
			stockpileDamage(entity, amount);
			return true;
		}));

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			WelkinState.worldSeed = server.getOverworld().getSeed();
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			WelkinState.worldSeed = server.getOverworld().getSeed();

		});

		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) ->{
			WardedBlocksState wards = WardedBlocksState.get(world);
			return !wards.isWarded(pos, world.getBlockState(pos));
		});

		ServerTickEvents.END_WORLD_TICK.register(world -> WardedBlocksState.get(world).validate(world));

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new RecyclerReloadListener());

		LOGGER.info("Hello Fabric world!");


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

	public static void syncToPlayer(ServerPlayerEntity player, Collection<BlockPos> positions) {

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