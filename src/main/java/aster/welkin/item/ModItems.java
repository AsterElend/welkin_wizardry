package aster.welkin.item;

import aster.welkin.Welkin;
import aster.welkin.sound.ModSounds;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item STORMPHRAX = registerItem("stormphrax", new Item(new FabricItemSettings()));
    public static final Item STELLARIUM = registerItem("stellarium", new Item(new FabricItemSettings()));
    public static final Item CHARGESTONE = registerItem("chargestone", new Item(new FabricItemSettings()));
    public static final Item ZEPHYRITE = registerItem("zephyrite", new Item(new FabricItemSettings()) );
    public static final Item GRIMORE = registerItem("grimore", new Item(new FabricItemSettings()));
    public static final Item WAND = registerItem("wand", new Item(new FabricItemSettings()));
    public static final Item STORMCYCLE_DISC = registerItem("stormcycle_disc",
            new MusicDiscItem(15, ModSounds.STORMCYCLE,  new FabricItemSettings().maxCount(1), 36 ));
    public static final Item WINDTUNNEL_DISC = registerItem("windtunnel_disc",
            new MusicDiscItem(15, ModSounds.WINDTUNNEL,  new FabricItemSettings().maxCount(1), 94 ));

    private static void addItemsToWWItemGroup(FabricItemGroupEntries entries) {
    entries.add(STORMPHRAX);
    entries.add(CHARGESTONE);
    entries.add(ZEPHYRITE);
    entries.add(STELLARIUM);
    entries.add(WAND);
    entries.add(STORMCYCLE_DISC);
    entries.add(WINDTUNNEL_DISC);

    }





public static Item registerItem(String name, Item item) {
    return Registry.register(Registries.ITEM, new Identifier(Welkin.MOD_ID, name), item); }
public static void registerModItems() {

    Welkin.LOGGER.info("Registering Mod Items for " + Welkin.MOD_ID);
    ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToWWItemGroup);
}
}


