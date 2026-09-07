package aster.welkin.mixin.interop;

import aster.welkin.api.WeatherQueryHelper;
import aster.welkin.api.WeatherState;
import aster.welkin.api.state.WeatherManager;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "at.petrak.hexcasting.common.casting.actions.spells.great.OpWeather$Spell", remap = false)
public class OpWeatherMixin {
    @Final
    @Shadow
    private boolean rain;
    @Inject(method = "cast(Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;)V",
            at = @At("HEAD"),
            cancellable = true)
    public void welkin$hexWeatherCast(CastingEnvironment env, CallbackInfo ci){
        ServerWorld world = env.getWorld();
        BlockPos pos =  BlockPos.ofFloored(env.mishapSprayPos());
        WeatherManager manager = WeatherManager.getServerState(world);
       if (rain != WeatherQueryHelper.isActiveAt(world, pos)){
        manager.forceStateFromLoc(world, pos, WeatherState.MODERATE);
       } else {
           manager.forceStateFromLoc(world, pos, WeatherState.CALM);
       }
       ci.cancel();
    }
}
