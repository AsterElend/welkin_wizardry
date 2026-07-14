package aster.welkin.recipes;

import aster.welkin.registry.WelkinRecipes;
import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecyclerRecipe implements Recipe<Inventory> {
    private final Identifier id;
    private final List<Ingredient> ingredients;
    private final ItemStack result;

    public RecyclerRecipe(Identifier id, List<Ingredient> ingredients, ItemStack result) {
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        // Collect all non-empty stacks from the inventory
        List<ItemStack> inventoryStacks = new ArrayList<>();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                inventoryStacks.add(stack);
            }
        }

        // Quick size check — inventory must have at least as many stacks as ingredients
        if (inventoryStacks.size() < ingredients.size()) {
            return false;
        }

        // For each ingredient, find a matching stack in the inventory (shapeless)
        List<ItemStack> remaining = new ArrayList<>(inventoryStacks);
        for (Ingredient ingredient : ingredients) {
            boolean found = false;
            Iterator<ItemStack> iter = remaining.iterator();
            while (iter.hasNext()) {
                if (ingredient.test(iter.next())) {
                    iter.remove();
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }

        return true;
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return result.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        // Shapeless; the inventory is flat (up to 16 slots), so width/height aren't meaningful.
        // Return true as long as the total slot count can hold all ingredients.
        return width * height >= ingredients.size();
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return null;
    }


    public ItemStack getOutput() {
        return result.copy();
    }



    @Override
    public Identifier getId() {
        return id; // return the instance field, not a hardcoded value
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        // You mentioned bypassing the serializer — return null or a no-op sentinel as needed.
        return DUMMY_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return WelkinRecipes.RECYCLE_TYPE;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.addAll(ingredients);
        return list;
    }


    public static final RecipeSerializer<RecyclerRecipe> DUMMY_SERIALIZER = new RecipeSerializer<>() {
        @Override
        public RecyclerRecipe read(Identifier id, JsonObject json) { return null; }

        @Override
        public RecyclerRecipe read(Identifier id, PacketByteBuf buf) { return null; }

        @Override
        public void write(PacketByteBuf buf, RecyclerRecipe recipe) {}
    };


}