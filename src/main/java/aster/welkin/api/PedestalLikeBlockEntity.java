package aster.welkin.api;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public abstract class PedestalLikeBlockEntity extends BlockEntity implements ImplementedInventory, PedestalInteractable {
    protected final PedestalLogic logic = new PedestalLogic(this);

    @Override
    public ItemStack stackInteractionAttempt(ItemStack incoming) {
        ItemStack stack =  logic.stackInteractionAttempt(incoming);
        WelkinUtil.yellAtEverything(this);
        return stack;
    }

    @Override
    public void doSneakInteraction(PlayerEntity player, Hand hand) {

        logic.doSneakInteraction(player, hand);
        WelkinUtil.yellAtEverything(this);
    }

    public PedestalLogic getLogic(){
        return logic;
    }

    public PedestalLikeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state){
        super(type, pos, state);


    }

    public void transmuteTo(Item item){
        int count = logic.getCount();
        logic.setStack(new ItemStack(item, count));
        WelkinUtil.yellAtEverything(this);
    }

    public ItemStack getStack(){
        return logic.getStack();
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
        readAdditionalData(nbt);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.logic.getItems());
        storeAdditionalData(nbt);
    }

    public void storeAdditionalData(NbtCompound nbt){

    }

    public void readAdditionalData(NbtCompound nbt){

    }
}
