package aster.welkin.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class ItemLightningMixin {

    @Inject(
            method = "onStruckByLightning",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stopDestruction(ServerWorld world, LightningEntity lightning, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;

        // If this entity *is* an item entity, prevent normal lightning behavior
        if (self instanceof ItemEntity) {
            ci.cancel();
        }
    }
}