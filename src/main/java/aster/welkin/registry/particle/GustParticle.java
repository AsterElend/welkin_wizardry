package aster.welkin.registry.particle;

import aster.welkin.Welkin;
import aster.welkin.api.WeatherState;
import aster.welkin.client.ClientWeatherQueries;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.jetbrains.annotations.Nullable;

public class GustParticle extends WeatherParticle {
    protected GustParticle(ClientWorld world, double x, double y, double z){
        super(world, x, y, z);
        this.scale = 2F;
        this.gravityStrength = 0.2f;
        if (ClientWeatherQueries.isThisStateHere(world, pos, WeatherState.SEVERE)){
            velocityX = gravityStrength * 0.3f;
        } else {
            velocityX = gravityStrength;
        }
        velocityX = velocityX * WeatherParticle.yLevelWindAdjustment(y);
        velocityZ = velocityX;



    }

    @Override
    public void tick() {
        super.tick();
        if (this.onGround) {
            this.velocityY = 0.01F;
        }
        this.removeIfObstructed();
        if (!this.world.getFluidState(this.pos).isEmpty()) {
            this.shouldFade = true;
            this.gravityStrength = 0;
        } else {
            boolean sandstorm = ClientWeatherQueries.isThisStateHere(world, pos, WeatherState.SEVERE);
            if (sandstorm){
                this.velocityX = gravityStrength * 0.3f * WeatherParticle.yLevelWindAdjustment(y);
            } else {
                this.velocityX = gravityStrength;
            }
            this.velocityZ = velocityX;
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
        SpriteProvider provider;
        public DefaultFactory(SpriteProvider sprite){
        provider = sprite;
        }
        @Override
        public @Nullable Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
           GustParticle particle = new GustParticle(world, x, y, z);
           particle.setSprite(provider);
           return particle;
        }
    }

}
