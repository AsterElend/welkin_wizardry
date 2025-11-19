package aster.welkin.cc;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;


public class SkyForce implements AutoSyncedComponent, ServerTickingComponent {
    public static final int MAX_FORCE = 1000;

    private final PlayerEntity person;
    private int force = 0, forceTimer = 0;

            public SkyForce(PlayerEntity person){
        this.person = person;
            }

            @Override
            public void readFromNbt(NbtCompound tag){
                setForce(tag.getInt("Force"));
                setForceTimer(tag.getInt("ForceTimer"));

            }

            @Override
    public void writeToNbt(NbtCompound tag){
                tag.putInt("Force", getForce());
                tag.putInt("ForceTimer", getForceTimer());
            }

            @Override
    public void serverTick(){
                if (getForceTimer() > 0){
                    setForceTimer(getForceTimer() -1);
                }
            }

            public int getForce() {
                return force;
            }
            public int getForceTimer(){
                return forceTimer;
            }

            public void setForce(int force){
                this.force = force;
                MyComponents.SKYFORCE.sync(person);

            }

            public void setForceTimer(int forceTimer){
                this.forceTimer = forceTimer;
                MyComponents.SKYFORCE.sync(person);
            }

            public boolean fillForce(int amount, boolean simulate){
                if (getForce()< MAX_FORCE){
                    if (!simulate){
                        setForce(Math.min(MAX_FORCE, getForce()+amount));
                    }
                    setForceTimer(30);
                    return true;
                }
                return false;
            }
            public boolean drainMagic(int amount, boolean simulate){
                if (getForce() - amount >= 0) {
                    if (!simulate) {
                        setForce(getForce() - amount);
                    }
                    setForceTimer(30);
                    return true;
                }
                return false;
            }



}

