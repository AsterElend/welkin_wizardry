package aster.welkin.recipes;

import aster.welkin.block.entity.TeapotBlockEntity;
import aster.welkin.registry.WelkinRecipes;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class TeapotRecipe implements Recipe<TeapotBlockEntity> {
    private final Identifier id;
    private final FluidVariant fluidInput;
    private final int fluidInputAmount;
    private final Ingredient itemInput;
    private final ItemStack itemOutput;
    private final FluidVariant fluidOutput;
    private final int fluidOutputAmount;

    public TeapotRecipe(Identifier id, FluidVariant fluid, int fluidInputAmount, Ingredient itemInput, ItemStack output, FluidVariant fluidOutput, int fluidOutputAmount) {
        this.id = id;
        this.fluidInput = fluid;
        this.fluidInputAmount = fluidInputAmount;
        this.itemInput = itemInput;
        this.itemOutput = output;
        this.fluidOutput = fluidOutput;
        this.fluidOutputAmount = fluidOutputAmount;
    }


    @Override
    public boolean matches(TeapotBlockEntity entity, World world) {
        if (!itemInput.test(entity.getStoredItem())) return false;
        FluidVariant storedFluid = entity.getInputStoredFluid();
        long storedamount =  entity.getInputFluidAmount();

        return storedFluid.equals(fluidInput) && storedamount >= fluidInputAmount;

    }

    @Override
    public ItemStack craft(TeapotBlockEntity entity, DynamicRegistryManager registryManager) {
        return itemOutput.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return itemOutput.copy();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return WelkinRecipes.TEAPOT_TYPE;
    }

    public static class TeapotRecipeSerializer implements RecipeSerializer<TeapotRecipe> {




        @Override
        public TeapotRecipe read(Identifier id, JsonObject json) {
            // Item input — standard Ingredient
            Ingredient itemInput = Ingredient.fromJson(json.get("item_input"));

            // Fluid input — manual deserialization
            JsonObject fluidJson = json.getAsJsonObject("fluid_input");
            Identifier fluidId = new Identifier(fluidJson.get("fluid").getAsString());
            Fluid fluid = Registries.FLUID.get(fluidId);
            NbtCompound nbt = null;
            try {
                nbt = fluidJson.has("nbt")
                        ? NbtHelper.fromNbtProviderString(fluidJson.get("nbt").getAsString())
                        : null;
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }

            FluidVariant fluidVariant = FluidVariant.of(fluid, nbt);
            int amount = fluidJson.get("amount").getAsInt();

            // Output
            ItemStack result = ShapedRecipe.outputFromJson(json.getAsJsonObject("result"));

            JsonObject fluidOutputJson = json.getAsJsonObject("fluid_output");
            Identifier fluidOutputId = new Identifier(fluidOutputJson.get("fluid").getAsString());
            Fluid outputFluid = Registries.FLUID.get(fluidOutputId);
            NbtCompound fluidOutput = null;
            try {
                fluidOutput = fluidJson.has("nbt")
                        ? NbtHelper.fromNbtProviderString(fluidJson.get("nbt").getAsString())
                        : null;
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
            FluidVariant outputVariant = FluidVariant.of(outputFluid, fluidOutput);
            int outputQuantity = fluidOutputJson.get("amount").getAsInt();

            return new TeapotRecipe(id,  fluidVariant, amount, itemInput, result, outputVariant, outputQuantity);
        }

        @Override
        public TeapotRecipe read(Identifier id, PacketByteBuf buf) {
          FluidVariant variant = FluidVariant.fromPacket(buf);
          int fluidInputQuantity = buf.readInt();
          Ingredient itemIntput = Ingredient.fromPacket(buf);
          ItemStack outputStack = buf.readItemStack();
          FluidVariant outputFluid = FluidVariant.fromPacket(buf);
          int fluidOutputQuantity = buf.readInt();

          return new TeapotRecipe(id, variant, fluidInputQuantity, itemIntput, outputStack, outputFluid, fluidOutputQuantity);
        }

        @Override
        public void write(PacketByteBuf buf, TeapotRecipe recipe) {
            recipe.fluidInput.toPacket(buf);
            buf.writeInt(recipe.fluidInputAmount);
           recipe.itemInput.write(buf);
           buf.writeItemStack(recipe.itemOutput);
           recipe.fluidOutput.toPacket(buf);
           buf.writeInt(recipe.fluidOutputAmount);


        }
    }


}
