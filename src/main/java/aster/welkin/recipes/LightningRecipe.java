package aster.welkin.recipes;

import aster.welkin.registry.WelkinRecipes;
import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class LightningRecipe extends ItemEntityTransmutationRecipe {
    public LightningRecipe(Identifier id, Ingredient ingredient, ItemStack result) {
        super(id, ingredient, result);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return WelkinRecipes.LIGHTNING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType(){
        return WelkinRecipes.LIGHTNING_TYPE;
    }
}
