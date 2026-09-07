package aster.welkin.registry.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;

public class FallingParticle extends SpriteBillboardParticle {
    private static final float HORIZONTAL_DAMPING = 0.985f;

    protected FallingParticle(ClientWorld world, double x, double y, double z,
                              double velX, double velY, double velZ, SpriteProvider sprites) {
        super(world, x, y, z, velX, velY, velZ);
        this.velocityX = velX;
        this.velocityY = velY;
        this.velocityZ = velZ;
        this.gravityStrength = 0.0f;
        this.setSpriteForAge(sprites);

        int groundY = world.getTopY(Heightmap.Type.MOTION_BLOCKING, MathHelper.floor(x), MathHelper.floor(z));
        double fallDistance = Math.max(0, y - groundY);
        double speed = Math.max(0.05, Math.abs(velY));
        this.maxAge = (int) MathHelper.clamp(fallDistance / speed + 5, 20, 300) + this.random.nextInt(10);
    }

    @Override
    public void tick() {
        super.tick();
        this.velocityX *= HORIZONTAL_DAMPING;
        this.velocityZ *= HORIZONTAL_DAMPING;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;
        public Factory(SpriteProvider sprites) { this.sprites = sprites; }

        @Override
        public Particle createParticle(DefaultParticleType type, ClientWorld world,
                                       double x, double y, double z, double velX, double velY, double velZ) {
            return new FallingParticle(world, x, y, z, velX, velY, velZ, sprites);
        }
    }
}