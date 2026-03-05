package aster.welkin.cc;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;


public class SkyForceComponent implements AutoSyncedComponent {
    public static final int MAX_FORCE = 1000;
    private int force = 0;

    private final PlayerEntity player;
    public SkyForceComponent(PlayerEntity player){
        this.player = player;
    }
    public int getForce() {
        return force;
    }

    public void setForce(int value) {
        force = Math.max(0, Math.min(MAX_FORCE, value));
        WelkinEntityCC.FORCE.sync(player);
    }

    public boolean consume(int amount) {
        if (force < amount) return false;
        setForce(force - amount);
        return true;
    }

    public void regenerate(int amount) {
        setForce(force + amount);
    }


    @Override
    public void readFromNbt(NbtCompound tag) {
        if (tag.contains("force", NbtElement.INT_TYPE)) {
            force = tag.getInt("force");
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("force", force);
    }
}

