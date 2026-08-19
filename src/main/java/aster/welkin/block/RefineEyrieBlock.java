package aster.welkin.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class RefineEyrieBlock extends Block {

    public RefineEyrieBlock(Settings settings) {
        super(settings);
    }


    public static class Controller extends BlockWithEntity{
        protected Controller(Settings settings) {
            super(settings);
        }

        @Override
        public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
            return null;
        }
    }
}
