package aster.welkin.api;

import aster.welkin.block.sigil.SigilBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class SigilHelper {
    public Set<Identifier> collectAdjacentSigils(World world, BlockPos pos){
        //only call this inside onNeighborUpdate and cache the result; it will be better for lag
        Set<Identifier> output = new HashSet<>();

        for (Direction direction: Direction.values()){
            BlockPos tempPos = pos.offset(direction);
            BlockEntity testEntity = world.getBlockEntity(tempPos);
            if (testEntity instanceof SigilBlockEntity){
                output.add(((SigilBlockEntity) testEntity).SIGIL_ID);
            }
        }
        return output;
    }
}
