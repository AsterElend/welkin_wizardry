package aster.welkin.recipes;

import aster.welkin.registry.ModRecipes;
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

public class ForsakeRecipe implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final Ingredient ingredient;
    private final ItemStack result;

    public ForsakeRecipe(Identifier id, Ingredient ingredient, ItemStack result) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public boolean matches(SimpleInventory inv, World world) {
        return ingredient.test(inv.getStack(0));
    }


    @Override
    public ItemStack craft(SimpleInventory inv, DynamicRegistryManager manager) {
        return result.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return result.copy();
    }


    @Override
    public Identifier getId() {
        return id;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FORSAKE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.FORSAKE_TYPE;
    }


    public static class ForsakeSerializer implements RecipeSerializer<ForsakeRecipe>{
        @Override
         public ForsakeRecipe read(Identifier id, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));

            JsonObject resultObj = json.getAsJsonObject("result");
            Identifier resultId = new Identifier(resultObj.get("item").getAsString());
            Item resultItem = Registries.ITEM.get(resultId);

            int count = resultObj.has("count") ? resultObj.get("count").getAsInt() : 1;

            return new ForsakeRecipe(id, ingredient, new ItemStack(resultItem, count));
        }

        @Override
        public ForsakeRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient ingredient = Ingredient.fromPacket(buf);
            ItemStack result = buf.readItemStack();
            return new ForsakeRecipe(id, ingredient, result);
        }

        @Override
        public void write(PacketByteBuf buf, ForsakeRecipe recipe) {
            recipe.getIngredient().write(buf);
            buf.writeItemStack(recipe.getOutput(null));
        }
}

}
