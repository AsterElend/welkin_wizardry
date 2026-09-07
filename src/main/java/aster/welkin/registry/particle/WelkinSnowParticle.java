package aster.welkin.registry.particle;

import aster.welkin.Welkin;
import aster.welkin.WelkinClient;
import aster.welkin.api.WeatherQueryHelper;
import aster.welkin.api.WeatherState;
import aster.welkin.client.ClientWeatherQueries;
import com.ibm.icu.text.MessagePattern;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.texture.atlas.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class WelkinSnowParticle extends WeatherParticle{
    float rotationAmount;

    protected WelkinSnowParticle(ClientWorld world, double x, double y, double z){
        super(world, x, y, z);
        this.scale = 0.5F;
        this.gravityStrength = 0.08F;
        this.rotationAmount = 0.03F;
        this.velocityY = -gravityStrength;

        if (ClientWeatherQueries.isThisStateHere(world, pos, WeatherState.SEVERE)){
            this.velocityX = gravityStrength * 3F;
        } else {
            this.velocityX = gravityStrength;
        }
        this.velocityX = this.velocityX * WeatherParticle.yLevelWindAdjustment(y);
        this.velocityZ = this.velocityX;
        if (world.getRandom().nextBoolean()){
            this.rotationAmount = 1;
        } else {
            this.rotationAmount = -1;
        }

    }

    public void tick(){
        super.tick();
        this.prevAngle = this.angle;
        this.angle = this.prevAngle * (ClientWeatherQueries.isThisStateHere(world, pos, WeatherState.SEVERE)
        ? 0.05F: 0.03F);
        if (this.onGround || this.removeIfObstructed()){
            this.markDead();
        }
    }

    @Override
    public ParticleTextureSheet getType(){
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
        private SpriteProvider provider;
        public DefaultFactory(SpriteProvider sprite){
        this.provider = sprite;
        }
        @Override
        public @Nullable Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            WelkinSnowParticle snowParticle = new WelkinSnowParticle(world, x, y, z);
            snowParticle.setSprite(provider);
            return snowParticle;
        }
    }

}
