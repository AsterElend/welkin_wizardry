package aster.welkin.emi;

import aster.welkin.Welkin;
import aster.welkin.recipes.LightningRecipe;
import aster.welkin.registry.WelkinBlocks;
import aster.welkin.registry.WelkinRecipes;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;

public class WelkinEmiPlugin implements EmiPlugin {
    public static final EmiRecipeCategory LIGHTNING_CATEGORY = new EmiRecipeCategory(
            Welkin.id("lightning_transmutation"),
            EmiStack.of(WelkinBlocks.LIGHTNING_ALTAR)
    );


    @Override
    public void register(EmiRegistry emiRegistry) {
        emiRegistry.addCategory(LIGHTNING_CATEGORY);
        emiRegistry.addWorkstation(LIGHTNING_CATEGORY, EmiStack.of(WelkinBlocks.LIGHTNING_ALTAR));
        for (LightningRecipe recipe: emiRegistry.getRecipeManager().listAllOfType(WelkinRecipes.LIGHTNING_TYPE)){

        }
    }
}
