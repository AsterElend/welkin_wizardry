package aster.welkin.client;

import aster.welkin.api.BiomeCategory;
import aster.welkin.api.WeatherState;

import java.util.EnumMap;
import java.util.Map;

public final class ClientWeatherState {
    private static final Map<BiomeCategory, WeatherState> STATES = new EnumMap<>(BiomeCategory.class);

    static {
        for (BiomeCategory cat : BiomeCategory.values()) {
            STATES.put(cat, WeatherState.CALM);
        }
    }

    private ClientWeatherState() {}

    public static WeatherState get(BiomeCategory category) {
        return STATES.getOrDefault(category, WeatherState.CALM);
    }

    public static void set(BiomeCategory category, WeatherState state) {
        STATES.put(category, state);
    }

    public static void setAll(Map<BiomeCategory, WeatherState> snapshot) {
        STATES.putAll(snapshot);
    }
}