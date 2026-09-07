package aster.welkin.packet;

import aster.welkin.Welkin;
import aster.welkin.api.BiomeCategory;
import aster.welkin.api.WeatherState;
import aster.welkin.api.state.WeatherManager;
import aster.welkin.client.ClientWeatherState;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.EnumMap;
import java.util.Map;

public class WeatherSyncPackets {
    public static final Identifier DELTA_SYNC = Welkin.id("weather_delta_sync");
    public static final Identifier FULL_SYNC = Welkin.id("weather_full_sync");

    public static PacketByteBuf writeDelta(BiomeCategory category, WeatherState state) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeEnumConstant(category);
        buf.writeEnumConstant(state);
        return buf;
    }

    public static PacketByteBuf writeFull(Map<BiomeCategory, WeatherState> states) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(states.size());
        for (var entry : states.entrySet()) {
            buf.writeEnumConstant(entry.getKey());
            buf.writeEnumConstant(entry.getValue());
        }
        return buf;
    }

    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(DELTA_SYNC, (client, handler, buf, sender) -> {
            BiomeCategory category = buf.readEnumConstant(BiomeCategory.class);
            WeatherState state = buf.readEnumConstant(WeatherState.class);
            client.execute(() -> ClientWeatherState.set(category, state));
        });

        ClientPlayNetworking.registerGlobalReceiver(FULL_SYNC, (client, handler, buf, sender) -> {
            int count = buf.readVarInt();
            Map<BiomeCategory, WeatherState> snapshot = new EnumMap<>(BiomeCategory.class);
            for (int i = 0; i < count; i++) {
                snapshot.put(buf.readEnumConstant(BiomeCategory.class), buf.readEnumConstant(WeatherState.class));
            }
            client.execute(() -> ClientWeatherState.setAll(snapshot));
        });
    }
}

