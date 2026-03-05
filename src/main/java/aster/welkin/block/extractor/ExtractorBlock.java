package aster.welkin.block.extractor;

import aster.welkin.registry.ModBlockEntities;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ExtractorBlock extends BlockWithEntity {
    public static VoxelShape SHAPE = ExtractorBlock.createCuboidShape(4, 0, 4, 12, 6, 12);

    public ExtractorBlock(Settings settings){
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state){
        return new ExtractorBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context){
        return SHAPE;
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;

        ExtractorBlockEntity be = (ExtractorBlockEntity) world.getBlockEntity(pos);
        ItemStack held = player.getStackInHand(hand);

        if (be == null) return ActionResult.FAIL;

        // Retrieve item (empty hand)
        if (held.isEmpty() && !be.getItem().isEmpty()) {
            player.setStackInHand(hand, be.getItem());
            be.setItem(ItemStack.EMPTY);
            return ActionResult.SUCCESS;
        }

        // Insert item (block is empty)
        if (!held.isEmpty() && be.getItem().isEmpty()) {
            be.setItem(held.split(1));
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.EXTRACTOR, ExtractorBlockEntity::tick);
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
