package aster.welkin.mixin;

import aster.welkin.cc.FrozenVelocityComponent;
import aster.welkin.cc.WelkinEntityCC;
import aster.welkin.registry.WelkinEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Shadow
    protected ItemStack activeItemStack;

    @Shadow
    protected int itemUseTimeLeft;

    @Inject(method = "travel", at = @At("HEAD"))
    private void welkin$clampTravelVelocity(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof PlayerEntity player) {
            FrozenVelocityComponent comp = WelkinEntityCC.FROZEN_MOMENTUM.get(player);
            if (comp.isLocked()) {
                Vec3d snapshot = comp.getSnapshot();

                // Keep ground connection if horizontal speed is maintained
                double yVel = (player.isOnGround() && snapshot.y == 0) ? -0.08 : snapshot.y;
                player.setVelocity(snapshot.x, yVel, snapshot.z);

            }
        }
    }

    @Inject(method = "takeKnockback", at = @At("HEAD"), cancellable = true)
    private void welkin$cancelKnockback(double strength, double x, double z, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof PlayerEntity player) {
            if (WelkinEntityCC.FROZEN_MOMENTUM.get(player).isLocked()) {
                ci.cancel(); // Completely immune to knockback changes
            }
        }
    }

    @Inject(method = "updateLimbs(Z)V", at = @At("HEAD"), cancellable = true)
    protected void welkin$dontWiggleLimbs(boolean flutter, CallbackInfo ci){
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof PlayerEntity player){
            if (WelkinEntityCC.FROZEN_MOMENTUM.get(player).isLocked()){
                ci.cancel();
            }
        }
    }

    @Inject(method = "tickActiveItemStack", at = @At("HEAD"))
    private void welkin$halveEatTime(CallbackInfo ci){
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.hasStatusEffect(WelkinEffects.PECKISHNESS)){
            if (this.activeItemStack != null && this.activeItemStack.isFood()){
                if (this.itemUseTimeLeft > 0){
                    this.itemUseTimeLeft--;
                }
            }
        }

    }


}
