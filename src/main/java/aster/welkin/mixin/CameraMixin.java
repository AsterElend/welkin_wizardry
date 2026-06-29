package aster.welkin.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow protected abstract void setPos(Vec3d pos);
    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Unique
    public static boolean isCutsceneActive = false;
    @Unique public static float orbitAngle = 0.0f;
    @Unique public static double orbitRadius = 4.0; // Distance from player
    @Unique public static double heightOffset = 2.5; // Set it above the player

    @Inject(method = "update", at = @At("TAIL"))
    private void overrideCamera(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (isCutsceneActive && focusedEntity != null) {
            // 1. Get the target player's exact center position
            Vec3d playerPos = focusedEntity.getLerpedPos(tickDelta);
            double playerMidY = playerPos.y + (focusedEntity.getStandingEyeHeight() / 2.0);

            // 2. Calculate the orbiting camera position (Above and to the side)
            double radians = Math.toRadians(orbitAngle);
            double camX = playerPos.x + (orbitRadius * Math.cos(radians));
            double camZ = playerPos.z + (orbitRadius * Math.sin(radians));
            double camY = playerPos.y + heightOffset;

            Vec3d newCamPos = new Vec3d(camX, camY, camZ);
            this.setPos(newCamPos);

            // 3. Calculate rotation angles to point directly back at the player's body
            double diffX = playerPos.x - camX;
            double diffY = playerMidY - camY;
            double diffZ = playerPos.z - camZ;

            double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

            // Math.atan2 returns radians, convert to Minecraft degrees
            float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
            float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));

            this.setRotation(yaw, pitch);
        }
    }
}
