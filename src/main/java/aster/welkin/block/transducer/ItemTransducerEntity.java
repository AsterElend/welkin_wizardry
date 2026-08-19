package aster.welkin.block.transducer;

import aster.welkin.api.IHasLensInfo;
import aster.welkin.api.Linkable;
import aster.welkin.registry.WelkinBlockEntities;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ItemTransducerEntity extends Linkable implements IHasLensInfo {
   private int cooldown;
    static int base_interval = 20;


    public void serverTick(World world, BlockPos pos, BlockState state) {
        if (world.isReceivingRedstonePower(pos)) return;
        if ( cooldown <= 0) {
            Storage<ItemVariant> source = getMyItemStorage( pos, state);
            Storage<ItemVariant> dest = getNotMyItemStorage();
            if (source != null && dest != null) {
                moveOneItem(source, dest);
                cooldown = base_interval;
            }
        }
    cooldown--;
    }

    public ItemTransducerEntity(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.ITEM_TRANSDUCER, pos, state);
        this.range = 16;
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
    @Override
    public void readNbt(NbtCompound nbt){
        super.readNbt(nbt);
        readLink(nbt);
        cooldown = nbt.getInt("cooldown");
    }
    @Override
    public void writeNbt(NbtCompound nbt){
        super.writeNbt(nbt);
        writeLink(nbt);
        nbt.putInt("cooldown", cooldown);
    }

    protected Storage<ItemVariant> getMyItemStorage(BlockPos pos, BlockState state) {
        return ItemStorage.SIDED.find(world, pos.offset(state.get(Properties.FACING).getOpposite()),
                state.get(Properties.FACING).getOpposite());
    }

    @Nullable
    protected Storage<ItemVariant> getNotMyItemStorage() {
        if (linkPos == null) return null;
        return ItemStorage.SIDED.find(this.getWorld(), linkPos, linkSide);
    }




}