package aster.welkin.registry;

import aster.welkin.Welkin;
import aster.welkin.api.SCREEN_INVOCATIONS;
import aster.welkin.item.*;
import aster.welkin.item.baton.ConductorBatonItem;
import aster.welkin.item.baton.WindstormerBatonItem;
import aster.welkin.item.baton.BricklayerBatonItem;
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

public class WelkinItems {
    public static List<Item> ALL_ITEMS = new ArrayList<>();
    public static final Item STORMPHRAX = registerItem("stormphrax", new Item(new FabricItemSettings()));
    public static final Item STELLARIUM = registerItem("stellarium", new Item(new FabricItemSettings()));
    public static final Item CHARGESTONE = registerItem("chargestone", new Item(new FabricItemSettings()));
    public static final Item ZEPHYRITE = registerItem("zephyrite", new Item(new FabricItemSettings()) );
    public static final Item CRYSTAL_HEART = registerItem("crystal_heart", new Item(new FabricItemSettings()));
    public static final Item EXTRACTED_HEART = registerItem("extracted_heart", new Item(new FabricItemSettings().fireproof()));
    public static final Item WISHFUL_BATON = registerItem("wishful_baton", new Item(new FabricItemSettings()));
    public static final Item GRIMORE = registerItem("grimore", new Item(new FabricItemSettings().maxCount(1)));
    public static final Item OCULATOR_LENS = registerItem("oculator_lens", new Item(new FabricItemSettings().maxCount(1)));

    public static final Item STORMCYCLE_DISC = registerItem("stormcycle_disc",
            new MusicDiscItem(15, ModSounds.STORMCYCLE,  new FabricItemSettings().maxCount(1), 36 ));
    public static final Item WINDTUNNEL_DISC = registerItem("windtunnel_disc",
            new MusicDiscItem(15, ModSounds.WINDTUNNEL,  new FabricItemSettings().maxCount(1), 94 ));

    public static final Item CONDUCTOR_BATON = registerItem("conductor_baton", new ConductorBatonItem(new FabricItemSettings()));

    public static final Item WINDSTORMER_BATON = registerItem("windstormer_baton", new WindstormerBatonItem(new FabricItemSettings().maxCount(1)));
    public static final Item BRICKLAYER_BATON = registerItem("bricklayer_baton", new BricklayerBatonItem(new FabricItemSettings().maxCount(1)));
  //  public static final Item DOLLY = registerItem("mover_baton", new AbscondBatonItem(new FabricItemSettings().maxCount(1)));
  public static final Item WARDING_PRISM = registerItem("warding_prism", new WardingPrismItem(new FabricItemSettings().maxCount(1)));
    public static final Item LETHEAN_WATER_BUCKET = registerItem("lethean_water_bucket", new BucketItem(LoomFluids.LETHEAN_WATER_STATIC,
            new FabricItemSettings().maxCount(1)));

    public static final Item LETHEAN_WATER_BOTTLE = registerItem("lethean_water_bottle", new LetheanWaterBottle(new FabricItemSettings().maxCount(16)));
    public static final Item WORLD_SALTS = registerItem("world_salts", new WorldSaltsItem(new FabricItemSettings().maxCount(16)));
    public static final Item ALCHEMY_SLATE = registerItem("alchemy_slate",
            new GenericScreenInvocationItem(new FabricItemSettings().maxCount(1), false, SCREEN_INVOCATIONS.ALCHEMY_SLATE));

  public static final Item DEBUG_ALCHEMY_SLATE = registerItem("debug_alchemy_slate",
            new GenericScreenInvocationItem(new FabricItemSettings().maxCount(1), true, SCREEN_INVOCATIONS.ALCHEMY_SLATE));









public static Item registerItem(String name, Item item) {
    ALL_ITEMS.add(item);
    return Registry.register(Registries.ITEM, new Identifier(Welkin.MOD_ID, name), item); }

public static void registerModItems() {

    Welkin.LOGGER.info("Registering Mod Items for " + Welkin.MOD_ID);

}
}


