package aster.welkin.block;

import aster.welkin.block.entity.AetherTransducerBlockEntity;
import aster.welkin.block.transducer.TransducerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AetherTransducerBlock extends TransducerBlock {

    public AetherTransducerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AetherTransducerBlockEntity(pos, state);

    }




    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            World world,
            BlockState state,
            BlockEntityType<T> type
    ) {
        if (world.isClient) return null;

        return (w, pos, s, be) -> {
            if (be instanceof AetherTransducerBlockEntity laser) {
                laser.tick(world, pos, state);
            }
        };
    }

}
