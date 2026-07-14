package aster.welkin.item.baton;

import aster.welkin.api.WelkinUtil;
import aster.welkin.registry.WelkinItems;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import com.unascribed.lib39.recoil.api.DirectClickItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BricklayerBatonItem extends Item implements DirectClickItem {


    public BricklayerBatonItem(Settings settings) {
        super(settings);
    }

    @NotNull
    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
       if (ctx.getWorld().isClient) return ActionResult.SUCCESS;
       return ActionResult.PASS;
    }
    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return false;
    }



    @Override
    public ActionResult onDirectAttack(PlayerEntity player, Hand hand) {
        if (player.getWorld().isClient) return ActionResult.SUCCESS;
        if (player.getItemCooldownManager().isCoolingDown(WelkinItems.BRICKLAYER_BATON)){
            return ActionResult.FAIL;
        }
        BlockHitResult hit = WelkinUtil.getTargetedBlock(player, false);
        if (hit == null) return ActionResult.FAIL;

        Item block = player.getWorld().getBlockState(hit.getBlockPos()).getBlock().asItem();
        String id = Registries.ITEM.getId(block).toString();
        NbtCompound nbt = player.getStackInHand(hand).getOrCreateNbt();

        NbtList nbtlist = nbt.getList("stored_values", NbtElement.STRING_TYPE);
        List<String> currentValues = new ArrayList<>();
        for (NbtElement element: nbtlist){
            currentValues.add(element.asString());
        }
         if (player.isSneaking() && currentValues.contains(id)){
            currentValues.remove(id);
        } else {
            currentValues.add(id);
        }


        NbtCompound newNbt = new NbtCompound();
        NbtList list = new NbtList();
        for (String string: currentValues){
            list.add(NbtString.of(string));
        }
        newNbt.put("stored_values", list);
        player.getStackInHand(hand).setNbt(newNbt);
        player.getItemCooldownManager().set(WelkinItems.BRICKLAYER_BATON, 5);
        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult onDirectUse(PlayerEntity player, Hand hand) {
        World world = player.getWorld();
        if (world.isClient) return ActionResult.SUCCESS;
        if (player.getItemCooldownManager().isCoolingDown(WelkinItems.BRICKLAYER_BATON)){
            return ActionResult.FAIL;
        }
        if (player.isSneaking()){
            player.getStackInHand(hand).setNbt(new NbtCompound());
            player.getItemCooldownManager().set(WelkinItems.BRICKLAYER_BATON, 5);
            return ActionResult.SUCCESS;
        }


        BlockHitResult hit = WelkinUtil.getTargetedBlock(player, false);

        if (hit == null) return ActionResult.FAIL;
        NbtCompound nbt = player.getStackInHand(hand).getOrCreateNbt();
        NbtList nbtlist = nbt.getList("stored_values", NbtElement.STRING_TYPE);
        List<Item> currentValues = new ArrayList<>();
        for (NbtElement element: nbtlist){
            Item temp = Registries.ITEM.get(new Identifier(element.asString()));
            currentValues.add(temp);
        }


        if (currentValues.isEmpty()){
            return ActionResult.FAIL;
        }

        Item operant = currentValues.get(world.getRandom().nextInt(currentValues.size()));

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

        ItemPlacementContext placeContext =
                new ItemPlacementContext(player, hand, placeStack, hit);

        ActionResult result =
                ((BlockItem) operant).place(placeContext);

        if (result.isAccepted() && !player.isCreative()) {
            invStack.decrement(1);
        }
        player.getItemCooldownManager().set(WelkinItems.BRICKLAYER_BATON, 5);
        return ActionResult.SUCCESS;
    }


}
