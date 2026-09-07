package aster.welkin.api;

import aster.welkin.recipes.ItemEntityTransmutationRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

@FunctionalInterface
public interface ItemTransmutationFactory<T extends ItemEntityTransmutationRecipe> {
    T create(Identifier id, Ingredient ingredient, ItemStack result);
}
