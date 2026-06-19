package aster.welkin.emi;

import aster.welkin.Welkin;
import aster.welkin.recipes.LightningRecipe;
import aster.welkin.recipes.RecyclerRecipe;
import aster.welkin.recipes.RecyclerRecipeManager;
import aster.welkin.registry.ModBlocks;
import aster.welkin.registry.ModRecipes;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.recipe.RecipeManager;

public class WelkinEmiPlugin implements EmiPlugin {
    public static final EmiRecipeCategory LIGHTNING_CATEGORY = new EmiRecipeCategory(
            Welkin.id("lightning_transmutation"),
            EmiStack.of(ModBlocks.LIGHTNING_ALTAR)
    );


    @Override
    public void register(EmiRegistry emiRegistry) {
        emiRegistry.addCategory(LIGHTNING_CATEGORY);
        emiRegistry.addWorkstation(LIGHTNING_CATEGORY, EmiStack.of(ModBlocks.LIGHTNING_ALTAR));
        for (LightningRecipe recipe: emiRegistry.getRecipeManager().listAllOfType(ModRecipes.LIGHTNING_TYPE)){

        }
    }
}
