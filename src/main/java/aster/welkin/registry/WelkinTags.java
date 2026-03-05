package aster.welkin.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class WelkinTags {

    public static final TagKey<Item> SHOW_FORCE = TagKey.of(RegistryKeys.ITEM, Identifier.of("welkin", "show_force"));
    public static final TagKey<Block> ABSCONDABLE = TagKey.of(RegistryKeys.BLOCK, Identifier.of("welkin", "abscondable"));
}
