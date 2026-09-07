package aster.welkin.registry.particle;

import aster.welkin.Welkin;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class WelkinParticles {
    public static void registerClient() {
   ParticleFactoryRegistry.getInstance().register(SNOW, WelkinSnowParticle.DefaultFactory::new);
   ParticleFactoryRegistry.getInstance().register(RAIN, WelkinRainParticle.DefaultFactory::new);
   ParticleFactoryRegistry.getInstance().register(GUST, GustParticle.DefaultFactory::new);


    }

    public static void register(){};
    public static final DefaultParticleType SNOW = Registry.register(Registries.PARTICLE_TYPE, Welkin.id("snow"), FabricParticleTypes.simple(true));
    public static final DefaultParticleType RAIN = Registry.register(Registries.PARTICLE_TYPE, Welkin.id("rain"), FabricParticleTypes.simple(true));
    public static final DefaultParticleType GUST = Registry.register(Registries.PARTICLE_TYPE, Welkin.id("gust"), FabricParticleTypes.simple(true));
}
