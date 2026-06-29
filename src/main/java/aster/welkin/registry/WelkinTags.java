package aster.welkin.registry;

import aster.welkin.Welkin;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class WelkinTags {

    public static final TagKey<Item> SHOW_FORCE = TagKey.of(RegistryKeys.ITEM, Identifier.of("welkin", "show_force"));
    public static final TagKey<Block> ABSCONDABLE = TagKey.of(RegistryKeys.BLOCK, Identifier.of("welkin", "abscondable"));
    public static final TagKey<Item> TAKE_HEART = TagKey.of(RegistryKeys.ITEM, Welkin.id("take_heart"));
    public static final TagKey<Fluid> LETHE = TagKey.of(RegistryKeys.FLUID, Welkin.id("lethe"));
}
