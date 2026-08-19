package aster.welkin.emi;

import aster.welkin.recipes.AlchemyRecipe;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.recipe.Ingredient;

public class EmiAlchemyRecipe extends BasicEmiRecipe {
    public EmiAlchemyRecipe(AlchemyRecipe recipe) {
        super(WelkinEmiPlugin.ALCHEMY_CATEGORY, recipe.getId(), 128, 64);
        this.inputs.add(EmiIngredient.of(Ingredient.ofItems(recipe.getInput())));
        if (recipe.hasCatalyst()){
            this.catalysts.add(EmiIngredient.of(Ingredient.ofItems(recipe.getCatalyst())));
        }

        this.outputs.add(EmiStack.of(recipe.getOutput()));
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        widgetHolder.addTexture(EmiTexture.FULL_ARROW, 26, 1);
        widgetHolder.addSlot(inputs.get(0), 0, 0);
        widgetHolder.addSlot(outputs.get(0), 58, 0).recipeContext(this);
        if (!this.catalysts.isEmpty()){
            widgetHolder.addSlot(catalysts.get(0), 16, 25);
        }

    }
}
