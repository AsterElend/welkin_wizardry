package aster.welkin.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class WardstoneItem extends Item {
    public WardstoneItem(Settings settings) {
        super(settings);
    }
    //Lovingly borrowed from botania's Stone of Temperance
    //https://github.com/Vazkii/Botania



    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand){
        ItemStack stack = player.getStackInHand(hand);
        toggleActive(stack, player, world);
        return TypedActionResult.success(stack, world.isClient);
    }

    public static boolean hasParticularWardstoneActive(PlayerEntity player, WardstoneItem searchFor){
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (!stack.isEmpty() && stack.isOf(searchFor)
                    && stack.hasNbt() && stack.getOrCreateNbt().contains("active")
            && stack.getOrCreateNbt().getBoolean("active")) {
                return true;
            }
        }

        return false;
    }

@Override
public boolean onClicked(ItemStack stack, ItemStack cursor, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
    World world = player.getWorld();
    if (clickType == ClickType.RIGHT && slot.canTakeItems(player) && cursor.isEmpty()) {
        toggleActive(stack, player, world);
        cursorStackReference.set(cursor);
        return true;
    }
    return false;
}

    private void toggleActive(ItemStack stack, PlayerEntity player, World world) {
        NbtCompound nbt = new NbtCompound();
        boolean active = true;
        if (stack.getOrCreateNbt().contains("active")){
             active = stack.getOrCreateNbt().getBoolean("active");
        }

        nbt.putBoolean("active", !active);
        stack.setNbt(nbt);
    }


}
