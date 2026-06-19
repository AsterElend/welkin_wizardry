package aster.welkin.block.transducer;

import aster.welkin.registry.ModBlockEntities;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
public class ItemTransducerEntity extends TransducerEntity {


    public void serverTick(World world, BlockPos pos, BlockState state) {
        if (world.isReceivingRedstonePower(pos)) return;
        if (state.get(TransducerBlock.IS_SEND) && cooldown <= 0) {
            Storage<ItemVariant> source = getMyItemStorage();
            Storage<ItemVariant> dest = getNotMyItemStorage();
            if (source != null && dest != null) {
                moveOneItem(source, dest);
                cooldown = base_interval;
            }
        }
        cooldown--;
    }

    public ItemTransducerEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ITEM_TRANSDUCER, pos, state);
    }

    public static void moveOneItem(Storage<ItemVariant> source, Storage<ItemVariant> destination) {
        for (StorageView<ItemVariant> view : source) {
            if (view.isResourceBlank() || view.getAmount() <= 0) continue;

            ItemVariant item = view.getResource();

            try (Transaction tx = Transaction.openOuter()) {
                long extracted = source.extract(item, 1, tx);
                if (extracted == 0) continue;
                long inserted = destination.insert(item, 1, tx);
                if (inserted == 1) {
                    tx.commit();
                }
                // otherwise auto-rollback, try next slot
            }
            return;
        }
    }

    public void readNbt(NbtCompound nbt){
        super.readNbt(nbt);
    }

    public void writeNbt(NbtCompound nbt){
        super.writeNbt(nbt);

    }

    @Nullable
    protected Storage<ItemVariant> getMyItemStorage() {
        return ItemStorage.SIDED.find(world, getFacingPos(), getFacing().getOpposite());
    }

    @Nullable
    protected Storage<ItemVariant> getNotMyItemStorage() {
        if (linkedNode == null) return null;
        if (world.getBlockEntity(linkedNode) instanceof TransducerEntity transducer) {
            return ItemStorage.SIDED.find(world, transducer.getFacingPos(), transducer.getFacing().getOpposite());
        }
        return null;
    }
}