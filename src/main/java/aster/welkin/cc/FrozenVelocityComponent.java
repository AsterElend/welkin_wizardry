package aster.welkin.cc;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;

public class FrozenVelocityComponent implements AutoSyncedComponent {
    private Vec3d velocitySnapshot = Vec3d.ZERO;
    private boolean locked = false;
    private final PlayerEntity player;

    public FrozenVelocityComponent(PlayerEntity player) {
        this.player = player;
    }
    public boolean isLocked(){
        return locked;
    }

    public Vec3d getSnapshot(){
        return velocitySnapshot;
    }
    public void freeze(Vec3d velocity){
        locked = true;
        velocitySnapshot = velocity;
        if (player.isOnGround()){
            velocitySnapshot = new Vec3d(velocity.x, 0, velocity.z);
        }
        WelkinEntityCC.FROZEN_MOMENTUM.sync(player);
    }
    public void unfreeze(){
        locked = false;
        velocitySnapshot = Vec3d.ZERO;
        WelkinEntityCC.FROZEN_MOMENTUM.sync(player);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        velocitySnapshot = new Vec3d(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
        locked = tag.getBoolean("locked");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putDouble("x", velocitySnapshot.x);
        tag.putDouble("y", velocitySnapshot.y);
        tag.putDouble("z", velocitySnapshot.z);
        tag.putBoolean("locked", locked);
    }
}
