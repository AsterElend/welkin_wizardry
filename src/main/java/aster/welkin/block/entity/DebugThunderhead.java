package aster.welkin.block.entity;

import aster.welkin.api.AetherHolder;
import aster.welkin.api.IHasLensInfo;
import aster.welkin.registry.WelkinBlockEntities;
import aster.welkin.registry.WelkinItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DebugThunderhead extends AetherHolder implements IHasLensInfo {
    public DebugThunderhead(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.CREATIVE_THUNDERHEAD, pos, state);
    }

    @Override
    public boolean hasSufficientAether(int test){
        return true;
    }

    @Override
    public void applyLensOverlay(List<Pair<ItemStack, StringVisitable>> lines,
                                 BlockState state, BlockPos pos,
                                 PlayerEntity observer, World world, Direction hitFace){
        if (world.getBlockEntity(pos) instanceof DebugThunderhead) {
            lines.add(new Pair<>(new ItemStack(WelkinItems.CHARGESTONE), Text.translatable("welkin.scry.infinite")
                    .setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.BLUE)).withBold(true))));
        }
    }

    public static class Block extends BlockWithEntity {

        public Block(Settings settings) {
            super(settings);
        }

        @Override
        public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
            return new DebugThunderhead(pos, state);
        }
    }
}
