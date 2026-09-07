package aster.welkin.api;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public abstract class PedestalLikeBlockEntity extends BlockEntity implements ImplementedInventory {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);
    public void clearStack(){
        items.set(0, ItemStack.EMPTY);
        WelkinUtil.yellAtEverything(this);
    }

    public boolean isEmpty(){
       return getStack().isEmpty();
    }

    public ItemStack getStack(){
        return items.get(0);
    }

    public void setStack(ItemStack stack){
        items.set(0, stack);
        WelkinUtil.yellAtEverything(this);
    }
    public void setCount(int count){
        items.get(0).setCount(count);
        WelkinUtil.yellAtEverything(this);
    }
    public int getCount(){
        return items.get(0).getCount();
    }
    @Override
    public DefaultedList<ItemStack> getItems(){
        return items;
    }


    public ItemStack stackInteractionAttempt(ItemStack inputStack){
        ItemStack stack = inputStack;

        if (getStack().isEmpty() && stack.isEmpty()){
            return stack;
        }

        if (stack.isEmpty()){
            stack = getStack();

            clearStack();
            WelkinUtil.yellAtEverything(this);

            return stack;
        }

        if (getStack().isEmpty()){
            setStack(stack);
            stack = ItemStack.EMPTY;
            WelkinUtil.yellAtEverything(this);
            return stack;
        }

        if (getStack().getItem() == stack.getItem()){
            if (getStack().getCount() + stack.getCount() < getStack().getMaxCount()) {
                int newCount = getStack().getCount() + stack.getCount();
                setCount(newCount);
                stack = ItemStack.EMPTY;
                WelkinUtil.yellAtEverything(this);
                return stack;
            } else {
                int movingCount = getStack().getMaxCount() - getStack().getCount();
                stack.setCount(stack.getCount() - movingCount);
                setCount(getStack().getMaxCount());
                WelkinUtil.yellAtEverything(this);
                return stack;
            }
        }

        stack = swapStacks(stack);

        return stack;
    }

    public ItemStack swapStacks(ItemStack stack){
        ItemStack cachedStack = getStack();
        setStack(stack);
        WelkinUtil.yellAtEverything(this);
        return cachedStack;
    }
    
    public PedestalLikeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state){
        super(type, pos, state);


    }

    public void transmuteTo(Item item){
        int count = getCount();
        setStack(new ItemStack(item, count));
        WelkinUtil.yellAtEverything(this);
    }



    @Override
    public NbtCompound toInitialChunkDataNbt(){
        return createNbt();
    }

    @Override
    @Nullable
    public Packet<ClientPlayPacketListener> toUpdatePacket(){
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        Inventories.readNbt(nbt, items);
        readAdditionalData(nbt);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, items);
        storeAdditionalData(nbt);
    }


    void doSneakInteraction(PlayerEntity player, Hand hand) {};

    public void storeAdditionalData(NbtCompound nbt){

    }

    public void readAdditionalData(NbtCompound nbt){

    }
}
