package aster.welkin.api;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class LinkablePedestal extends Linkable implements ImplementedInventory, PedestalInteractable{
    protected final PedestalLogic logic = new PedestalLogic(this);

    public LinkablePedestal(BlockEntityType<?> type, BlockPos pos, BlockState state){
        super(type, pos, state);
    }



    @Override
    public DefaultedList<ItemStack> getItems(){
        return logic.getItems();
    }


    @Override
    public NbtCompound toInitialChunkDataNbt(){
        return createNbt();
    }

    @Override
    @Nullable
    public Packet<ClientPlayPacketListener> toUpdatePacket(){
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        Inventories.readNbt(nbt, this.logic.getItems());
        readLink(nbt);
        readAdditionalData(nbt);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.logic.getItems());
        writeLink(nbt);
     writeAdditionalData(nbt);
    }

    public void writeAdditionalData(NbtCompound nbt){

    }

    public void readAdditionalData(NbtCompound nbt){

    }

    @Override
    public ItemStack stackInteractionAttempt(ItemStack stack) {
        return logic.stackInteractionAttempt(stack);

    }



    @Override
    public void doSneakInteraction(PlayerEntity player, Hand hand) {
        logic.doSneakInteraction(player, hand);
    }
}
