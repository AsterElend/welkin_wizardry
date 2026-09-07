package aster.welkin.recipes;

import aster.welkin.api.ItemTransmutationFactory;
import com.google.gson.JsonObject;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class ItemEntityTransmutationRecipe implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final Ingredient ingredient;
    private final ItemStack result;

    public ItemEntityTransmutationRecipe(Identifier id, Ingredient ingredient, ItemStack result) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
    }

    public ItemStack getBaseOutput() {
        return result.copy();
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

    public <T extends ItemEntityTransmutationRecipe> ItemEntity transmuteAStack(ItemEntity in, ServerWorld world, ParticleEffect particle){
        int count = in.getStack().getCount();
        ItemStack base = this.getBaseOutput();
        ItemStack result = new ItemStack(base.getItem(), base.getCount() * count);


        ItemEntity output = new ItemEntity(
                world,
                in.getX(),
                in.getY(),
                in.getZ(),
                result
        );

        in.discard();
        world.spawnParticles(particle, in.getX(), in.getY(), in.getZ(), 6, 1, 1, 1,1 );
        world.spawnEntity(output);
        return output;
    }

    public static class Serializer<T extends ItemEntityTransmutationRecipe> implements RecipeSerializer<T> {
        private final ItemTransmutationFactory<T> factory;

        // Pass the specific subclass factory here
        public Serializer(ItemTransmutationFactory<T> factory) {
            this.factory = factory;
        }

        @Override
        public T read(Identifier id, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));

            JsonObject resultObj = json.getAsJsonObject("result");
            Identifier resultId = new Identifier(resultObj.get("item").getAsString());
            Item resultItem = Registries.ITEM.get(resultId);

            int count = resultObj.has("count") ? resultObj.get("count").getAsInt() : 1;

            // Use the factory to create the specific sub-class variant
            return this.factory.create(id, ingredient, new ItemStack(resultItem, count));
        }

        @Override
        public T read(Identifier id, PacketByteBuf buf) {
            Ingredient ingredient = Ingredient.fromPacket(buf);
            ItemStack result = buf.readItemStack();
            return this.factory.create(id, ingredient, result);
        }

        @Override
        public void write(PacketByteBuf buf, T recipe) {
            // Because T extends ItemEntityTransmutationRecipe,
            // you have direct access to all its methods safely!
            recipe.getIngredient().write(buf);
            buf.writeItemStack(recipe.getOutput(null));
        }
    }

}
