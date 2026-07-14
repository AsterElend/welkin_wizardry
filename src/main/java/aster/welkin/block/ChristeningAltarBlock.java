package aster.welkin.block;

import aster.welkin.api.PedestalLikeBlock;
import aster.welkin.block.entity.ChristeningAltarBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ChristeningAltarBlock extends PedestalLikeBlock {
    public ChristeningAltarBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ChristeningAltarBlockEntity(pos, state);
    }



}
