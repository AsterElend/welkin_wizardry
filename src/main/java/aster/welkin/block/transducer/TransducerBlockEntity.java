package aster.welkin.block.transducer;

import aster.welkin.registry.ModBlockEntities;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TransducerBlockEntity extends BlockEntity {

    private boolean isInput = false;
    private BlockPos linkedPos = null;

    public TransducerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TRANSDUCER, pos, state);
    }

    /* ─────────────────────────────
                USER ACTIONS
       ───────────────────────────── */

    public void toggleInputMode(PlayerEntity player) {
        isInput = !isInput;
        player.sendMessage(Text.literal(
                isInput ? "Transducer set to INPUT" : "Transducer set to OUTPUT"), true);
        markDirty();
    }

    public void setLinkedPos(BlockPos other) {
        this.linkedPos = other;
        markDirty();
    }

    /* ─────────────────────────────
                TICK LOGIC
       ───────────────────────────── */

    public static void tick(World world, BlockPos pos,
                            BlockState state, TransducerBlockEntity be) {
        be.serverTick(world);
    }

    private void serverTick(World world) {
        if (world.isClient) return;
        if (linkedPos == null) return;

        BlockEntity otherBe = world.getBlockEntity(linkedPos);
        if (!(otherBe instanceof TransducerBlockEntity other)) return;

        // Must be opposite mode: one INPUT, one OUTPUT
        if (this.isInput == other.isInput) return;

        TransducerBlockEntity input = this.isInput ? this : other;
        TransducerBlockEntity output = this.isInput ? other : this;

        moveFluid(world, input, output);
    }

    private void moveFluid(World world, TransducerBlockEntity input, TransducerBlockEntity output) {

        BlockEntity inputInv = world.getBlockEntity(input.getPos().down());
        BlockEntity outputInv = world.getBlockEntity(output.getPos().down());

        if (inputInv == null || outputInv == null) return;

        Storage<FluidVariant> inputStorage =
                FluidStorage.SIDED.find(world, inputInv.getPos(), null);
        Storage<FluidVariant> outputStorage =
                FluidStorage.SIDED.find(world, outputInv.getPos(), null);

        if (inputStorage == null || outputStorage == null) return;

        try (Transaction tx = Transaction.openOuter()) {
            long moved = StorageUtil.move(inputStorage, outputStorage, fv -> true, 50, tx);
            if (moved > 0)
                tx.commit();
        }
    }

    /* ─────────────────────────────
                 NBT SYNC
       ───────────────────────────── */

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean("IsInput", isInput);
        if (linkedPos != null)
            nbt.putLong("LinkedPos", linkedPos.asLong());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        isInput = nbt.getBoolean("IsInput");
        if (nbt.contains("LinkedPos"))
            linkedPos = BlockPos.fromLong(nbt.getLong("LinkedPos"));
    }
}
