package aster.welkin.mixin;

import aster.welkin.cc.AlchemyDiscoveriesComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @Shadow
    public PlayerEntity player;

    @Inject(method = "insertStack(Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"))
    private void welkin$onInsertStack(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (this.player.getWorld().isClient || stack.isEmpty()) return;
        Identifier id = Registries.ITEM.getId(stack.getItem());
        AlchemyDiscoveriesComponent.tryDiscover(this.player, id);
    }
}