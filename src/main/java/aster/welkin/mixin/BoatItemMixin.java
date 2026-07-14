package aster.welkin.mixin;


import aster.welkin.api.EnchantableBoatEntity;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoatItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(BoatItem.class)
public abstract class BoatItemMixin extends Item {

    public BoatItemMixin(Settings settings) {
        super(settings);
    }

    /**
     * BoatItem#use raycasts, then calls world.spawnEntity(boatEntity) to place
     * the boat. We redirect that single call so the freshly spawned entity
     * immediately inherits whatever enchantments were on the ItemStack used to
     * place it.
     */

    @Redirect(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    private boolean welkin$copyEnchantsOnPlace(World instance, Entity entity,
                                                    @Local(argsOnly = true) PlayerEntity user, @Local(argsOnly = true) Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
        if (!enchantments.isEmpty() && entity instanceof EnchantableBoatEntity enchantable) {
            enchantable.welkin$setBoatEnchantments(enchantments);
        }
        return instance.spawnEntity(entity);
    }

    @Override
    public boolean isEnchantable(ItemStack stack){
        return stack.getCount() == 1;
    }
}