package aster.welkin.api;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class WelkinUtil {
    public static BlockHitResult getTargetedBlock(PlayerEntity player, Boolean includeFluids) {
        double base;
        if (player.isCreative()){
            base = 6.0;
        } else {
            base = 4.5;
        }
        double interactionRange = ReachEntityAttributes.getReachDistance(player, base);
        HitResult hitResult = player.raycast(interactionRange, 0.0F, includeFluids);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return (BlockHitResult) hitResult;
        }

        return null;
    }
    public static List<BlockPos> getAdjacent(BlockPos pos){
        List<BlockPos> list = new ArrayList<>();
        list.add(pos.up());
        list.add(pos.down());
        list.add(pos.north());
        list.add(pos.south());
        list.add(pos.east());
        list.add(pos.west());
        return list;
    }

    public static ActionResult placeBlockWithoutItem(ItemUsageContext ctx, Block sigil) {
        World world = ctx.getWorld();
        PlayerEntity player = ctx.getPlayer();
        BlockPos pos = ctx.getBlockPos();
        Direction side = ctx.getSide();

        BlockPos placePos = pos.offset(side);
        BlockState placeState = world.getBlockState(placePos);

        if (player != null && placeState.isAir()) {
            if (!world.isClient) {
                ItemPlacementContext newContext = new ItemPlacementContext(ctx);
                world.setBlockState(placePos, sigil.getPlacementState(newContext));
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.SUCCESS;
    }

    //synchronization is a headache
    public static void yellAtEverything(BlockEntity be){
        be.markDirty();
        if (be.getWorld() != null && !be.getWorld().isClient) {
            be.getWorld().updateListeners(be.getPos(), be.getCachedState(), be.getCachedState(), 3);
        }
    }


}
