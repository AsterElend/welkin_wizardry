package aster.welkin.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class SmiteRecipeSerializer implements RecipeSerializer<SmiteRecipe> {
    public static final SmiteRecipeSerializer INSTANCE = new SmiteRecipeSerializer();

    @Override
    public SmiteRecipe read(Identifier id, JsonObject json) {
        // 1. Parse input
        JsonObject inputObj = JsonHelper.getObject(json, "input");
        Ingredient input = Ingredient.fromJson(inputObj);

        // 2. Parse result item
        String resultString = JsonHelper.getString(json, "result");
        Identifier resultId = new Identifier(resultString);
        Item outputItem = Registries.ITEM.get(resultId);

        if (outputItem == Items.AIR) {
            throw new JsonSyntaxException("Invalid result item for SmiteRecipe: " + resultString);
        }

        // 3. Parse optional count (default = 1)
        int count = JsonHelper.getInt(json, "count", 1);

        ItemStack output = new ItemStack(outputItem, count);

        return new SmiteRecipe(id, input, output);
    }

    @Override
    public SmiteRecipe read(Identifier id, PacketByteBuf buf) {
        Ingredient input = Ingredient.fromPacket(buf);
        ItemStack output = buf.readItemStack();
        return new SmiteRecipe(id, input, output);
    }

    @Override
    public void write(PacketByteBuf buf, SmiteRecipe recipe) {
        recipe.getInput().write(buf);
        buf.writeItemStack(recipe.getOut());
    }
}
