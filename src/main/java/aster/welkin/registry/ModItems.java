package aster.welkin.registry;

import aster.welkin.Welkin;
import aster.welkin.item.WireItem;
import aster.welkin.item.GalvanicWand;

import aster.welkin.item.baton.GustBaton;
import aster.welkin.sound.ModSounds;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static List<Item> ALL_ITEMS = new ArrayList<>();
    public static final Item STORMPHRAX = registerItem("stormphrax", new Item(new FabricItemSettings()));
    public static final Item STELLARIUM = registerItem("stellarium", new Item(new FabricItemSettings()));
    public static final Item CHARGESTONE = registerItem("chargestone", new Item(new FabricItemSettings()));
    public static final Item ZEPHYRITE = registerItem("zephyrite", new Item(new FabricItemSettings()) );
    public static final Item GRIMORE = registerItem("grimore", new Item(new FabricItemSettings()));

    public static final Item STORMCYCLE_DISC = registerItem("stormcycle_disc",
            new MusicDiscItem(15, ModSounds.STORMCYCLE,  new FabricItemSettings().maxCount(1), 36 ));
    public static final Item WINDTUNNEL_DISC = registerItem("windtunnel_disc",
            new MusicDiscItem(15, ModSounds.WINDTUNNEL,  new FabricItemSettings().maxCount(1), 94 ));

    public static final Item WAND = registerItem("wand", new GalvanicWand(new FabricItemSettings()));
    public static final Item WIRE = registerItem("wire", new WireItem(new FabricItemSettings()));
      public static final Item GUST = registerItem("gust_baton", new GustBaton(new FabricItemSettings().maxCount(1)));
  //  public static final Item DOLLY = registerItem("baton/dolly", new AbscondBatonItem(new FabricItemSettings().maxCount(1)));










public static Item registerItem(String name, Item item) {
    ALL_ITEMS.add(item);
    return Registry.register(Registries.ITEM, new Identifier(Welkin.MOD_ID, name), item); }

public static void registerModItems() {

    Welkin.LOGGER.info("Registering Mod Items for " + Welkin.MOD_ID);

}
}


