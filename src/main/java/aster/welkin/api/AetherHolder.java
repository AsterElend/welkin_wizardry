package aster.welkin.api;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public abstract class AetherHolder extends BlockEntity {
    private int aether;
    private final int MAX_AETHER = 1000000;
    public int getAether(){
        return aether;
    }

    public boolean hasSufficientAether(int query){
        return aether >= query;
    }

    public AetherHolder(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public boolean acceptAether(int toAdd) {
        if (aether + toAdd > this.MAX_AETHER) return false;
        aether += toAdd;
        WelkinUtil.yellAtEverything(this);
        return true;
    }

    @Override
    public void readNbt(NbtCompound nbt){
        aether = nbt.getInt("aether");
    }

    @Override
    public void writeNbt(NbtCompound nbt){
        nbt.putInt("aether", aether);
    }

}
