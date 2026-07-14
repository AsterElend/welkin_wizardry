package aster.welkin.block.transducer;

import aster.welkin.registry.WelkinBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FluidTransducerBlock extends TransducerBlock {
    public FluidTransducerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FluidTransducerEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient
                ? null
                : checkType(type, WelkinBlockEntities.FLUID_TRANSDUCER,
                (w, pos, s, be) -> {
                    be.serverTick(w, pos, s);
                });
    }
}
