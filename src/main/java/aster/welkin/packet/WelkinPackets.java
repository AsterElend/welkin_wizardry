package aster.welkin.packet;

import aster.welkin.Welkin;
import aster.welkin.api.WardedBlocksState;
import aster.welkin.client.ClientWardedState;
import aster.welkin.client.NadirToast;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static aster.welkin.item.WardingPrismItem.syncBatch;

public class WelkinPackets {

        public static final Identifier SYNC_WARDS = new Identifier("welkin", "ward_sync");

        // Server tells Client: "Start the cutscene at this position"
        public static final Identifier START_CUTSCENE = new Identifier("welkin", "start_cutscene");

        // Client tells Server: "Animation done, unlock my character"
        public static final Identifier STOP_CUTSCENE = new Identifier("welkin", "stop_cutscene");



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

