package aster.welkin.block;

import aster.welkin.api.PedestalLikeBlock;
import aster.welkin.block.entity.AgoniteTransmuterEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AgoniteTransmuterBlock extends PedestalLikeBlock {

    public static final BooleanProperty TRIGGERED = Properties.TRIGGERED;

    public AgoniteTransmuterBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AgoniteTransmuterEntity(pos, state);
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TRIGGERED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(TRIGGERED, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
    }


    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        var isTriggered = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up());
        boolean wasTriggered = state.get(TRIGGERED);

        if (isTriggered && !wasTriggered) {
            if (!world.isClient) {
               if (world.getBlockEntity(pos) instanceof AgoniteTransmuterEntity transmuter){
                   transmuter.runRecipe();
               }
            }
            world.setBlockState(pos, state.with(TRIGGERED, true), Block.NO_REDRAW);
        } else if (!isTriggered && wasTriggered) {
            world.setBlockState(pos, state.with(TRIGGERED, false), Block.NO_REDRAW);
        }
    }
}
