package aster.welkin.block.entity;

import aster.welkin.api.PedestalLikeBlockEntity;
import aster.welkin.registry.WelkinBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class NodeBlockEntity extends PedestalLikeBlockEntity {

    public NodeBlockEntity(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.NODE, pos, state);
    }

    @Override
    public int getMaxCountPerStack() {
        return 1;
    }

}