package aster.welkin.block.entity;

import aster.welkin.api.AetherHolder;
import aster.welkin.api.Linkable;
import aster.welkin.api.WelkinUtil;
import aster.welkin.registry.WelkinBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AetherTransducerBlockEntity extends Linkable  {
    private int cooldown;
    private int BASE_COOLDOWN = 20;
    public AetherTransducerBlockEntity(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.AETHER_TRANSDUCER, pos, state);
        cooldown = 0;
        this.range = 16;
    }

    public void tick(World world, BlockPos pos, BlockState state){
        if (linkPos == null) return;
        if (world.isReceivingRedstonePower(pos)) return;
        if (cooldown > 0){
            cooldown--;
            return;
        }

        var myHolder = getMyHolder(pos, state);
        var otherHolder = getTheOtherOne();

        if (myHolder != null && otherHolder != null){
            int transfer = 10;
            if (!myHolder.hasSufficientAether(transfer) ){
                transfer = myHolder.getAether();
            }
            if (transfer <= 0 ) {
                cooldown = BASE_COOLDOWN;
                return;
            }

            otherHolder.acceptAether(-transfer);
            WelkinUtil.yellAtEverything(myHolder);
            WelkinUtil.yellAtEverything(otherHolder);
            cooldown = BASE_COOLDOWN;
            return;
        }

       cooldown = BASE_COOLDOWN;

    }
    @Nullable
    protected AetherHolder getMyHolder(BlockPos pos, BlockState state){
        if (world.getBlockEntity(pos.offset(state.get(Properties.FACING).getOpposite())) instanceof AetherHolder holder){
            return holder;
        }
        return null;
    }

    @Nullable
    protected  AetherHolder getTheOtherOne(){
        if (world.getBlockEntity(linkPos) instanceof AetherHolder holder){
            return holder;
        }
        return null;
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
}
