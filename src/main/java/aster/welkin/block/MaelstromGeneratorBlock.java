package aster.welkin.block;

import aster.welkin.block.entity.MaelstromGeneratorBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MaelstromGeneratorBlock extends BlockWithEntity {
    public MaelstromGeneratorBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MaelstromGeneratorBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            World world,
            BlockState state,
            BlockEntityType<T> type
    ) {
        if (world.isClient) return null;

        return (w, pos, s, be) -> {
            if (be instanceof MaelstromGeneratorBlockEntity maelstrom) {
                maelstrom.tick(w);
            }
        };
    }

}
