package aster.welkin.block.fancy.node;

import aster.welkin.block.ModBlockEntities;
import aster.welkin.block.fancy.ImplementedInventory;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class NodeBlockEntity extends BlockEntity implements ImplementedInventory {
    private final DefaultedList<ItemStack> inventory;

    public NodeBlockEntity(BlockPos pos, BlockState state){
        super(ModBlockEntities.NODE, pos, state);
this.inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    }

    @Override
    public DefaultedList<ItemStack> getItems(){
        return inventory;
    }

    @Override
    protected void writeNbt(NbtCompound nbt){
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
    }

    @Override
    public int getMaxCountPerStack()  {
        return 64;
    }

    @Override
    public void markDirty() {
        if(!world.isClient()) {
            PacketByteBuf data = PacketByteBufs.create();
            data.writeInt(inventory.size());
            for(int i = 0; i < inventory.size(); i++) {
                data.writeItemStack(inventory.get(i));
            }
            data.writeBlockPos(getPos());


        }
        super.markDirty();
    }
    public ItemStack getRenderStack(){
        return this.getStack(0);
    }
    public void setInventory(DefaultedList<ItemStack> list){
        for(int i = 0; i < list.size(); i++) {
            this.inventory.set(i, list.get(i));
        }
    }
    public boolean addItem(ItemStack itemStack) {
        if (isEmpty() && !itemStack.isEmpty()) {
            setStack(0, itemStack.split(1));
            return true;
        }
        return false;
    }
    public ItemStack removeItem() {
        if (!isEmpty()) {
            return getStoredItem().split(1);
        }
        return ItemStack.EMPTY;
    }

    public boolean isEmpty() {
        return getStack(0).isEmpty();
    }

    public ItemStack getStoredItem() {
        return getStack(0);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}