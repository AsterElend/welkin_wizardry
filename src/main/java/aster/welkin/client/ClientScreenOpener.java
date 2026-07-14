package aster.welkin.client;

import aster.welkin.Welkin;
import aster.welkin.api.SCREEN_INVOCATIONS;
import aster.welkin.registry.screen.AlchemySlateScreen;
import net.minecraft.client.MinecraftClient;

public class ClientScreenOpener {
    public static void open(SCREEN_INVOCATIONS type, boolean debug) {
        MinecraftClient client = MinecraftClient.getInstance();
        switch (type) {
            case ALCHEMY_SLATE -> client.setScreen(new AlchemySlateScreen(client.player, debug));
            default -> Welkin.LOGGER.warn("No screen registered for type {}", type);
        }
    }
}
