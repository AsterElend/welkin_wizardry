package aster.welkin.cc;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class AlchemyVisionsComponent implements AutoSyncedComponent {
    private final List<Identifier> discovered = new ArrayList<>() {
    };

    public AlchemyVisionsComponent(PlayerEntity player) {
    }

    public void add(Identifier id){
        discovered.add(id);
    }

    public boolean hasDiscovery(Identifier id){
        return discovered.contains(id);
    }


    @Override
    public void readFromNbt(NbtCompound tag) {

    }

    @Override
    public void writeToNbt(NbtCompound tag) {
     NbtList list = new NbtList();
        for (int i = 0; i < discovered.size(); i++) {
            Identifier stack = discovered.get(i);
            if (!(stack == null)) {
                NbtCompound entry = new NbtCompound();
                entry.putString(String.valueOf(i), discovered.get(i).toString());
                list.add(entry);
            }
        }
    }
}
