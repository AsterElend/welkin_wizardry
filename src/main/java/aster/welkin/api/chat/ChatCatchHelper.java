package aster.welkin.api.chat;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

public class ChatCatchHelper {

    private static final int RADIUS = 6; // blocks


    public static void register() {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(ChatCatchHelper::onChatMessage);
        ServerWorldEvents.UNLOAD.register((server, world) ->{
            ChatReceiverCache.clear();
            GlobalChatReceiverCache.clear();
        });
    }

    private static boolean onChatMessage(SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters params) {
        String content = message.getContent().getString();
        ServerWorld senderWorld = sender.getServerWorld();
        BlockPos center = sender.getBlockPos();

        boolean cancel = false;

        // Nearby listeners
        for (BlockPos pos : ChatReceiverCache.getNearby(senderWorld, center, RADIUS)) {
            if (senderWorld.getBlockEntity(pos) instanceof ChatReceiver receiver) {
                if (receiver.onChatReceived(content, sender)) {
                    cancel = true;
                }
            }
        }

        // Global listeners — every dimension, since chat is server-wide
        for (ServerWorld world : sender.getServer().getWorlds()) {
            for (BlockPos pos : GlobalChatReceiverCache.getAll(world)) {
                if (world.getBlockEntity(pos) instanceof GlobalChatReceiver receiver) {
                    if (receiver.onChatReceived(content, sender)) {
                        cancel = true;
                    }
                }
            }
        }

        if (cancel) {
            sender.sendMessage(Text.literal("Message intercepted.").formatted(Formatting.GRAY), true);
        }

        return !cancel; // ALLOW_CHAT_MESSAGE: true = let it broadcast, false = cancel
    }
    private static int notifyNearbyBlocks(ServerWorld world, BlockPos center, String message, ServerPlayerEntity sender) {
        int count = 0;
        for (BlockPos pos : ChatReceiverCache.getNearby(world, center, RADIUS)) {
            if (world.getBlockEntity(pos) instanceof ChatReceiver receiver) {
                receiver.onChatReceived(message, sender);
                count++;
            }
        }
        return count;
    }
}

