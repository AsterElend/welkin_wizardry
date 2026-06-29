package aster.welkin.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class PedestalRenderable extends BlockEntity {
    public PedestalRenderable(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public ItemStack getRenderStack() {
        return null;
    }
}
