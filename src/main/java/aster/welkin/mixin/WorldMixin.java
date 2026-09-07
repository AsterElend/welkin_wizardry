package aster.welkin.mixin;

import aster.welkin.api.WeatherQueryHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(World.class)
public abstract class WorldMixin {


    @Shadow
    @Final
    public boolean isClient;

    @Inject(method = "hasRain", at = @At("HEAD"), cancellable = true)
    private void welkin$hasRain(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (this.isClient) return; // let client fall through to vanilla
        World world = (World) (Object) this;
        ServerWorld serverWorld = (ServerWorld) world;
        if (!serverWorld.isSkyVisible(pos)) { cir.setReturnValue(false); return; }
        if (serverWorld.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos).getY() > pos.getY()) { cir.setReturnValue(false); return; }

        boolean raining = WeatherQueryHelper.isActiveAt(serverWorld, pos)
                && WeatherQueryHelper.precipitationAt(serverWorld, pos) == WeatherQueryHelper.Precipitation.RAIN;
        cir.setReturnValue(raining);
    }
}
