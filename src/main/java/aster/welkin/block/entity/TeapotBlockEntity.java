package aster.welkin.block.entity;

import aster.welkin.block.ImplementedInventory;
import aster.welkin.registry.ModBlockEntities;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
public class TeapotBlockEntity extends BlockEntity implements ImplementedInventory {

    private final DefaultedList<ItemStack> outputInventory;
    private final DefaultedList<ItemStack> inputInventory;


    public static final long CAPACITY = FluidConstants.BUCKET;

    // Exposed storage view for Fabric's fluid API
    private final SingleVariantStorage<FluidVariant> inputStorage = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return CAPACITY;
        }

        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };


    private final SingleVariantStorage<FluidVariant> outputStorage = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return CAPACITY;
        }

        @Override
        protected void onFinalCommit() {
            markDirty();

        }
    };


    public TeapotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TEAPOT_ENTITY, pos, state);
        this.inputInventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
        this.outputInventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    }

    // --- Fluid accessor methods ---

    public SingleVariantStorage<FluidVariant> getInputStorage() {
        return inputStorage;
    }

    public FluidVariant getInputStoredFluid() {
        return inputStorage.variant;
    }

    public long getInputFluidAmount() {
        return inputStorage.amount;
    }

    public ItemStack getInputStack(){
        return inputInventory.get(0);
    }

    public ItemStack getOutputStack(){
        return outputInventory.get(0);
    }

    public void setInputStack(ItemStack newStack){
        inputInventory.set(0, newStack);
    }

    public void setOutputStack(ItemStack newStack){
        outputInventory.set(0, newStack);
    }



    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inputInventory);
        Inventories.writeNbt(nbt, outputInventory);

        // Write fluid
        NbtCompound fluidNbt = new NbtCompound();
        fluidNbt.put("variant", inputStorage.variant.toNbt());
        fluidNbt.putLong("amount", inputStorage.amount);
        nbt.put("inputFluid", fluidNbt);

        NbtCompound outputNbt = new NbtCompound();
        outputNbt.put("variant", outputStorage.variant.toNbt());
        outputNbt.putLong("amount", outputStorage.amount);
        nbt.put("outputFluid", outputNbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inputInventory);
        Inventories.readNbt(nbt, outputInventory);
        super.readNbt(nbt);

        // Read fluid
        if (nbt.contains("inputFluid")) {
            NbtCompound fluidNbt = nbt.getCompound("inputFluid");
            inputStorage.variant = FluidVariant.fromNbt(fluidNbt.getCompound("variant"));
            inputStorage.amount = fluidNbt.getLong("amount");

        }

        if (nbt.contains("outputFluid")){
            NbtCompound fluidNbt = nbt.getCompound("outputFluid");
            outputStorage.variant = FluidVariant.fromNbt(fluidNbt.getCompound("variant"));
            inputStorage.amount = fluidNbt.getLong("amount");

        }
    }

    // --- Rest of your existing code unchanged ---

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inputInventory;
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }
    


}