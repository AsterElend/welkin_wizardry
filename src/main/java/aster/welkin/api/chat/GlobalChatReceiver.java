package aster.welkin.api.chat;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public interface GlobalChatReceiver {

    boolean onChatReceived(String message, ServerPlayerEntity sender);

    default void registerGlobalChatReceiver() {
        if (this instanceof BlockEntity be && be.getWorld() != null && !be.getWorld().isClient) {
            GlobalChatReceiverCache.register(be.getWorld(), be.getPos());
        }
    }

    default void unregisterGlobalChatReceiver() {
        if (this instanceof BlockEntity be && be.getWorld() != null && !be.getWorld().isClient) {
            GlobalChatReceiverCache.unregister(be.getWorld(), be.getPos());
        }
    }
}