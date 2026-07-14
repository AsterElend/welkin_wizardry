package aster.welkin.api;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public abstract class Linkable extends BlockEntity {
   protected BlockPos linkPos = null;
    protected Direction linkSide = null;
    protected double range = 0;

    public Linkable(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public boolean setLink(BlockPos pos, Direction side){
        double distance = Math.sqrt(this.getPos().getSquaredDistance(pos)); // was: linkPos
        if (distance <= range){
            linkPos = pos;
            this.linkSide = side;
            this.markDirty();
            if (this.world != null && !this.world.isClient) {
                this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
            }
            return true;
        }
        return false;
    }
    public  BlockPos getLinkPos(){ return linkPos;}
    public  Direction getLinkSide(){return linkSide;}

    public void writeLink(NbtCompound nbt){
        if (linkPos == null) return;
        nbt.putLong("linkpos", linkPos.asLong());
        nbt.putInt( "ordinalvalueofthedirectionbecausethereisntabetterwaytodoit", linkSide.ordinal());
    }

    public  void readLink(NbtCompound nbt){
        if (!nbt.contains("linkpos")) return;
        linkPos = BlockPos.fromLong(nbt.getLong("linkpos"));
        linkSide = Direction.values()[nbt.getInt("ordinalvalueofthedirectionbecausethereisntabetterwaytodoit")];
    }
}
