package aster.welkin.mixin;

import aster.welkin.Welkin;
import aster.welkin.recipes.ItemEntityTransmutationRecipe;
import aster.welkin.recipes.LightningRecipe;
import aster.welkin.registry.WelkinRecipes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(
            method = "onStruckByLightning",
            at = @At("HEAD"),
            cancellable = true
    )
    private void welkin$stopLightningDestruction(ServerWorld server, LightningEntity lightning, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if (self instanceof ItemEntity itemEntity) {
            SimpleInventory inv = new SimpleInventory(itemEntity.getStack());
            Optional<LightningRecipe> match =  server.getRecipeManager().getFirstMatch(
                    WelkinRecipes.LIGHTNING_TYPE, inv, server
            );

            match.ifPresent(lightningRecipe -> lightningRecipe.transmuteAStack(itemEntity, server, ParticleTypes.SCRAPE));

            if (Welkin.CONFIG.CancelLightningItemDestruction){
                ci.cancel();
            }
        }
    }



}