package aster.welkin.registry;

import aster.welkin.recipes.ExtractorRecipe;
import aster.welkin.recipes.ForsakeRecipe;
import aster.welkin.recipes.LightningRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {

    public static RecipeType<ForsakeRecipe> FORSAKE_TYPE;
    public static RecipeSerializer<ForsakeRecipe> FORSAKE_SERIALIZER;
     public static RecipeType<LightningRecipe> LIGHTNING_TYPE;
    public static RecipeSerializer<LightningRecipe> LIGHTNING_SERIALIZER;

 public static RecipeType<ExtractorRecipe> EXTRACT_TYPE;
    public static RecipeSerializer<ExtractorRecipe> EXTRACT_SERIALIZER;



    public static void register() {
        //void brazier recipe
        FORSAKE_TYPE = Registry.register(
                Registries.RECIPE_TYPE,
                new Identifier("welkin", "forsake"),
                new RecipeType<ForsakeRecipe>() {
                    @Override
                    public String toString() {
                        return "welkin:forsake";
                    }
                }
        );

        FORSAKE_SERIALIZER = Registry.register(
                Registries.RECIPE_SERIALIZER,
                new Identifier("welkin", "forsake"),
                new ForsakeRecipe.ForsakeSerializer());

        //smite recipe

        LIGHTNING_TYPE = Registry.register(
                Registries.RECIPE_TYPE,
                new Identifier("welkin", "strike"),
                new RecipeType<LightningRecipe>() {
                    @Override
                    public String toString() {
                        return "welkin:forsake";
                    }
                }
        );

        LIGHTNING_SERIALIZER = Registry.register(
                Registries.RECIPE_SERIALIZER,
                new Identifier("welkin", "strike"),
                new LightningRecipe.LightningSerializer());

        //sky extraction recipe

        EXTRACT_TYPE = Registry.register(
                Registries.RECIPE_TYPE,
                new Identifier("welkin", "extract"),
                new RecipeType<ExtractorRecipe>() {
                    @Override
                    public String toString() {
                        return "welkin:extract";
                    }
                }
        );

        EXTRACT_SERIALIZER = Registry.register(
                Registries.RECIPE_SERIALIZER,
                new Identifier("welkin", "extract"),
                new ExtractorRecipe.ExtractorRecipeSerializer());
    }
}
