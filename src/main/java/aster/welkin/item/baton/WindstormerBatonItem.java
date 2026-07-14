package aster.welkin.item.baton;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WindstormerBatonItem extends Item {


    public WindstormerBatonItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand){
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @NotNull
    @Override
    public UseAction getUseAction(ItemStack stack) { return UseAction.BOW; }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks){
        if (world.isClient || !(user instanceof  PlayerEntity player)) return;



        double range = 6.0;        // how far the cone reaches
        double force = 1.1;        // push strength
        double coneAngle = Math.toRadians(35); // half-angle of the cone

        Vec3d eyePos = user.getEyePos();
        Vec3d look = user.getRotationVec(1.0F).normalize();

        // Check entities in a big sphere around the player
        Box box = new Box(eyePos, eyePos).expand(range);
        List<Entity> entities = world.getOtherEntities(user, box);

        for (Entity e : entities) {
            if (!(e instanceof LivingEntity)) continue;

            // Vector from player → entity
            Vec3d toEntity = e.getPos().add(0, e.getHeight() * 0.5, 0).subtract(eyePos);
            double dist = toEntity.length();
            if (dist > range) continue;

            Vec3d dir = toEntity.normalize();

            // Compute angle between look direction and target
            double angle = Math.acos(look.dotProduct(dir));

            // Skip things outside the cone
            if (angle > coneAngle) continue;

            // Apply outward velocity
            Vec3d push = dir.multiply(force);
            e.addVelocity(push.x, push.y * 0.4, push.z);
            e.velocityModified = true;
        }



    }




}
