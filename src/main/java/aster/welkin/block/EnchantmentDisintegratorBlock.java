package aster.welkin.block;

import aster.welkin.api.IHasLensInfo;
import aster.welkin.api.PedestalLikeBlock;
import aster.welkin.block.entity.EnchantmentDisintegratorBlockEntity;
import aster.welkin.registry.WelkinBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EnchantmentDisintegratorBlock extends PedestalLikeBlock implements IHasLensInfo {
    public EnchantmentDisintegratorBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EnchantmentDisintegratorBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient
                ? null
                : checkType(type, WelkinBlockEntities.ENCHANTMENT_DISINTEGRATOR,
                (w, pos, s, be) -> {
                    be.tick(pos, world);
                });
    }
}
