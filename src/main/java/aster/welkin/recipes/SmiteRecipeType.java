package aster.welkin.recipes;

import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

public class SmiteRecipeType implements RecipeType<SmiteRecipe> {
    public static final SmiteRecipeType INSTANCE = new SmiteRecipeType();
    public static final Identifier ID = new Identifier("welkin", "smite");

    @Override
    public String toString() {
        return ID.toString();
    }
}
