package aster.welkin.registry.particle;

import aster.welkin.Welkin;
import aster.welkin.WelkinClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.joml.Vector3f;

//why are particles so haaaaard
//thank you particlerain for actually knowing what you're doing
public abstract class WeatherParticle extends SpriteBillboardParticle {

    protected BlockPos.Mutable pos;
    boolean shouldFade = false;
    float temperature;

    protected WeatherParticle(ClientWorld world, double x, double y, double z){
        super(world, x, y, z);
        this.scale = 0.01F;
        this.maxAge = Welkin.CONFIG.particleRadius * 10;
        this.alpha = 0.0F;
        this.pos = new BlockPos.Mutable(x, y, z);
        this.temperature = world.getBiome(this.pos).value().getTemperature();
        WelkinClient.particleCount++;
    }

    @Override
    public void tick(){
        super.tick();
        this.pos.set(this.x, this.y - 0.2, this.z);
        this.removeIfOOB();
        if (shouldFade){
            fadeOut();
        } else if (this.age % 10 == 0){
            if (Math.abs(world.getBiome(this.pos).value().getTemperature() - this.temperature) > 0.4) shouldFade = true;
        } else {
            fadeIn();
        }
    }


    void removeIfOOB() {
        Entity cameraEntity = MinecraftClient.getInstance().getCameraEntity();
        if (cameraEntity == null || cameraEntity.squaredDistanceTo(this.x, this.y, this.z) > MathHelper.square(Welkin.CONFIG.particleRadius)) {
            shouldFade = true;
        }

    }
    public void fadeIn() {
        if (age < 20) {
            this.alpha = (age * 1.0f) / 20;
        }
    }

    public void fadeOut() {
        if (this.alpha < 0.01) {
            markDead();
        } else {
            this.alpha = this.alpha - 0.05F;
        }
    }

    @Override
    public void markDead() {
        if (this.isAlive()) WelkinClient.particleCount--;
        super.markDead();
    }

    protected boolean removeIfObstructed() {
        if (x == prevPosX || z == prevPosZ) {
            this.markDead();
            return true;
        } else {
            return false;
        }
    }

    public static double yLevelWindAdjustment(double y) {
        return MathHelper.clamp(0.01, 1, (y - 64) / 40);
    }

    //wow, no wonder it wasn't working before
    public Quaternionf flipItTurnwaysIfBackfaced(Quaternionf quaternion, Vector3f toCamera) {
        Vector3f normal = new Vector3f(0, 0, 1);
        normal.rotate(quaternion).normalize();
        float dot = normal.dot(toCamera);
        if (dot > 0) {
            return quaternion.mul(RotationAxis.POSITIVE_Y.rotation(MathHelper.PI));
        }
        else return quaternion;
    }
    public void renderRotatedQuad(VertexConsumer vertexConsumer, Quaternionf quaternion, float x, float y, float z, float tickPercentage) {
        quaternion.rotateY(MathHelper.PI);
        float quadSize = this.getSize(tickPercentage);
        float u0 = this.getMinU();
        float u1 = this.getMaxU();
        float v0 = this.getMinV();
        float v1 = this.getMaxV();
        int lightColor = this.getBrightness(tickPercentage);

        Vector3f[] vector3fs = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)};

        for(int k = 0; k < 4; ++k) {
            Vector3f vector3f = vector3fs[k];
            vector3f.rotate(quaternion);
            vector3f.mul(quadSize);
            vector3f.add(x, y, z);
        }
        vertexConsumer.vertex((double)vector3fs[0].x(), (double)vector3fs[0].y(), (double)vector3fs[0].z()).texture(u1, v1).color(this.red, this.green, this.blue, this.alpha).light(lightColor).next();
        vertexConsumer.vertex((double)vector3fs[1].x(), (double)vector3fs[1].y(), (double)vector3fs[1].z()).texture(u1, v0).color(this.red, this.green, this.blue, this.alpha).light(lightColor).next();
        vertexConsumer.vertex((double)vector3fs[2].x(), (double)vector3fs[2].y(), (double)vector3fs[2].z()).texture(u0, v0).color(this.red, this.green, this.blue, this.alpha).light(lightColor).next();
        vertexConsumer.vertex((double)vector3fs[3].x(), (double)vector3fs[3].y(), (double)vector3fs[3].z()).texture(u0, v1).color(this.red, this.green, this.blue, this.alpha).light(lightColor).next();
    }
}
