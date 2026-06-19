package aster.welkin.recipes;

import aster.welkin.registry.ModRecipes;
import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

public class ExtractorRecipe implements Recipe<Inventory> {
    private final Identifier id;
    private final Ingredient input;
    private final ItemStack output;

    public ExtractorRecipe(Identifier id, Ingredient input, ItemStack output) {
        this.id = id;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        return input.test(inv.getStack(0));
    }

    @Override
    public ItemStack craft(Inventory inv, DynamicRegistryManager manager) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return output.copy();
    }

    @Override
    public Identifier getId() { return id; }

    @Override
    public RecipeSerializer<?> getSerializer() { return ModRecipes.EXTRACT_SERIALIZER; }

    @Override
    public RecipeType<?> getType() { return ModRecipes.EXTRACT_TYPE; }


    public static class ExtractorRecipeSerializer implements RecipeSerializer<ExtractorRecipe>{
        @Override
        public ExtractorRecipe read(Identifier id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(json.get("input"));
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));
            return new ExtractorRecipe(id, input, output);
        }

        @Override
        public ExtractorRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient input = Ingredient.fromPacket(buf);
            ItemStack output = buf.readItemStack();
            return new ExtractorRecipe(id, input, output);
        }

        @Override
        public void write(PacketByteBuf buf, ExtractorRecipe recipe) {
            recipe.input.write(buf);
            buf.writeItemStack(recipe.output);
        }
    }
}
