package aster.welkin.mixin;

import aster.welkin.cc.WelkinEntityCC;
import aster.welkin.registry.WelkinTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerEntity.class)
public abstract class PlayerLetheanCheck {
    @Inject(method = "tick", at = @At("TAIL"))
    void forgettingTick(CallbackInfo ci){

       PlayerEntity ambiPlayer = (PlayerEntity) (Object) this;
        if (ambiPlayer.getWorld().isClient){
            return;
        }

        ServerPlayerEntity tickingPlayer = (ServerPlayerEntity) ambiPlayer;
        boolean isInTheRiverOfForgottenThings = (tickingPlayer.isSubmergedIn(WelkinTags.LETHE));
        int ageSlice = tickingPlayer.age % 100;
        if (isInTheRiverOfForgottenThings && ageSlice == 0){
            WelkinEntityCC.FORGOTTEN.get(tickingPlayer).forgetRandom(tickingPlayer);
        }

        Random random = Random.create();
        if (ageSlice == random.nextInt(100) && WelkinEntityCC.FORGOTTEN.get(tickingPlayer).isAnyForgotten()){
            WelkinEntityCC.FORGOTTEN.get(tickingPlayer).rememberRandom(tickingPlayer);
        }
    }


}
