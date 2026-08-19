package aster.welkin.item.baton;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class TunnelerBatonItem extends Item {
    public TunnelerBatonItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();

        // Execute only on the server side to handle teleportation safely
        if (!world.isClient) {
            PlayerEntity player = context.getPlayer();
            if (player == null) return ActionResult.PASS;

            // 1. Get the clicked block position and the face that was hit
            BlockPos hitPos = context.getBlockPos();
            Direction side = context.getSide();

            // 2. The search direction is the opposite of the clicked face (pushing "into" the wall)
            Direction searchDir = side.getOpposite();

            BlockPos targetPos = null;
            boolean solidWallFound = false;

            // 3. Scan up to 8 blocks into the wall
            for (int i = 1; i <= 8; i++) {
                BlockPos currentCheck = hitPos.offset(searchDir, i);

                if (!solidWallFound) {
                    // Check if we are still inside the solid wall
                    if (world.getBlockState(currentCheck).isOpaqueFullCube(world, currentCheck)) {
                        solidWallFound = true;
                    }
                } else {
                    // We entered a wall and are now looking for a 1x2x1 safe air clearance
                    if (isSafeLocation(world, currentCheck)) {
                        targetPos = currentCheck;
                        break;
                    }
                }
            }

            // 4. Teleport if a valid spot was found
            if (targetPos != null) {
                // Align player to the center of the block to prevent clipping into edges
                double tpX = targetPos.getX() + 0.5;
                double tpY = targetPos.getY();
                double tpZ = targetPos.getZ() + 0.5;

                // Play audio at the old location
                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);

                // Teleport the player entity
                player.teleport(tpX, tpY, tpZ);

                // Play audio at the new location
                world.playSound(null, tpX, tpY, tpZ,
                        SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);

                return ActionResult.SUCCESS;
            } else {
                player.sendMessage(Text.literal("§cNo safe spot found on the other side!"), true);
                return ActionResult.FAIL;
            }
        }

        return ActionResult.SUCCESS;
    }

    private boolean isSafeLocation(World world, BlockPos pos) {
        BlockState feet = world.getBlockState(pos);
        BlockState head = world.getBlockState(pos.up());
        BlockState floor = world.getBlockState(pos.down());

        // Feet and head spaces must not suffocate the player, floor must be solid or standable
        return !feet.isOpaqueFullCube(world, pos) &&
                !head.isOpaqueFullCube(world, pos.up()) &&
                !floor.isAir();
    }
}
