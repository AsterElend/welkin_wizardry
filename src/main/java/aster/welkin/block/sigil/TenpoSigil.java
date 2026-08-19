package aster.welkin.block.sigil;

import aster.welkin.Welkin;
import aster.welkin.registry.WelkinBlockEntities;
import aster.welkin.registry.WelkinTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
@SuppressWarnings("unchecked")
public class TenpoSigil extends Sigil{
    public TenpoSigil(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.TENPO_SIGIL, pos, state);
    }

    public void tick(BlockPos pos, ServerWorld world){
        if (world.isClient) return;
        Direction direction = world.getBlockState(pos).get(Properties.FACING);
        BlockPos activePos = pos.offset(direction);
        BlockState activeState = world.getBlockState(activePos);
        BlockEntity activeEntity = world.getBlockEntity(activePos);
        if (activeEntity == null || activeState.isIn(WelkinTags.UNACCELERATEABLE) || (activeState.getBlock() instanceof SigilBlock)) return;
        //this cast is obviously safe, why are you worried
        var ticker = activeEntity.getCachedState().getBlockEntityTicker(world,
                (BlockEntityType<BlockEntity>) activeEntity.getType());
        if (ticker == null) return;
        ticker.tick(world, activePos, activeState, activeEntity);
    }

    @Override
    public Identifier getTexture() {
        return Welkin.id("block/sigil/tenpo");
    }
}
