package aster.welkin.registry;

import aster.welkin.Welkin;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class WelkinTags {
    public static void register(){};
    public static final TagKey<Item> SHOW_FORCE = TagKey.of(RegistryKeys.ITEM, Identifier.of("welkin", "show_force"));
    public static final TagKey<Block> ABSCONDABLE = TagKey.of(RegistryKeys.BLOCK, Identifier.of("welkin", "abscondable"));
    public static final TagKey<Item> TAKE_HEART = TagKey.of(RegistryKeys.ITEM, Welkin.id("take_heart"));
    public static final TagKey<Fluid> LETHEAN_WATER = TagKey.of(RegistryKeys.FLUID, Welkin.id("lethean_water"));
    public static final TagKey<Fluid> FALSE_MILK = TagKey.of(RegistryKeys.FLUID, Welkin.id("false_milk"));
    public static final TagKey<Block> YOINKABLE = TagKey.of(RegistryKeys.BLOCK, Welkin.id("yoinkable"));
    public static final TagKey<Block> UNACCELERATEABLE = TagKey.of(RegistryKeys.BLOCK, Welkin.id("unaccelerateable"));
    public static final TagKey<Block> CUT_COPPER = TagKey.of(RegistryKeys.BLOCK, Welkin.id("cut_copper"));
    public static final TagKey<Item> CUT_COPPER_ITEM_TAG = makeItemTag("cut_copper");
    public static final TagKey<Item> FRACTAL_LOGS = makeItemTag("fractal_logs");
    public static final TagKey<Item> CHARGED_LOGS = makeItemTag("charged_logs");
    public static final TagKey<Item> WATCHER_LOGS = makeItemTag("watcher_logs");

    public static TagKey<Block> makeBlockTag(String name){
        return TagKey.of(RegistryKeys.BLOCK, Welkin.id(name));
    }
    public static TagKey<Item> makeItemTag(String name){
        return TagKey.of(RegistryKeys.ITEM, Welkin.id(name));
    }

}
