package aster.welkin.client;

import aster.welkin.Welkin;
import aster.welkin.WelkinClient;
import aster.welkin.api.BiomeCategory;
import aster.welkin.api.WeatherQueryHelper;
import aster.welkin.api.WeatherState;
import aster.welkin.registry.particle.WelkinParticles;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import org.jetbrains.annotations.Nullable;

public class WeatherParticleSummoner {


    public static final BlockPos.Mutable pos = new BlockPos.Mutable();

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(WeatherParticleSummoner::tick);
    }

    private static void spawnParticle(ClientWorld world, double x, double y, double z){
        // 1. Clamp the primitive y value directly
        if (y > Welkin.CONFIG.cloudHeight) {
            y = Welkin.CONFIG.cloudHeight;
        }

        // 2. Now update the shared pos tracking object with the clamped value
        pos.set(x, y, z);

        if (ClientWeatherQueries.isThisStateHere(world, pos, WeatherState.CALM)) return;
        if (WelkinClient.particleCount > 1500) return;

        WeatherQueryHelper.Precipitation precipitation = WeatherQueryHelper.precipitationFor(ClientWeatherQueries.categoryAt(world, pos));
        if (precipitation == WeatherQueryHelper.Precipitation.RAIN){
            if (world.random.nextFloat() < 1){
                // 3. This will now correctly use the updated, clamped y coordinate
                world.addParticle(WelkinParticles.RAIN, x, y, z, 0, 0, 0);
            }
        } else if (precipitation == WeatherQueryHelper.Precipitation.SNOW){
            if (world.random.nextFloat() < 40f / 100f){
                world.addParticle(WelkinParticles.SNOW, x, y, z, 0, 0, 0);
            }
        } else if (precipitation == WeatherQueryHelper.Precipitation.SAND){
            if (world.random.nextFloat() < 8f / 10f){
                y = world.getTopY(Heightmap.Type.MOTION_BLOCKING, (int) x, (int) z);
                world.addParticle(WelkinParticles.GUST, x, y, z, 0, 0, 0);
            }
        }
    }

    private static void tick(MinecraftClient client) {
        if (client.cameraEntity == null) return;
        if (client.world == null) return;
        if (client.isPaused()) return;
        Entity entity = client.cameraEntity;
        double camY = entity.getY();
        if (camY > 217) return;


        Random random = Random.create();
        for (int pass = 0; pass < 100; pass++){
            float theta = (MathHelper.TAU * random.nextFloat());
            float phi = (float) Math.acos(2 * random.nextFloat() -1);
            double x = 25 * MathHelper.sin(phi) * Math.cos(theta);
            double y = 25 * MathHelper.sin(phi) * Math.sin(theta);
            double z = 25 * MathHelper.cos(phi);
            pos.set(x + entity.getBlockX(), y + entity.getBlockY(), z + entity.getBlockZ());
            int topY = client.world.getTopY(Heightmap.Type.MOTION_BLOCKING, pos.getX(), pos.getZ());
            if (pos.getY() < topY - 5) continue;
            Vec3d randshift = new Vec3d(pos.getX() + random.nextFloat(), pos.getY() + random.nextFloat(), pos.getZ() + random.nextFloat());
            if (ClientWeatherQueries.isThisStateHere(client.world, BlockPos.ofFloored(randshift), WeatherState.CALM)) {
                pass -= 1;
                continue;
            };
            pos.set(BlockPos.ofFloored(randshift));
            spawnParticle(client.world,pos.getX(), pos.getY(), pos.getZ());
        }


    }


}
