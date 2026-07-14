package aster.welkin.block.transducer;

import aster.welkin.api.IHasLensInfo;
import aster.welkin.api.Linkable;
import aster.welkin.registry.WelkinBlockEntities;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.impl.transfer.fluid.CauldronStorage;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.state.property.Properties;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FluidTransducerEntity extends Linkable implements IHasLensInfo {
    int base_interval = 80;
    int cooldown;
    public FluidTransducerEntity(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.FLUID_TRANSDUCER, pos, state);
        this.range = 16;
    }

    public void serverTick(World world, BlockPos pos, BlockState state) {
        if (world.isReceivingRedstonePower(pos)) return;
        if (cooldown <= 0) {
            Storage<FluidVariant> source = getMyFluidStorage(pos, state);
            Storage<FluidVariant> dest = getNotMyFluidStorage();
            if (source != null && dest != null) {
                moveFluid(source, dest);
                cooldown = base_interval;
            }
        }
        cooldown--;
    }


    @Nullable
    protected Storage<FluidVariant> getMyFluidStorage(BlockPos pos, BlockState state) {
        return getFluidStorage(pos.offset(state.get(Properties.FACING).getOpposite()),
                state.get(Properties.FACING).getOpposite());
    }

    @Nullable
    protected Storage<FluidVariant> getNotMyFluidStorage() {
        if (linkPos == null) return null;
        getFluidStorage(linkPos, linkSide);
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

    @Override
    public void readNbt(NbtCompound nbt){
        super.readNbt(nbt);
        cooldown = nbt.getInt("cooldown");
        readLink(nbt);
    }
    @Override
    public void writeNbt(NbtCompound nbt){
        super.writeNbt(nbt);
        nbt.putInt("cooldown", cooldown);
        writeLink(nbt);
    }


    @Override
    public void applyLensOverlay(List<Pair<ItemStack, StringVisitable>> lines,
                                 BlockState state, BlockPos pos,
                                 PlayerEntity observer, World world, Direction hitFace){
        if (world.getBlockEntity(pos) instanceof FluidTransducerEntity entity) {
            if (entity.linkPos == null) {
                lines.add(new Pair<>(new ItemStack(Items.SLIME_BALL),
                        Text.translatable("welkin.scry.trans.idle").setStyle(Style.EMPTY.withColor(Formatting.BLUE))));
            } else {
                lines.add(new Pair<>(new ItemStack(Blocks.CALIBRATED_SCULK_SENSOR),
                        Text.translatable("welkin.scry.trans.linked").append(" ").append(linkPos.toShortString())
                                ));
                lines.add(new Pair<>(new ItemStack(Items.SPECTRAL_ARROW),
                        Text.translatable("welkin.scry.trans.side1").append(linkSide.getName())
                                .append(Text.translatable("welkin.scry.trans.side2"))));
            }


        }



    };

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt(); // or however you're building save NBT — reuse your writeNbt logic
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }


}
