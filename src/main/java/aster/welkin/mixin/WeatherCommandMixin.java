package aster.welkin.mixin;

import aster.welkin.api.BiomeCategory;
import aster.welkin.api.WeatherQueryHelper;
import aster.welkin.api.WeatherState;
import aster.welkin.api.state.WeatherManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.WeatherCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WeatherCommand.class)
public class WeatherCommandMixin {
    @Inject(method = "executeClear", at = @At("HEAD"), cancellable = true)
    private static void welkin$executeClear(ServerCommandSource source, int duration, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(welkin$applyWeather(source, duration, WeatherState.CALM, "commands.weather.set.clear"));
    }

    @Inject(method = "executeRain", at = @At("HEAD"), cancellable = true)
    private static void welkin$executeRain(ServerCommandSource source, int duration, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(welkin$applyWeather(source, duration, WeatherState.MODERATE, "commands.weather.set.rain"));
    }

    @Inject(method = "executeThunder", at = @At("HEAD"), cancellable = true)
    private static void welkin$executeThunder(ServerCommandSource source, int duration, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(welkin$applyWeather(source, duration, WeatherState.SEVERE, "commands.weather.set.thunder"));
    }

    @Unique
    private static int welkin$applyWeather(ServerCommandSource source, int duration, WeatherState state, String feedbackKey) {
        ServerWorld world = source.getWorld();
        BlockPos pos = BlockPos.ofFloored(source.getPosition());
        BiomeCategory category = WeatherQueryHelper.categoryAt(world, pos);
        source.sendMessage(Text.literal("category fetched: " + category));
        if (category == BiomeCategory.MYSTERY) {
            source.sendError(Text.literal("Cannot set weather here — this position isn't in a recognized overworld biome category."));
            return 0;
        }

        int resolvedDuration = duration;
        WeatherManager manager = WeatherManager.getServerState(world);
        if (duration > 0) {
            manager.forceState(category, state, duration, world);
        } else {
          resolvedDuration = manager.forceStateWithRandomDuration(category, state, world);
        }


        source.sendFeedback(() -> Text.translatable(feedbackKey), true);
        return resolvedDuration;
    }
}
