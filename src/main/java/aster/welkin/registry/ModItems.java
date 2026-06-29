package aster.welkin.registry;

import aster.welkin.Welkin;
import aster.welkin.item.*;
import aster.welkin.item.baton.GustBaton;
import aster.welkin.sound.ModSounds;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BucketItem;
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
    public static final Item CRYSTAL_HEART = registerItem("crystal_heart", new Item(new FabricItemSettings()));
    public static final Item EXTRACTED_HEART = registerItem("extracted_heart", new Item(new FabricItemSettings().fireproof()));

    public static final Item GRIMORE = registerItem("grimore", new Item(new FabricItemSettings()));

    public static final Item STORMCYCLE_DISC = registerItem("stormcycle_disc",
            new MusicDiscItem(15, ModSounds.STORMCYCLE,  new FabricItemSettings().maxCount(1), 36 ));
    public static final Item WINDTUNNEL_DISC = registerItem("windtunnel_disc",
            new MusicDiscItem(15, ModSounds.WINDTUNNEL,  new FabricItemSettings().maxCount(1), 94 ));

    public static final Item GALVANIC_WAND = registerItem("galvanic_wand", new GalvanicWand(new FabricItemSettings()));
    public static final Item WIRE = registerItem("wire", new WireItem(new FabricItemSettings()));
    public static final Item GUST_BATON = registerItem("gust_baton", new GustBaton(new FabricItemSettings().maxCount(1)));
    public static final Item HALO_BATON = registerItem("halo_baton", new HaloWand(new FabricItemSettings().maxCount(1)));
  //  public static final Item DOLLY = registerItem("baton/dolly", new AbscondBatonItem(new FabricItemSettings().maxCount(1)));
  public static final Item WARDING_PRISM = registerItem("warding_prism", new WardingPrismItem(new FabricItemSettings().maxCount(1)));
    public static final Item LETHEAN_WATER_BUCKET = registerItem("lethean_water_bucket", new BucketItem(LoomFluids.LETHEAN_WATER_STATIC,
            new FabricItemSettings().maxCount(1)));

    public static final Item LETHEAN_WATER_BOTTLE = registerItem("lethean_water_bottle", new LetheanWaterBottle(new FabricItemSettings().maxCount(16)));
    public static final Item WORLD_SALTS = registerItem("world_salts", new WorldSaltsItem(new FabricItemSettings().maxCount(16)));









public static Item registerItem(String name, Item item) {
    ALL_ITEMS.add(item);
    return Registry.register(Registries.ITEM, new Identifier(Welkin.MOD_ID, name), item); }

public static void registerModItems() {

    Welkin.LOGGER.info("Registering Mod Items for " + Welkin.MOD_ID);

}
}


