package aster.welkin.block.transducer;

import aster.welkin.registry.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public abstract class TransducerEntity extends BlockEntity {




    protected BlockPos linkedNode = null;
    protected Identifier augment = null;
    protected int cooldown = 0;
    protected static int base_interval = 5;

    public TransducerEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public BlockPos getLinkedNode(){
        return linkedNode;
    }
    public boolean hasLink(){
        return linkedNode != null;
    }
    public void socketAugment(Identifier id){
        augment = id;
        changed();
    }

    public void linkNode(BlockPos pos){
        linkedNode = pos;
        changed();
    }

    private void unlink(){
        linkedNode = null;
        changed();
    }
    private void changed(){
        markDirty();
        if (world != null && !world.isClient){
            world.updateListeners(this.pos, getCachedState(), getCachedState(), 3);
        }
    }




    public Direction getFacing(){
        if (world == null) return Direction.NORTH;
        return world.getBlockState(this.getPos()).get(TransducerBlock.FACING);
    }

    public BlockPos getFacingPos(){
        return pos.offset(getFacing().getOpposite());
    }

    @Override
    public void readNbt(NbtCompound nbt){
        super.readNbt(nbt);
        if (nbt.contains("linkedNode")){
            linkedNode = BlockPos.fromLong(nbt.getLong("linkedNode"));
        }


        if (nbt.contains("augment")){
            augment = new Identifier(nbt.getString("augment"));
        }

        cooldown = nbt.getInt("cooldown");

    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (linkedNode != null) {
            nbt.putLong("linkedNode", linkedNode.asLong());
        }
        if (augment != null) {
            nbt.putString("augment", augment.toString());
        }
        nbt.putInt("cooldown", cooldown);
    }



    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = super.toInitialChunkDataNbt();
        writeNbt(nbt);
        return nbt;
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public void delink(BlockPos dropPos) {
        if (linkedNode == null) return;
        if (world.getBlockEntity(linkedNode) instanceof TransducerEntity transducer) {
            transducer.unlink();
            changed();
        }
        unlink();
        ItemScatterer.spawn(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(),
                new ItemStack(ModItems.WIRE));
    }

}
