package aster.welkin.mixin;


import aster.welkin.client.WelkinFogState;
import aster.welkin.registry.WelkinTags;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class FogMixin {

    @Inject(
            method = "applyFog",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onApplyFog(
            Camera camera,
            BackgroundRenderer.FogType fogType,
            float viewDistance,
            boolean thickFog,
            float tickDelta,
            CallbackInfo ci
    ) {
        if (!isInCustomFluid(camera)) return;

        // Mimic what water fog does:
        if (fogType == BackgroundRenderer.FogType.FOG_SKY) {
            RenderSystem.setShaderFogStart(0.0f);
            RenderSystem.setShaderFogEnd(viewDistance * 0.5f); // tighten for denser fog
        } else {
            RenderSystem.setShaderFogStart(0.25f);
            RenderSystem.setShaderFogEnd(12.0f); // short particleRadius = thick fog
        }

        ci.cancel();
    }

    @Inject(method = "applyFog", at = @At("TAIL"))
    private static void welkin$applyWeatherFog(Camera camera, BackgroundRenderer.FogType fogType,
                                               float clearEnd, boolean thickFog, float tickDelta,
                                               CallbackInfo ci) {
        if (thickFog) return; // fluid/submersion fog takes priority, untouched

        float weatherEnd = WelkinFogState.getFogEnd();
        float currentEnd = RenderSystem.getShaderFogEnd();
        if (weatherEnd >= currentEnd) return; // only tighten, never loosen

        RenderSystem.setShaderFogStart(Math.min(RenderSystem.getShaderFogStart(), WelkinFogState.getFogStart()));
        RenderSystem.setShaderFogEnd(weatherEnd);
        float[] color = WelkinFogState.getFogColor();
        RenderSystem.setShaderFogColor(color[0], color[1], color[2]);
    }


    @Inject(
            method = "render",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onRender(
            Camera camera,
            float tickDelta,
            ClientWorld world,
            int viewDistance,
            float skyDarkness,
            CallbackInfo ci
    ) {
        if (!isInCustomFluid(camera)) return;

        // Your fluid's fog color (R, G, B) — this example is a murky green
        RenderSystem.clearColor(1f, 0.125f, 0.624f, 0.9f);
        ci.cancel();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    @Unique
    private static boolean isInCustomFluid(Camera camera) {
        Entity entity = camera.getFocusedEntity();
        World world = entity.getWorld();
        BlockPos eyePos = BlockPos.ofFloored(camera.getPos());
        FluidState fluidState = world.getFluidState(eyePos);
        return fluidState.isIn(WelkinTags.LETHEAN_WATER);
    }
}
