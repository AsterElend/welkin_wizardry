package aster.welkin.mixin;

import aster.welkin.cc.FrozenVelocityComponent;
import aster.welkin.cc.WelkinEntityCC;
import aster.welkin.registry.WelkinTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    void welkin$forgettingTick(CallbackInfo ci){

       PlayerEntity ambiPlayer = (PlayerEntity) (Object) this;
        if (ambiPlayer.getWorld().isClient){
            return;
        }

        ServerPlayerEntity tickingPlayer = (ServerPlayerEntity) ambiPlayer;
        boolean isInTheRiverOfForgottenThings = (tickingPlayer.isSubmergedIn(WelkinTags.LETHEAN_WATER));
        int ageSlice = tickingPlayer.age % 100;
        if (isInTheRiverOfForgottenThings && ageSlice == 0){
            WelkinEntityCC.FORGOTTEN.get(tickingPlayer).forgetRandom(tickingPlayer);
        }

        Random random = Random.create();
        if (ageSlice == random.nextInt(100) && WelkinEntityCC.FORGOTTEN.get(tickingPlayer).isAnyForgotten()){
            WelkinEntityCC.FORGOTTEN.get(tickingPlayer).rememberRandom(tickingPlayer);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void welkin$forcePostTickVelocity(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        FrozenVelocityComponent comp = WelkinEntityCC.FROZEN_MOMENTUM.get(player);

        if (comp.isLocked()) {
            Vec3d snapshot = comp.getSnapshot();
            double yVel = (player.isOnGround() && snapshot.y == 0) ? -0.08 : snapshot.y;

            // Re-lock velocity right before the tick ends to stop collision-clamping
            player.setVelocity(snapshot.x, yVel, snapshot.z);
            player.velocityModified = true;
        }
    }
}
