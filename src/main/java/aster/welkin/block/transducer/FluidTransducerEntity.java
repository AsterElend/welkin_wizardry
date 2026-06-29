package aster.welkin.block.transducer;

import aster.welkin.registry.ModBlockEntities;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.impl.transfer.fluid.CauldronStorage;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FluidTransducerEntity extends TransducerEntity{
    int base_interval = 80;
    public FluidTransducerEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FLUID_TRANSDUCER, pos, state);
    }

    public void serverTick(World world, BlockPos pos, BlockState state) {
        if (world.isReceivingRedstonePower(pos)) return;
        if (state.get(TransducerBlock.IS_SEND) && cooldown <= 0) {
            Storage<FluidVariant> source = getMyFluidStorage();
            Storage<FluidVariant> dest = getNotMyFluidStorage();
            if (source != null && dest != null) {
                moveFluid(source, dest);
                cooldown = base_interval;
            }
        }
        cooldown--;
    }


    @Nullable
    protected Storage<FluidVariant> getMyFluidStorage() {
        return getFluidStorage(getFacingPos(), getFacing().getOpposite());
    }

    @Nullable
    protected Storage<FluidVariant> getNotMyFluidStorage() {
        if (linkedNode == null) return null;
        if (world.getBlockEntity(linkedNode) instanceof TransducerEntity transducer) {
            return getFluidStorage(transducer.getFacingPos(), transducer.getFacing().getOpposite());
        }
        return null;
    }

    public static void moveFluid(Storage<FluidVariant> source, Storage<FluidVariant> destination) {
        for (StorageView<FluidVariant> view : source) {
            if (view.isResourceBlank() || view.getAmount() <= 0) continue;

            FluidVariant fluid = view.getResource();
            long amount = Math.min(view.getAmount(), FluidConstants.BOTTLE);

            long extracted = 0;
            try (Transaction tx = Transaction.openOuter()) {
                extracted = source.extract(fluid, amount, tx);
                long inserted = destination.insert(fluid, extracted, tx);
                if (inserted == extracted) {
                    tx.commit();
                }
                // if not committed, transaction rolls back automatically
            }
            return;
        }
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
