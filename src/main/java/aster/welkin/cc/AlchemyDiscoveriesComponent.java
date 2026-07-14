package aster.welkin.cc;

import aster.welkin.recipes.RecyclerRecipe;
import aster.welkin.recipes.RecyclerRecipeManager;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
public class AlchemyDiscoveriesComponent implements AutoSyncedComponent {
    private final PlayerEntity player;
    private final LinkedHashSet<Identifier> discovered = new LinkedHashSet<>();

    public AlchemyDiscoveriesComponent(PlayerEntity player) {
        this.player = player;
    }

    /** Returns true if this was a new discovery */
    public boolean add(Identifier id) {
        boolean isNew = discovered.add(id);
        if (isNew) sync();
        return isNew;
    }

    public boolean hasDiscovery(Identifier id) {
        return discovered.contains(id);
    }

    public List<Identifier> getDiscoveries() {
        return List.copyOf(discovered);
    }

    private void sync() {
        if (player != null && !player.getWorld().isClient) {
            WelkinEntityCC.DISCOVERIES.sync(player);
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        discovered.clear();
        NbtList list = tag.getList("Discovered", NbtElement.STRING_TYPE);
        for (NbtElement element : list) {
            Identifier id = Identifier.tryParse(element.asString());
            if (id != null) discovered.add(id);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtList list = new NbtList();
        for (Identifier id : discovered) {
            list.add(NbtString.of(id.toString()));
        }
        tag.put("Discovered", list);
    }

    public static void tryDiscover(PlayerEntity player, Identifier itemId) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;

        RecyclerRecipe recipe = RecyclerRecipeManager.getRecipeForOutput(itemId);
        if (recipe == null) return; // not an alchemy output

        AlchemyDiscoveriesComponent component = WelkinEntityCC.DISCOVERIES.get(serverPlayer);
        if (component.add(itemId)) {
            serverPlayer.sendMessage(
                    Text.translatable("welkin.alchemy.discovered", recipe.getOutput().getName()),
                    true
            );
        }
    }
}