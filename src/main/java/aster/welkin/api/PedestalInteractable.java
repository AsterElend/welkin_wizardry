package aster.welkin.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public interface PedestalInteractable {
    ItemStack stackInteractionAttempt(ItemStack stack);
    void doSneakInteraction(PlayerEntity player, Hand hand);
}