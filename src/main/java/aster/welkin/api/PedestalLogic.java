package aster.welkin.api;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;

public class PedestalLogic {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private final BlockEntity owner; // for markDirty/world access

    public PedestalLogic(BlockEntity owner) {
        this.owner = owner;
    }


    public void clearStack(){
        items.set(0, ItemStack.EMPTY);
       WelkinUtil.yellAtEverything(owner);
    }

    public boolean isEmpty(){
        return items.isEmpty();
    }

    public ItemStack getStack(){
        return items.get(0);
    }

    public void setStack(ItemStack stack){
        items.set(0, stack);
        WelkinUtil.yellAtEverything(owner);
    }
    public void setCount(int count){
        items.get(0).setCount(count);
        WelkinUtil.yellAtEverything(owner);
    }
    public int getCount(){
        return items.get(0).getCount();
    }
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
            WelkinUtil.yellAtEverything(owner);

            return stack;
        }

        if (getStack().isEmpty()){
            setStack(stack);
            stack = ItemStack.EMPTY;
            WelkinUtil.yellAtEverything(owner);
            return stack;
        }

        if (getStack().getItem() == stack.getItem()){
            if (getStack().getCount() + stack.getCount() < getStack().getMaxCount()) {
                int newCount = getStack().getCount() + stack.getCount();
                setCount(newCount);
                stack = ItemStack.EMPTY;
                WelkinUtil.yellAtEverything(owner);
                return stack;
            } else {
                int movingCount = getStack().getMaxCount() - getStack().getCount();
                stack.setCount(stack.getCount() - movingCount);
                setCount(getStack().getMaxCount());
                WelkinUtil.yellAtEverything(owner);
                return stack;
            }
        }

        stack = swapStacks(stack);

        return stack;
    }

    public ItemStack swapStacks(ItemStack stack){
        ItemStack cachedStack = getStack();
        setStack(stack);
       WelkinUtil.yellAtEverything(owner);
        return cachedStack;
    }

    public void doSneakInteraction(PlayerEntity player, Hand hand){};










}