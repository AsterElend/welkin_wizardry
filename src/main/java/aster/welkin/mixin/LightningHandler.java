package aster.welkin.mixin;

import aster.welkin.Welkin;
import aster.welkin.recipes.LightningRecipe;
import aster.welkin.registry.WelkinRecipes;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(LightningEntity.class)
public abstract class LightningHandler {

    @Inject(method = "spawnFire", at = @At("HEAD"), cancellable = true)
    private void stopFireCreation(CallbackInfo ci) {
        if (Welkin.CONFIG.CancelLightningFire){
            ci.cancel();
        }
    }



}