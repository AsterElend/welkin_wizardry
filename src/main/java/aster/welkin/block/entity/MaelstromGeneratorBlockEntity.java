package aster.welkin.block.entity;

import aster.welkin.api.Linkable;
import aster.welkin.registry.WelkinBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MaelstromGeneratorBlockEntity extends Linkable {
    private int cooldown;
    private static final int BASE_COOLDOWN = 20;
    public MaelstromGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.MAELSTROM_GENERATOR, pos, state);
        this.cooldown = 0;
        this.range = 8;
    }

    public void tick(World world){
        if (!world.isRaining()) return;
        if (linkPos == null) return;
        if (!(world.getBlockEntity(linkPos) instanceof ThunderheadBlockEntity thunderhead)) return;
        if (cooldown <= 0){
            thunderhead.acceptAether(1);
            if (world.isThundering()){
                thunderhead.acceptAether(1);
            }
            cooldown = BASE_COOLDOWN;
            return;
        }
        cooldown--;
    }

    @Override
    public void readNbt(NbtCompound nbt){
        readLink(nbt);
        cooldown = nbt.getInt("cooldown");
    }
    @Override
    public void writeNbt(NbtCompound nbt){
        nbt.putInt("cooldown", cooldown);
        writeLink(nbt);
    }
}
