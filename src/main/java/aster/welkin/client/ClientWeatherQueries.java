package aster.welkin.client;

import aster.welkin.api.BiomeCategory;
import aster.welkin.api.WeatherState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClientWeatherQueries {
    public static BiomeCategory categoryAt(World world, BlockPos pos) {
        return BiomeCategory.fromBiomeEntry(world.getBiome(pos));
    }

    public static WeatherState stateAt(World world, BlockPos pos) {
        BiomeCategory category = categoryAt(world, pos);
        return category == BiomeCategory.MYSTERY ? WeatherState.CALM : ClientWeatherState.get(category);
    }

    public static boolean isThisStateHere(World world, BlockPos pos, WeatherState test){
        return (stateAt(world, pos) == test);
    }
}
