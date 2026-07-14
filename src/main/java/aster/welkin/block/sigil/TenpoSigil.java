package aster.welkin.block.sigil;

import aster.welkin.registry.WelkinBlockEntities;
import aster.welkin.registry.WelkinTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class TenpoSigil extends SigilBlockEntity{
    public TenpoSigil(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.TENPO_SIGIL, pos, state);
    }

    public static void tick(BlockPos pos, ServerWorld world){
        Direction direction = world.getBlockState(pos).get(Properties.FACING);
        BlockPos activePos = pos.offset(direction);
        BlockState activeState = world.getBlockState(activePos);
        BlockEntity activeEntity = world.getBlockEntity(activePos);
        if (activeEntity == null || activeState.isIn(WelkinTags.UNACCELERATEABLE)) return;

        var ticker = activeEntity.getCachedState().getBlockEntityTicker(world,
                (BlockEntityType<BlockEntity>) activeEntity.getType());
            ticker.tick(world, activePos, activeState, activeEntity);

    }
}
