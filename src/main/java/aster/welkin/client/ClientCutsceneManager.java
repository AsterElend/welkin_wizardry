package aster.welkin.client;

import aster.welkin.mixin.CameraMixin;
import aster.welkin.packet.WelkinPackets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.util.math.BlockPos;

public class ClientCutsceneManager {

    // Register this in your ClientModInitializer
    public static void registerPacketReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(WelkinPackets.START_CUTSCENE, (client, handler, buf, responseSender) -> {
            BlockPos targetPos = buf.readBlockPos();

            client.execute(() -> {
                // Engage the mixin logic
                CameraMixin.orbitAngle = 0.0f;
                CameraMixin.isCutsceneActive = true;
            });
        });
    }

    // Call this from your Client Tick loop once orbitAngle >= 360.0f
    public static void stopSequence() {
        CameraMixin.isCutsceneActive = false;

        // Tell the server we are done
        ClientPlayNetworking.send(WelkinPackets.STOP_CUTSCENE, PacketByteBufs.empty());
    }
}
