package aster.welkin.block.sigil;

import aster.welkin.registry.WelkinTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

/**
 * look, the only reason I had to do this was because you can't randomly tick a block entity.
 */

public class SuliSigilBlock extends SigilBlock{

    public SuliSigilBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasRandomTicks(BlockState state){
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // Ensure the code only runs on the logical server to avoid crashes
        if (!world.isClient()) {
         Direction activeDirection = state.get(FACING);
         BlockPos activePos = pos.offset(activeDirection);
         BlockState activeState = world.getBlockState(activePos);
         if (state.isIn(WelkinTags.UNACCELERATEABLE) || !activeState.hasRandomTicks() || (activeState.getBlock() instanceof SigilBlock)) return;
         activeState.randomTick(world, activePos, random);
        }
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SuliSigil(pos, state);
    }
}
