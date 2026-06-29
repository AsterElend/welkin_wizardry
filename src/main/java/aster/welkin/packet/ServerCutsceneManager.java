package aster.welkin.packet;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.UUID;

public class ServerCutsceneManager {
    // Track locked players so we can freeze them in a server tick event
    public static final HashSet<UUID> LOCKED_PLAYERS = new HashSet<>();

    public static void startSequence(ServerPlayerEntity player, BlockPos pos) {
        LOCKED_PLAYERS.add(player.getUuid());

        // 1. Teleport player to the exact center of the block
        player.teleport(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, false);

        // 2. Write data to the buffer
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);

        // 3. Send to client
        ServerPlayNetworking.send(player, WelkinPackets.START_CUTSCENE, buf);
    }


}
