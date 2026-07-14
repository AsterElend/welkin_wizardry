package aster.welkin.api;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExtraEnchantsHolder {
    private static final Map<Enchantment, Set<TagKey<Item>>> EXTRA_ACCEPTABLE_TAGS = new HashMap<>();

    public static Set<TagKey<Item>> getTagsForAnEnchantment(Enchantment ench){
        return EXTRA_ACCEPTABLE_TAGS.get(ench);
    }

    public static void registerExtraTag(Enchantment enchantment, TagKey<Item> tag) {
        EXTRA_ACCEPTABLE_TAGS.computeIfAbsent(enchantment, e -> new HashSet<>()).add(tag);
    }
}
