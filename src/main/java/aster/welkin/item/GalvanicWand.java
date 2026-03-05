package aster.welkin.item;

import aster.welkin.registry.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
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
        PlayerEntity player = ctx.getPlayer();
        BlockPos pos = ctx.getBlockPos();
        Direction side = ctx.getSide();


        BlockPos placePos = pos.offset(side);
        BlockState placeState = world.getBlockState(placePos);

        if (!world.isClient && player != null && player.isSneaking() && placeState.canReplace((ItemPlacementContext) ctx) ) {


                world.setBlockState(placePos, ModBlocks.NODE.getDefaultState());
                return ActionResult.SUCCESS;



        } return ActionResult.FAIL;
    }

}

