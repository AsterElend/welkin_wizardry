package aster.welkin.emi;

import aster.welkin.Welkin;
import aster.welkin.recipes.AlchemyRecipe;
import aster.welkin.recipes.LightningRecipe;
import aster.welkin.recipes.StormEyeRecipe;
import aster.welkin.registry.WelkinBlocks;
import aster.welkin.registry.WelkinRecipes;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.item.ItemConvertible;

public class WelkinEmiPlugin implements EmiPlugin {
    public static final EmiRecipeCategory LIGHTNING_CATEGORY = registerCategory("lightning_transmutation", WelkinBlocks.LIGHTNING_ALTAR);

    public static final EmiRecipeCategory ALCHEMY_CATEGORY = registerCategory("alchemy", WelkinBlocks.ALCHEMICAL_ENGINE);

    public static final EmiRecipeCategory STORM_EYE_CATEGORY = registerCategory("storm_eye", WelkinBlocks.STORM_EYE);


    @Override
    public void register(EmiRegistry emiRegistry) {
        emiRegistry.addCategory(LIGHTNING_CATEGORY);
        emiRegistry.addWorkstation(LIGHTNING_CATEGORY, EmiStack.of(WelkinBlocks.LIGHTNING_ALTAR));
        for (LightningRecipe recipe: emiRegistry.getRecipeManager().listAllOfType(WelkinRecipes.LIGHTNING_TYPE)){
            emiRegistry.addRecipe(new EmiLightningRecipe(recipe));
        }

        emiRegistry.addCategory(ALCHEMY_CATEGORY);
        emiRegistry.addWorkstation(ALCHEMY_CATEGORY, EmiStack.of(WelkinBlocks.ALCHEMICAL_ENGINE));
        for (AlchemyRecipe recipe: emiRegistry.getRecipeManager().listAllOfType(WelkinRecipes.ALCHEMY_TYPE)){
            emiRegistry.addRecipe(new EmiAlchemyRecipe(recipe));
        }

        emiRegistry.addCategory(STORM_EYE_CATEGORY);
        emiRegistry.addWorkstation(STORM_EYE_CATEGORY, EmiStack.of(WelkinBlocks.STORM_EYE));
        for (StormEyeRecipe recipe: emiRegistry.getRecipeManager().listAllOfType(WelkinRecipes.STORM_EYE_TYPE)){
            emiRegistry.addRecipe(new EmiStormEyeRecipe(recipe));
        }

    }

    public static EmiRecipeCategory registerCategory(String name, ItemConvertible convertible){
        return new EmiRecipeCategory(Welkin.id(name), EmiStack.of(convertible));
    }
}
