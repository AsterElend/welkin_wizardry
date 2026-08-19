package aster.welkin.block.entity;

import aster.welkin.api.PedestalLikeBlockEntity;
import aster.welkin.registry.WelkinBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class PylonBlockEntity extends PedestalLikeBlockEntity {


    public PylonBlockEntity(BlockPos pos, BlockState state){
        super(WelkinBlockEntities.PYLON, pos, state);
    }



}