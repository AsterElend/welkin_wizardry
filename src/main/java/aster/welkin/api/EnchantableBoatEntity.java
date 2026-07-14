package aster.welkin.api;

import net.minecraft.enchantment.Enchantment;

import java.util.Map;

public interface EnchantableBoatEntity {
    Map<Enchantment, Integer> welkin$getBoatEnchantments();

    void welkin$setBoatEnchantments(Map<Enchantment, Integer> enchantments);

    default int welkin$getLevelOfBoatEnchant(Enchantment enchantment) {
        return welkin$getBoatEnchantments().getOrDefault(enchantment, 0);
    }

}
