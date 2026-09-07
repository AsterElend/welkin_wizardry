package aster.welkin.api;

public enum WeatherState {
    CALM, MODERATE, SEVERE;

    public static WeatherState intensityToState(float intensity) {
        return intensity < 0.5F ? WeatherState.MODERATE : WeatherState.SEVERE; // just for particle *type* selection
    }
}
