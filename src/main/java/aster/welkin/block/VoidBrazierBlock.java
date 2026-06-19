package aster.welkin.block;

import aster.welkin.block.entity.VoidBrazierBlockEntity;
import aster.welkin.registry.ModBlockEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class VoidBrazierBlock extends BlockWithEntity {
    public static final BooleanProperty LIT = Properties.LIT;
    private static final VoxelShape SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);



    public VoidBrazierBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LIT, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new VoidBrazierBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            World world,
            BlockState state,
            BlockEntityType<T> type
    ) {
        if (world.isClient) return null;
        return type == ModBlockEntities.VOID_BRAZIER_ENTITY
                ? (w,p,s,be) -> VoidBrazierBlockEntity.tick(w,p,s, (VoidBrazierBlockEntity) be) : null;


    }

    @Override
    public ActionResult onUse(
            BlockState state, World world, BlockPos pos,
            PlayerEntity player, Hand hand, BlockHitResult hit
    ) {
        ItemStack held = player.getStackInHand(hand);

        // Light it
        if (held.isOf(Items.FLINT_AND_STEEL) && !state.get(LIT)) {
            world.setBlockState(pos, state.with(LIT, true));

            world.playSound(null, pos,
                    SoundEvents.ITEM_FLINTANDSTEEL_USE,
                    SoundCategory.BLOCKS,
                    1.0f, 1.0f);

            return ActionResult.SUCCESS;
        }

        // Extinguish with empty hand
        if (held.isEmpty() && state.get(LIT)) {
            world.setBlockState(pos, state.with(LIT, false));

            world.playSound(null, pos,
                    SoundEvents.BLOCK_FIRE_EXTINGUISH,
                    SoundCategory.BLOCKS,
                    1.0f, 1.0f);

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

}
