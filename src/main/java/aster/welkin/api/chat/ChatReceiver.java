package aster.welkin.api.chat;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public interface ChatReceiver {
     //return true to cancel normal chat behavior
     boolean onChatReceived(String message, ServerPlayerEntity sender);

     default void registerChatReceiver() {
          if (this instanceof BlockEntity be && be.getWorld() != null && !be.getWorld().isClient) {
               ChatReceiverCache.register(be.getWorld(), be.getPos());
          }
     }

     default void unregisterChatReceiver() {
          if (this instanceof BlockEntity be && be.getWorld() != null && !be.getWorld().isClient) {
               ChatReceiverCache.unregister(be.getWorld(), be.getPos());
          }
     }
}