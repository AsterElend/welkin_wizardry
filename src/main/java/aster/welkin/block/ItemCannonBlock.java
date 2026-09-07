package aster.welkin.block;

import aster.welkin.api.PedestalLikeBlock;
import aster.welkin.block.entity.AlchemyBlockEntity;
import aster.welkin.block.entity.ItemCannonBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ItemCannonBlock extends PedestalLikeBlock {
    public static final BooleanProperty POWERED = Properties.POWERED;

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient()) {
            boolean powered = world.isReceivingRedstonePower(pos);
            boolean wasPowered = state.get(POWERED);

            if (powered && !wasPowered && world.getBlockEntity(pos) instanceof ItemCannonBlockEntity cannon) {
                cannon.fire(world, pos);
            }

            if (powered != wasPowered) {
                world.setBlockState(pos, state.with(POWERED, powered));
            }
        }
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }




    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }



    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }


    public ItemCannonBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(POWERED, false));
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ItemCannonBlockEntity(pos, state);
    }
}
