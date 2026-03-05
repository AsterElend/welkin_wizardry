package aster.welkin.cc;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ForceRegenHandler {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

                SkyForceComponent force = WelkinEntityCC.FORCE.get(player);
                if (force.getForce() >= SkyForceComponent.MAX_FORCE) continue;

                World world = player.getWorld();
                BlockPos pos = player.getBlockPos();

                boolean sky = world.isSkyVisible(pos);
                boolean raining = world.isRaining();
                boolean thundering = world.isThundering();

                int regen = 0;

                if (sky) {
                    regen = 2; // normal regen
                    if (raining) regen = 4;
                    if (thundering) regen = 6;
                } else {
                    regen = 1; // slower indoors
                }

                force.regenerate(regen);
            }
        });
    }
}
