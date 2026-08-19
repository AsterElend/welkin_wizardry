package aster.welkin.packet;

import aster.welkin.Welkin;
import aster.welkin.api.WardedBlocksState;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import static aster.welkin.item.WardingPrismItem.syncBatch;

public class WelkinPackets {

        public static final Identifier SYNC_WARDS = new Identifier("welkin", "ward_sync");




        public static final Identifier FIRE_NADIR_TOAST = Welkin.id("fire_nadir_toast");

        public static void registerServer(){
                ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> server.execute(() -> server.execute(() -> { // 1 tick delay

                        ServerPlayerEntity player = handler.player;
                        ServerWorld world = player.getServerWorld();

                        WardedBlocksState state = WardedBlocksState.get(world);

                        syncBatch(world, state.getAllPositions(), true);
                })));

        }


}

