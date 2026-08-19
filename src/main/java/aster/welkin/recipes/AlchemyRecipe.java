package aster.welkin.recipes;

import aster.welkin.registry.WelkinRecipes;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AlchemyRecipe implements Recipe<Inventory> {
    private final Identifier id;
    private final Item input;
    @Nullable private final Block catalyst;
    private final Item result;
    public AlchemyRecipe(Identifier id, Item input, @Nullable Block catalyst, Item result) {
        this.id = id;
        this.input = input;
        this.catalyst = catalyst;
        this.result = result;
    }

    public boolean hasCatalyst(){return catalyst != null;}
    public Item getInput(){return input;}

    public @Nullable Block getCatalyst(){return catalyst;}
    @Override
    public boolean matches(Inventory inventory, World world) {
        return input == (inventory.getStack(0).getItem());
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return new ItemStack(result);
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return new ItemStack(result);
    }


    public Item getOutput() {
        return result;
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
        return WelkinRecipes.ALCHEMY_TYPE;
    }



    public static final RecipeSerializer<AlchemyRecipe> DUMMY_SERIALIZER = new RecipeSerializer<>() {
        @Override
        public AlchemyRecipe read(Identifier id, JsonObject json) { return null; }

        @Override
        public AlchemyRecipe read(Identifier id, PacketByteBuf buf) { return null; }

        @Override
        public void write(PacketByteBuf buf, AlchemyRecipe recipe) {}
    };



}