package aster.welkin.mixin.interop;

import aster.welkin.api.WeatherQueryHelper;
import de.dafuqs.spectrum.api.predicate.world.WeatherPredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WeatherPredicate.class, remap = false)
public class WeatherPredicateMixin {

    @Shadow
    @Final
    public static WeatherPredicate ANY;

    @Shadow
    @Final
    public WeatherPredicate.WeatherCondition weatherCondition;

    @Inject(
            method = "test",
            at = @At("HEAD"),
            cancellable = true
    )
    public void welkin$fixWeatherTestForSpectrum(ServerWorld world, BlockPos pos, CallbackInfoReturnable<Boolean> cir){

        if ((WeatherPredicate) (Object) this == ANY) cir.setReturnValue(true);
      cir.setReturnValue(
              switch(this.weatherCondition){
                  case CLEAR_SKY ->  !WeatherQueryHelper.isActiveAt(world, pos);
                  case RAIN -> WeatherQueryHelper.isActiveAt(world, pos);
                  case STRICT_RAIN -> WeatherQueryHelper.isActiveAt(world, pos) && !WeatherQueryHelper.isActiveAt(world, pos);
                  case THUNDER -> WeatherQueryHelper.isSevereAt(world, pos);
                  case NOT_THUNDER -> !WeatherQueryHelper.isSevereAt(world, pos);
                  default -> true;
              }
      );

      cir.cancel();
    }

}
