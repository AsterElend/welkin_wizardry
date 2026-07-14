package aster.welkin.block.entity;

import aster.welkin.registry.WelkinBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class ThunderheadBlockEntity extends BlockEntity {
    private int aether;
    public ThunderheadBlockEntity(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.THUNDERHEAD, pos, state);
        aether = 0;
    }

    public boolean hasSufficientAether(int test){
        return test <= aether;
    }

    public void admixAether(int delta){
        aether += delta;
    }


}
