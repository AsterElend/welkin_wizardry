package aster.welkin.block;

import aster.welkin.block.entity.AgoniteTransmuterEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AgoniteTransmuterBlock extends BlockWithEntity {
    public AgoniteTransmuterBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AgoniteTransmuterEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ActionResult result = ActionResult.PASS;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof AgoniteTransmuterEntity nodeBlockEntity)) {
            return result;
        }

        if (nodeBlockEntity.isEmpty()) {
            result = addItemFromHand(world, nodeBlockEntity, player, hand);
        } else if (hand.equals(Hand.MAIN_HAND)) {
            removeItemFromTable(world, nodeBlockEntity, player);
            result = ActionResult.SUCCESS;
        }

        return result;
    }

    private ActionResult addItemFromHand(World world, AgoniteTransmuterEntity nodeBlockEntity, PlayerEntity player, Hand hand) {
        ItemStack heldItem = player.getStackInHand(hand);
        ItemStack offHandItem = player.getOffHandStack();

        if (!offHandItem.isEmpty()) {
            if (hand.equals(Hand.MAIN_HAND) && !(heldItem.getItem() instanceof BlockItem)) {
                return ActionResult.PASS;
            }
            if (hand.equals(Hand.OFF_HAND)) {
                return ActionResult.PASS;
            }
        }
        if (heldItem.isEmpty()) {
            return ActionResult.PASS;
        } else if (nodeBlockEntity.addItem(player.getAbilities().creativeMode ? heldItem.copy() : heldItem)) {
            playPlaceSound(world, nodeBlockEntity.getPos());
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    private void removeItemFromTable(World world, AgoniteTransmuterEntity nodeBlockEntity, PlayerEntity player) {
        BlockPos pos = nodeBlockEntity.getPos();
        if (player.isCreative()) {
            nodeBlockEntity.removeItem();
        } else if (!player.getInventory().insertStack(nodeBlockEntity.removeItem())) {
            ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), nodeBlockEntity.removeItem());
        }
        playRemoveSound(world, pos);
    }

    private void playPlaceSound(World world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, SoundCategory.BLOCKS, 1f, 1f);
    }

    private void playRemoveSound(World world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 1f);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean bl = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up());
        if (world.isClient) return;
       BlockEntity entity =  world.getBlockEntity(pos);
       if (!(entity instanceof AgoniteTransmuterEntity transmuter)) return;
       boolean bl2 = transmuter.isTriggered();
        if (bl && !bl2) {
            transmuter.runRecipe((ServerWorld) world, pos);
            transmuter.setTriggered(true);
        } else if (!bl && bl2) {
            transmuter.setTriggered(false);
        }

    }
}
