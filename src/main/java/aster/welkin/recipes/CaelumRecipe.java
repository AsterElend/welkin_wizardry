package aster.welkin.recipes;

import aster.welkin.registry.WelkinRecipes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

public class CaelumRecipe extends ItemEntityTransmutationRecipe {
    public CaelumRecipe(Identifier id, Ingredient ingredient, ItemStack result) {
        super(id, ingredient, result);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return WelkinRecipes.CAELUM_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return WelkinRecipes.CAELUM_TYPE;
    }
}
