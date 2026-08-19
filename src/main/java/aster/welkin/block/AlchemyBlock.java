package aster.welkin.block;

import aster.welkin.block.entity.AlchemyBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AlchemyBlock extends BlockWithEntity {

    public static final BooleanProperty POWERED = Properties.POWERED;
    public AlchemyBlock(FabricBlockSettings fabricBlockSettings) {
        super(fabricBlockSettings);
        setDefaultState(getStateManager().getDefaultState().with(POWERED, false));
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient()) {
            boolean powered = world.isReceivingRedstonePower(pos);
            boolean wasPowered = state.get(POWERED);

            if (powered && !wasPowered && world.getBlockEntity(pos) instanceof AlchemyBlockEntity phil) {
                phil.alchemicalInvocation(world, pos);
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


    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AlchemyBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state){
        return BlockRenderType.MODEL;
    }
}
