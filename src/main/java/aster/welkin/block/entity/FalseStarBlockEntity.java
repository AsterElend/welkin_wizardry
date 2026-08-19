package aster.welkin.block.entity;

import aster.welkin.api.Linkable;
import aster.welkin.registry.WelkinBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FalseStarBlockEntity extends Linkable{
    private int cooldown = 0;
    private final int base_cooldown = 20;
    public FalseStarBlockEntity(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.FALSE_STAR, pos, state);
    }

    public void tick(BlockPos pos, World world){

    }
}
