package aster.welkin.item.fancy;

import aster.welkin.block.ModBlocks;
import aster.welkin.cc.MyComponents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;


public class GalvanicWand extends Item {

    public GalvanicWand(Settings settings) {
        super(settings);
    }

    @NotNull
    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        Direction direction = ctx.getSide();
        BlockState target = world.getBlockState(pos.offset(direction));
        PlayerEntity player = ctx.getPlayer();

        if (target.isAir()) {

            MyComponents.SKYFORCE.get(player).drainMagic(100, false);
            world.setBlockState(pos.offset(direction), ModBlocks.NODE.getDefaultState());
            return ActionResult.success(true);
        } else {
            return ActionResult.FAIL;
        }
    }





}
