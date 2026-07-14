package aster.welkin.block;

import aster.welkin.block.entity.ThunderheadBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ThunderheadBlock extends BlockWithEntity {
    public ThunderheadBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ThunderheadBlockEntity(pos, state);
    }
}
