package aster.welkin.api.state;

import aster.welkin.api.BiomeCategory;
import aster.welkin.api.WeatherQueryHelper;
import aster.welkin.api.WeatherState;
import aster.welkin.packet.WeatherSyncPackets;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import java.util.EnumMap;
import java.util.Map;

public class WeatherManager extends PersistentState {
    private static final String KEY = "welkin_weather";




    private static final Map<WeatherState, IntRange> DURATION_TICKS = Map.of(
            WeatherState.CALM,     new IntRange(6000, 12000),  // 5-10 min
            WeatherState.MODERATE, new IntRange(3600, 9600),   // 3-8 min
            WeatherState.SEVERE,   new IntRange(1200, 4800)    // 1-4 min
    );


    private static final Map<WeatherState, Map<WeatherState, Integer>> TRANSITIONS = Map.of(
            WeatherState.CALM,     Map.of(WeatherState.CALM, 20,  WeatherState.MODERATE, 80),
            WeatherState.MODERATE, Map.of(WeatherState.CALM, 40,  WeatherState.MODERATE, 30, WeatherState.SEVERE, 30),
            WeatherState.SEVERE,   Map.of(WeatherState.MODERATE, 70, WeatherState.SEVERE, 30)
    );

    private final Map<BiomeCategory, CategoryWeather> categories = new EnumMap<>(BiomeCategory.class);

    public WeatherManager() {
        for (BiomeCategory cat : BiomeCategory.values()) {
            categories.put(cat, new CategoryWeather(WeatherState.CALM, cat == BiomeCategory.MYSTERY
                    ? Integer.MAX_VALUE
                    : randomDuration(WeatherState.CALM, Random.create())));
        }
    }

    public static WeatherManager getServerState(ServerWorld world) {
        PersistentStateManager manager = world.getPersistentStateManager();
       return manager.getOrCreate(
               WeatherManager::fromNbt,
               WeatherManager::new,
               KEY
       );
    }

    public void tick(ServerWorld world) {
        Random random = world.getRandom();
        boolean changed = false;

        for (BiomeCategory cat : BiomeCategory.values()) {
            if (cat == BiomeCategory.MYSTERY) continue;

            CategoryWeather cw = categories.get(cat);
            cw.ticksRemaining--;

            if (cw.ticksRemaining <= 0) {
                WeatherState next = rollNextState(cw.state, random);
                cw.state = next;
                cw.ticksRemaining = randomDuration(next, random);
                changed = true;
                onWeatherChanged(world, cat, next); // hook for events/sync later
            }
        }

        if (changed) markDirty();
    }

    private WeatherState rollNextState(WeatherState from, Random random) {
        Map<WeatherState, Integer> weights = TRANSITIONS.get(from);
        int total = weights.values().stream().mapToInt(Integer::intValue).sum();
        int roll = random.nextInt(total);
        int cumulative = 0;
        for (var entry : weights.entrySet()) {
            cumulative += entry.getValue();
            if (roll < cumulative) return entry.getKey();
        }
        return from; // shouldn't happen
    }

    private int randomDuration(WeatherState state, Random random) {
        IntRange range = DURATION_TICKS.get(state);
        return range.min() + random.nextInt(range.max() - range.min());
    }

    public void onWeatherChanged(ServerWorld world, BiomeCategory category, WeatherState newState) {
        for (ServerPlayerEntity player : world.getPlayers()) {
            ServerPlayNetworking.send(player, WeatherSyncPackets.DELTA_SYNC,
                    WeatherSyncPackets.writeDelta(category, newState));
        }
    }

    public WeatherState getState(BiomeCategory category) {
        return categories.get(category).state;
    }

    // --- Debug/command support ---
    public void forceState(BiomeCategory category, WeatherState state, int durationTicks, ServerWorld world) {
        CategoryWeather cw = categories.get(category);
        cw.state = state;
        cw.ticksRemaining = durationTicks;
        markDirty();
        onWeatherChanged(world, category, state);
    }

    public int forceStateWithRandomDuration(BiomeCategory cat, WeatherState state, ServerWorld world){
        int duration = randomDuration(state, world.getRandom());
        forceState(cat, state, duration, world);
        return duration;
    }

    public void forceStateFromLoc(ServerWorld world, BlockPos pos, WeatherState state){
        forceStateWithRandomDuration(WeatherQueryHelper.categoryAt(world, pos), state, world);
    }

    // --- NBT persistence ---
    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        for (BiomeCategory cat : BiomeCategory.values()) {
            CategoryWeather cw = categories.get(cat);
            NbtCompound catNbt = new NbtCompound();
            catNbt.putString("state", cw.state.name());
            catNbt.putInt("ticks", cw.ticksRemaining);
            nbt.put(cat.name(), catNbt);
        }
        return nbt;
    }

    public static WeatherManager fromNbt(NbtCompound nbt) {
        WeatherManager wm = new WeatherManager();
        for (BiomeCategory cat : BiomeCategory.values()) {
            if (!nbt.contains(cat.name())) continue;
            NbtCompound catNbt = nbt.getCompound(cat.name());
            WeatherState state = WeatherState.valueOf(catNbt.getString("state"));
            int ticks = catNbt.getInt("ticks");
            wm.categories.put(cat, new CategoryWeather(state, ticks));
        }
        return wm;
    }

    private static class CategoryWeather {
        WeatherState state;
        int ticksRemaining;

        CategoryWeather(WeatherState state, int ticksRemaining) {
            this.state = state;
            this.ticksRemaining = ticksRemaining;
        }
    }

    // in WeatherManager
    public void sendFullSync(ServerPlayerEntity player) {
        Map<BiomeCategory, WeatherState> snapshot = new EnumMap<>(BiomeCategory.class);
        for (var entry : categories.entrySet()) {
            snapshot.put(entry.getKey(), entry.getValue().state);
        }
        ServerPlayNetworking.send(player, WeatherSyncPackets.FULL_SYNC, WeatherSyncPackets.writeFull(snapshot));
    }

    private record IntRange(int min, int max) {}
}
