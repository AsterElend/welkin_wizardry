package aster.welkin.compat;

import aster.welkin.api.BiomeCategory;
import aster.welkin.api.WeatherQueryHelper;
import aster.welkin.api.WeatherState;
import aster.welkin.api.state.WeatherManager;
import de.dafuqs.spectrum.api.recipe.FusionShrineRecipeWorldEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class SpectrumWeatherOverrides {
    public static void register(){
        FusionShrineRecipeWorldEffect.register("weather_clear", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
            @Override
            public void trigger(ServerWorld world, BlockPos pos) {
                BiomeCategory category = WeatherQueryHelper.categoryAt(world, pos);
                if (category == BiomeCategory.MYSTERY) return;

                forceWeather(world, category, WeatherState.CALM);
            }
        });

        FusionShrineRecipeWorldEffect.register("weather_rain", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
            @Override
            public void trigger(ServerWorld world, BlockPos pos) {
                BiomeCategory category = WeatherQueryHelper.categoryAt(world, pos);
                if (category == BiomeCategory.MYSTERY) return;

                forceWeather(world, category, WeatherState.MODERATE);
                world.playSound(null, pos.up(), SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.8F, 0.9F + world.random.nextFloat() * 0.2F);
            }
        });

        FusionShrineRecipeWorldEffect.register("weather_thunder", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
            @Override
            public void trigger(ServerWorld world, BlockPos pos) {
                BiomeCategory category = WeatherQueryHelper.categoryAt(world, pos);
                if (category == BiomeCategory.MYSTERY) return;

                forceWeather(world, category, WeatherState.SEVERE);
                world.playSound(null, pos.up(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 0.8F, 0.9F + world.random.nextFloat() * 0.2F);
            }
        });

        FusionShrineRecipeWorldEffect.register("weather_rain_short", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
            @Override
            public void trigger(ServerWorld world, BlockPos pos) {
                BiomeCategory category = WeatherQueryHelper.categoryAt(world, pos);
                if (category == BiomeCategory.MYSTERY) return;

                forceWeather(world, category, WeatherState.MODERATE, 5000);
                world.playSound(null, pos.up(), SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.8F, 0.9F + world.random.nextFloat() * 0.2F);
            }
        });

        FusionShrineRecipeWorldEffect.register("weather_thunder_short", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
            @Override
            public void trigger(ServerWorld world, BlockPos pos) {
                BiomeCategory category = WeatherQueryHelper.categoryAt(world, pos);
                if (category == BiomeCategory.MYSTERY) return;

                forceWeather(world, category, WeatherState.SEVERE, 3500);
                world.playSound(null, pos.up(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 0.8F, 0.9F + world.random.nextFloat() * 0.2F);
            }
        });

    };

    public static void forceWeather(ServerWorld world, BiomeCategory cat, WeatherState state, int dur){
        WeatherManager manager = WeatherManager.getServerState(world);
        manager.forceState(cat, state, dur, world);

    }
    public static void forceWeather(ServerWorld world, BiomeCategory cat, WeatherState state){
        WeatherManager manager = WeatherManager.getServerState(world);
        manager.forceStateWithRandomDuration(cat, state, world);

    }
}
