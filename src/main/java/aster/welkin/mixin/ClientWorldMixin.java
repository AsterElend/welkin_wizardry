package aster.welkin.mixin;

import aster.welkin.client.WelkinFogState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {

    @Inject(method = "getSkyColor", at = @At("RETURN"), cancellable = true)
    private void welkin$tintSkyForWeather(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        float darken = WelkinFogState.getSkyDarken();
        if (darken <= 0f) return;

        Vec3d base = cir.getReturnValue();
        float[] fogColor = WelkinFogState.getFogColor();
        Vec3d blended = base.multiply(1.0 - darken)
                .add(fogColor[0] * darken, fogColor[1] * darken, fogColor[2] * darken);
        cir.setReturnValue(blended);
    }
}
