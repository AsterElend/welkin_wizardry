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

public class AgoniteTransmutationRecipe implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final Ingredient input;
    private final float damage;
    private final ItemStack result;

    public AgoniteTransmutationRecipe(Identifier id, Ingredient input, float damage, ItemStack output) {
        this.id = id;
        this.input = input;
        this.damage = damage;
        this.result = output;
    }

    public ItemStack getBaseOutput() {
        return result.copy();
    }
    public float getPain(){
        return damage;
    }

    @Override
    public boolean matches(SimpleInventory inv, World world) {
        return input.test(inv.getStack(0));
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
        return input;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.AGONY_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.AGONY_TYPE;
    }


    public static class AgonySerializer implements RecipeSerializer<AgoniteTransmutationRecipe>{
        @Override
        public AgoniteTransmutationRecipe read(Identifier id, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            float requiredPain = json.get("pain").getAsInt();
            JsonObject resultObj = json.getAsJsonObject("result");
            Identifier resultId = new Identifier(resultObj.get("item").getAsString());
            Item resultItem = Registries.ITEM.get(resultId);

            int count = resultObj.has("count") ? resultObj.get("count").getAsInt() : 1;

            return new AgoniteTransmutationRecipe(id, ingredient, requiredPain, new ItemStack(resultItem, count));
        }

        @Override
        public AgoniteTransmutationRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient ingredient = Ingredient.fromPacket(buf);
            float damage = buf.readFloat();
            ItemStack result = buf.readItemStack();
            return new AgoniteTransmutationRecipe(id, ingredient, damage, result);
        }

        @Override
        public void write(PacketByteBuf buf, AgoniteTransmutationRecipe recipe) {
            recipe.getIngredient().write(buf);
            buf.writeFloat(recipe.damage);
            buf.writeItemStack(recipe.getOutput(null));
        }
    }
}
