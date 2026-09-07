package aster.welkin.registry.particle;

import aster.welkin.Welkin;
import aster.welkin.api.WeatherState;
import aster.welkin.client.ClientWeatherQueries;
import de.dafuqs.spectrum.particle.client.RaindropParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class WelkinRainParticle extends WeatherParticle{
    protected WelkinRainParticle(ClientWorld world, double x, double y, double z){
        super(world, x, y, z);
        this.scale = 2F;
        this.gravityStrength = 1F;

        // CHANGE: Make this negative so the rain falls downward immediately
        this.velocityY = -gravityStrength;

        if (ClientWeatherQueries.isThisStateHere(world, pos, WeatherState.SEVERE)){
            this.velocityX = gravityStrength * 0.5F;
        } else {
            this.velocityX = gravityStrength * 0.3F;
        }
        this.velocityX = this.velocityX * WeatherParticle.yLevelWindAdjustment(y);
        this.velocityZ = velocityX;
        this.maxAge = Welkin.CONFIG.particleRadius * 5;
        Vec3d vec = MinecraftClient.getInstance().cameraEntity.getPos();
        this.angle = (float) (Math.atan2(x - vec.x, z - vec.z) + MathHelper.HALF_PI);
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickPercentage) {
        Vector3f camPos = camera.getPos().toVector3f();
        float x = (float) (MathHelper.lerp(tickPercentage, this.prevPosX, this.x) - camPos.x);
        float y = (float) (MathHelper.lerp(tickPercentage, this.prevPosY, this.y) - camPos.y);
        float z = (float) (MathHelper.lerp(tickPercentage, this.prevPosZ, this.z) - camPos.z);


        Vector3f delta = new Vector3f((float) this.velocityX, (float) this.velocityY, (float) this.velocityZ);
        final float angle = (float) Math.acos(delta.normalize().y);
        Vector3f axis = new Vector3f(-delta.z(), 0, delta.x()).normalize();
        Quaternionf quaternion = new Quaternionf(new AxisAngle4f(-angle, axis));


        quaternion.mul(RotationAxis.NEGATIVE_Y.rotation(this.angle));
        quaternion = this.flipItTurnwaysIfBackfaced(quaternion, new Vector3f(x, y, z));
        this.renderRotatedQuad(vertexConsumer, quaternion, x, y, z, tickPercentage);
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
            WelkinRainParticle particle = new WelkinRainParticle(world, x, y, z);
            particle.setSprite(provider);
            return particle;
        }
    }

    @Override
    public void tick(){
        super.tick();
        if (this.onGround || this.removeIfObstructed()){
            this.markDead();
        }
    }

}
