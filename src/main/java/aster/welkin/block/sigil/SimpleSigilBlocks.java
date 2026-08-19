package aster.welkin.block.sigil;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SimpleSigilBlocks {
    public static class TenpoSigilBlock extends SigilBlock {
        public TenpoSigilBlock(Settings settings) {
            super(settings);
        }

        @Override
        public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
            return new aster.welkin.block.sigil.TenpoSigil(pos, state);
        }
        @Override
        public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
                World world,
                BlockState state,
                BlockEntityType<T> type
        ) {
            if (world.isClient) return null;

            return (w, pos, s, be) -> {
                if (be instanceof TenpoSigil sigil) {
                    sigil.tick(pos, (ServerWorld) w);
                }
            };
        }

    }


}
