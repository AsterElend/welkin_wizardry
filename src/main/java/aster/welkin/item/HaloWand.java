package aster.welkin.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HaloWand extends Item {


    public HaloWand(Settings settings) {
        super(settings);
    }

    @NotNull
    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        World world = ctx.getWorld();
        if (world.isClient) return ActionResult.PASS;

        NbtCompound nbt = ctx.getStack().getOrCreateNbt();
        List<Item> currentValues = new ArrayList<>();
        for (String string: nbt.getKeys()){
            Item temp = Registries.BLOCK.get(new Identifier(string)).asItem();
            currentValues.add(temp);
        }

        if (currentValues.isEmpty()){
            return ActionResult.FAIL;
        }
        Item operant = currentValues.get(world.getRandom().nextInt(currentValues.size()));

        PlayerEntity player = ctx.getPlayer();

        PlayerInventory inv = player.getInventory();
        int slot = -1;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (!stack.isEmpty() && stack.isOf(operant)) {
                slot = i;
                break;
            }
        }

        if (slot < 0) {
         return ActionResult.FAIL;
        }

        ItemStack invStack = inv.getStack(slot);
        ItemStack placeStack = invStack.copyWithCount(1);


        BlockHitResult hit = new BlockHitResult(ctx.getHitPos(), ctx.getSide(), ctx.getBlockPos(), false);
        ItemPlacementContext placeContext =
                new ItemPlacementContext(player, Hand.MAIN_HAND, placeStack, hit);

        ActionResult result =
                ((BlockItem) operant).place(placeContext);

        if (result.isAccepted() && !player.isCreative()) {
            invStack.decrement(1);
        }

        return ActionResult.SUCCESS;
    }

    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        if (!world.isClient) {
            this.doLeftClickFunction(miner, state, miner.getStackInHand(Hand.MAIN_HAND));
        }

        return false;
    }


    private void doLeftClickFunction(PlayerEntity player, BlockState state, ItemStack stack) {

        Item block = state.getBlock().asItem();
        NbtCompound nbt = stack.getOrCreateNbt();
        List<String> currentValues = new java.util.ArrayList<>(nbt.getKeys().stream().toList());
        if (player.isSneaking()){
            currentValues.clear();
        } else if (currentValues.contains(block.toString())){
            currentValues.remove(block.toString());
        } else {
            currentValues.add(block.toString());
        }


        NbtCompound newNbt = new NbtCompound();

        for (int i = 0; i < currentValues.size() && i < 16; i++){
            newNbt.putString("savedBlock" + i, currentValues.get(i));
        }
        stack.writeNbt(newNbt);





    }



}
