package aster.welkin.packet;

import aster.welkin.Welkin;
import aster.welkin.api.WardedBlocksState;
import aster.welkin.cc.FrozenVelocityComponent;
import aster.welkin.cc.WelkinEntityCC;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import static aster.welkin.item.WardingPrismItem.syncBatch;

public class WelkinPackets {

        public static final Identifier SYNC_WARDS = new Identifier("welkin", "ward_sync");

        public static final Identifier FREEZE_MOMENTUM_PACKET = Welkin.id("freeze_momentum");


        public static final Identifier FIRE_NADIR_TOAST = Welkin.id("fire_nadir_toast");

        public static void registerServer(){
                ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> server.execute(() -> server.execute(() -> { // 1 tick delay

                        ServerPlayerEntity player = handler.player;
                        ServerWorld world = player.getServerWorld();

                        WardedBlocksState state = WardedBlocksState.get(world);

                        syncBatch(world, state.getAllPositions(), true);
                })));

                ServerPlayNetworking.registerGlobalReceiver(FREEZE_MOMENTUM_PACKET, ((server, player, handler, buf, responseSender) -> {
                        final boolean incomingFreeze = buf.readableBytes() > 0;
                        final double x = incomingFreeze ? buf.readDouble() : 0;
                        final double y = incomingFreeze ? buf.readDouble() : 0;
                        final double z = incomingFreeze ? buf.readDouble() : 0;

                        // Execute on the main server thread to avoid concurrency crashes
                        server.execute(() -> {
                                FrozenVelocityComponent comp = WelkinEntityCC.FROZEN_MOMENTUM.get(player);
                                if (!comp.isLocked() && incomingFreeze) {
                                        comp.freeze(new Vec3d(x, y, z));
                                        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
                                                SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.PLAYERS, 1f, 1f);
                                } else {
                                        comp.unfreeze();
                                        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
                                                SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE.value(), SoundCategory.PLAYERS, 1f, 1f);
                                }
                        });
                }));
        }


}

