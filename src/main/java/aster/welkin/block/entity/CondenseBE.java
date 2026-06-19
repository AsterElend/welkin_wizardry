package aster.welkin.block.entity;

import aster.welkin.block.CondenseBlock;
import aster.welkin.registry.ModBlockEntities;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.impl.transfer.fluid.CauldronStorage;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class CondenseBE extends BlockEntity {
    int interval = 80;
    int cooldown = 0;
    public CondenseBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONDENSER, pos, state);
    }

    public void tick(BlockPos pos, BlockState state) {
        if (cooldown <= 0){
            Direction facing = state.get(CondenseBlock.FACING);
            BlockPos targetPos = pos.offset(facing);

            Storage<FluidVariant> target = getFluidStorage(targetPos, facing);
            if (target == null) return;

            FluidVariant water = FluidVariant.of(Fluids.WATER);

            try (Transaction tx = Transaction.openOuter()) {
                target.insert(water, FluidConstants.BOTTLE, tx);
                tx.commit();
            }
            cooldown = interval;
            return;
        }

        cooldown--;


    }

    @Override
    public void readNbt(NbtCompound nbt){
        super.readNbt(nbt);
        cooldown = nbt.getInt("cooldown");
    }

    @Override
    public void writeNbt(NbtCompound nbt){
        super.writeNbt(nbt);
        nbt.putInt("cooldown", cooldown);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = super.toInitialChunkDataNbt();
        writeNbt(nbt);
        return nbt;
    }

    @Nullable
    public Storage<FluidVariant> getFluidStorage(BlockPos targetPos, Direction side) {
        // try normal sided lookup first
        Storage<FluidVariant> storage = FluidStorage.SIDED.find(world, targetPos, side);
        if (storage != null) return storage;

        // fall back to cauldron
        BlockState state = world.getBlockState(targetPos);
        if (state.getBlock() instanceof AbstractCauldronBlock) {
            return CauldronStorage.get(world, targetPos);
        }

        return null;
    }
}
