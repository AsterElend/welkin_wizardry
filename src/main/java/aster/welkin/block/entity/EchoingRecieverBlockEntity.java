package aster.welkin.block.entity;

import aster.welkin.api.chat.ChatReceiver;
import aster.welkin.api.chat.GlobalChatReceiver;
import aster.welkin.api.IHasLensInfo;
import aster.welkin.block.EchoingReceiverBlock;
import aster.welkin.registry.WelkinBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
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

public class EchoingRecieverBlockEntity extends BlockEntity implements GlobalChatReceiver, ChatReceiver, IHasLensInfo {
    String storedMessage = null;
    public EchoingRecieverBlockEntity(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.ECHOING_RECEIVER, pos, state);
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        registerForCurrentState();
    }

    @Override
    public void markRemoved() {
        super.markRemoved();
        unregisterChatReceiver();
        unregisterGlobalChatReceiver();
    }

    private void registerForCurrentState() {
        if (world == null || world.isClient) return;
        if (storedMessage == null) {
            registerChatReceiver();
        } else {
            registerGlobalChatReceiver();
        }
    }

    public void clearStoredMessage() {
        if (world == null || world.isClient || storedMessage == null) return;

        unregisterGlobalChatReceiver();
        storedMessage = null;
        registerChatReceiver();
        markDirty();

        if (getCachedState().get(EchoingReceiverBlock.POWERED)) {
            world.setBlockState(pos, getCachedState().with(EchoingReceiverBlock.POWERED, false), 3);
        }
    }

    public void setStoredMessage(String message) {
        this.storedMessage = message;
        this.markDirty();
        if (this.world != null && !this.world.isClient) {
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
        }
    }

    @Override
    public boolean onChatReceived(String message, ServerPlayerEntity sender) {
        if (storedMessage == null) {
            // Setting the password: consume this one, arm the lock
            setStoredMessage(message);
            unregisterChatReceiver();
            registerGlobalChatReceiver();
            markDirty();
            if (this.world != null && !this.world.isClient) {
                this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
            }
            return true;
        }

        // Armed: watch all chat, never consume it
        if (storedMessage.equals(message)) {
            pulseRedstone();
        }
        return false;
    }

    private void pulseRedstone() {
        if (!(world instanceof ServerWorld serverWorld)) return;
        BlockState state = getCachedState();
        if (!state.get(EchoingReceiverBlock.POWERED)) {
            serverWorld.setBlockState(pos, state.with(EchoingReceiverBlock.POWERED, true), 3);
            serverWorld.scheduleBlockTick(pos, state.getBlock(), 4);
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        storedMessage = nbt.contains("StoredMessage") ? nbt.getString("StoredMessage") : null;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (storedMessage != null) {
            nbt.putString("StoredMessage", storedMessage);
        }
    }
    @Override
    public void applyLensOverlay(List<Pair<ItemStack, StringVisitable>> lines,
                                 BlockState state, BlockPos pos,
                                 PlayerEntity observer, World world, Direction hitFace){
        if (world.getBlockEntity(pos) instanceof EchoingRecieverBlockEntity beai) {
            if (beai.storedMessage == null) {
                lines.add(new Pair<>(new ItemStack(Blocks.SCULK_SENSOR),
                        Text.translatable("welkin.scry.echo.idle").setStyle(Style.EMPTY.withColor(Formatting.BLUE))));
            } else {
                lines.add(new Pair<>(new ItemStack(Blocks.CALIBRATED_SCULK_SENSOR),
                        Text.translatable("welkin.scry.echo.listen").append(" ").append(storedMessage)
                                .setStyle(Style.EMPTY.withColor(Formatting.BLUE))));
            }
        }
    }

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
