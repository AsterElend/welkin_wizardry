package aster.welkin.client;

import aster.welkin.api.BiomeCategory;
import aster.welkin.api.WeatherQueryHelper;
import aster.welkin.api.WeatherState;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

public final class WelkinFogState {
    private WelkinFogState() {}

    private static final float OFF_END = 100_000f; // sentinel: "no weather fog"
    private static final float LERP_ALPHA = 0.15f;  // ~1s to mostly settle at 20 ticks/sec

    private static float currentStart = OFF_END * 0.2f;
    private static float currentEnd = OFF_END;
    private static final float[] currentColor = {1f, 1f, 1f};
    private static float currentSkyDarken = 0f;

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world == null || client.player == null) return;
            tick(client.world, client.player.getBlockPos());
        });
    }

    private static void tick(ClientWorld world, BlockPos pos) {
        float[] target = computeTarget(world, pos);
        currentStart = lerp(currentStart, target[0]);
        currentEnd = lerp(currentEnd, target[1]);
        currentColor[0] = lerp(currentColor[0], target[2]);
        currentColor[1] = lerp(currentColor[1], target[3]);
        currentColor[2] = lerp(currentColor[2], target[4]);
        currentSkyDarken = lerp(currentSkyDarken, target[5]);
    }

    private static float[] computeTarget(ClientWorld world, BlockPos pos) {
        float[] off = {OFF_END * 0.2f, OFF_END, 1f, 1f, 1f, 0f};

        BiomeCategory category = ClientWeatherQueries.categoryAt(world, pos);
        if (category == BiomeCategory.MYSTERY) return off;

        WeatherState state = ClientWeatherQueries.stateAt(world, pos);
        if (state == WeatherState.CALM) return off;
        if (!world.isSkyVisible(pos)) return off;

        WeatherQueryHelper.Precipitation precip = WeatherQueryHelper.precipitationFor(category);
        if (precip == WeatherQueryHelper.Precipitation.NONE) return off;

        float end = switch (state) {
            case MODERATE -> precip == WeatherQueryHelper.Precipitation.SAND ? 40f : 55f;
            case SEVERE   -> precip == WeatherQueryHelper.Precipitation.SAND ? 12f : 20f;
            default -> OFF_END;
        };
        float start = end * 0.2f;
        float[] color = fogColor(precip, state);
        float darken = state == WeatherState.SEVERE ? 0.45f : 0.18f;

        return new float[]{start, end, color[0], color[1], color[2], darken};
    }

    private static float[] fogColor(WeatherQueryHelper.Precipitation precip, WeatherState state) {
        boolean severe = state == WeatherState.SEVERE;
        return switch (precip) {
            case RAIN -> severe ? new float[]{0.38f, 0.42f, 0.45f} : new float[]{0.55f, 0.58f, 0.60f};
            case SNOW -> severe ? new float[]{0.75f, 0.78f, 0.82f} : new float[]{0.82f, 0.85f, 0.88f};
            case SAND -> severe ? new float[]{0.65f, 0.48f, 0.28f} : new float[]{0.72f, 0.62f, 0.42f};
            case NONE -> new float[]{1f, 1f, 1f};
        };
    }

    private static float lerp(float from, float to) {
        return from + (to - from) * LERP_ALPHA;
    }

    public static float getFogStart() { return currentStart; }
    public static float getFogEnd() { return currentEnd; }
    public static float[] getFogColor() { return currentColor; }
    public static float getSkyDarken() { return currentSkyDarken; }
}