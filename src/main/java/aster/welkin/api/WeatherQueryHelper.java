package aster.welkin.api;

import aster.welkin.api.state.WeatherManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class WeatherQueryHelper {
    public static BiomeCategory categoryAt(ServerWorld world, BlockPos pos) {
        RegistryEntry<Biome> biome = world.getBiome(pos);
        return BiomeCategory.fromBiomeEntry(biome);
    }

    public static WeatherState stateAt(ServerWorld world, BlockPos pos) {
        BiomeCategory category = categoryAt(world, pos);
        if (category == BiomeCategory.MYSTERY) return WeatherState.CALM;
        return WeatherManager.getServerState(world).getState(category);
    }

    public static boolean isActiveAt(ServerWorld world, BlockPos pos) {
        return stateAt(world, pos) != WeatherState.CALM; // MODERATE or SEVERE
    }

    public static boolean isSevereAt(ServerWorld world, BlockPos pos) {
        return stateAt(world, pos) == WeatherState.SEVERE;
    }

    public enum Precipitation { NONE, RAIN, SNOW, SAND }

    public static Precipitation precipitationFor(BiomeCategory category) {
        return switch (category) {
            case HOT_WET, TEMPERATE_WET -> Precipitation.RAIN;
            case COLD_WET -> Precipitation.SNOW;
            case HOT_DRY, TEMPERATE_DRY, COLD_DRY -> Precipitation.SAND;
            case MYSTERY -> Precipitation.NONE;
        };
    }

    public static Precipitation precipitationAt(ServerWorld world, BlockPos pos) {
        return precipitationFor(categoryAt(world, pos));
    }

}
