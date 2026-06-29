package aster.welkin.block.entity;

import aster.welkin.block.ImplementedInventory;
import aster.welkin.recipes.AgoniteTransmutationRecipe;
import aster.welkin.registry.ModBlockEntities;
import aster.welkin.registry.ModRecipes;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class AgoniteTransmuterEntity extends PedestalRenderable implements ImplementedInventory {
    public float stockpiledPain = 0;
    private boolean triggered;
    private DefaultedList<ItemStack> inventory;

    public void addToStockpile(float toAdd){
        stockpiledPain += toAdd;
    }

   public void setTriggered(boolean bool){
        triggered = bool;
   }

    public boolean isTriggered(){
        return triggered;
    }
    public AgoniteTransmuterEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.AGONITE_ENTITY, pos, state);
        this.inventory  = DefaultedList.ofSize(1);
        this.triggered = false;

    }

    public void runRecipe(ServerWorld server, BlockPos pos){
        SimpleInventory inv = new SimpleInventory(1);
        inv.setStack(0, inventory.get(0));
        Optional<AgoniteTransmutationRecipe> match =
                server.getRecipeManager().getFirstMatch(
                        ModRecipes.AGONY_TYPE, inv, server
                );

        if (match.isEmpty()) return;

        AgoniteTransmutationRecipe recipe = match.get();
        if (stockpiledPain < recipe.getPain()) return;

        inventory.get(0).decrement(1);
        stockpiledPain = stockpiledPain - recipe.getPain();
        ItemStack output = recipe.getBaseOutput();
        BlockPos up = pos.up();
        ItemEntity toSummon = new ItemEntity(server, up.getX(), up.getY(), up.getZ(), output);
        toSummon.setVelocity(server.getRandom().nextFloat(), 2, server.getRandom().nextFloat());
        server.spawnEntity(toSummon);
        int count = (int) recipe.getPain();
        server.spawnParticles(ParticleTypes.HEART, pos.getX(), pos.getY(), pos.getZ(), count,
                server.getRandom().nextFloat(), server.getRandom().nextFloat(), server.getRandom().nextFloat(), 1f);

    }

    @Override
    public DefaultedList<ItemStack> getItems(){
        return inventory;
    }


    @Override
    protected void writeNbt(NbtCompound nbt){
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putBoolean("triggerstate", triggered);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        triggered = nbt.getBoolean("triggerstate");
    }

    @Override
    public int getMaxCountPerStack()  {
        return 1;
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

    public ItemStack getStack(){
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
            return getStack().split(1);
        }
        return ItemStack.EMPTY;
    }

    public boolean isEmpty() {
        return getStack(0).isEmpty();
    }


    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public ItemStack getRenderStack() {
        return inventory.get(0).copy();
    }
}
