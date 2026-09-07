package aster.welkin.cc;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class LastDeathSourceComponent implements AutoSyncedComponent {
    private final PlayerEntity player;
    private RegistryKey<DamageType> deathType = null;

    public LastDeathSourceComponent(PlayerEntity player) {
        this.player = player;
    }

    public void setType(RegistryKey<DamageType> type){
        deathType = type;
    }

    public boolean isThisTheStoredDamageType(RegistryKey<DamageType> type){
        return type == deathType;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (!tag.contains("deathType")) return;
        Identifier id = new Identifier(tag.getString("deathType"));
       deathType = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (deathType != null){
            tag.putString("deathType", deathType.getValue().toString());
        }
    }
}