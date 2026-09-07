package aster.welkin.recipes;

import aster.welkin.registry.WelkinRecipes;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.common.multiblock.SerializedMultiblock;
public class StormEyeRecipe implements CraftingRecipe {

    // 1. Remove 'final' from multiblock so we can load it later
    private final SerializedMultiblock serializedMultiblock;
    private IMultiblock multiblock;

    private final ItemStack output;
    private final BlockState outputState;
    private final BlockPos offset;
    private final BlockPos outputOffset;
    private final Identifier recipeId;

    // 2. Fixed constructor: removed the crashing toMultiblock() call and the unused JsonObject argument
    public StormEyeRecipe(
            SerializedMultiblock serializedMultiblock,
            @Nullable ItemStack output,
            @Nullable BlockState state,
            BlockPos offset,
            @Nullable BlockPos outputOffset,
            Identifier recipeId
    ) {
        this.serializedMultiblock = serializedMultiblock;
        this.output = output;
        this.outputState = state;
        this.offset = offset;
        this.outputOffset = outputOffset;
        this.recipeId = recipeId;
    }

    @Override
    public RecipeType<?> getType() {
        return WelkinRecipes.STORM_EYE_TYPE;
    }

    public boolean hasItemOutput(){return output != null;}
    public boolean hasBlockOutput(){return outputState != null;}

    // 3. Lazy initialize the multiblock here
    public IMultiblock getMultiblock() {
        if (this.multiblock == null) {
            this.multiblock = this.serializedMultiblock.toMultiblock();
        }
        return this.multiblock;
    }

    public ItemStack getOutput() { return output; }
    public BlockState getOutputState() { return outputState; }
    public BlockPos getOffset() { return offset; }
    public BlockPos getOutputOffset() { return outputOffset; }

    // 4. Update these methods to use the lazy getMultiblock() getter
    public BlockRotation testMulti(BlockPos pos, World world) {
        return getMultiblock().validate(world, pos);
    }

    public Vec3i getSize() {
        return getMultiblock().getSize();
    }

    @Override
    public CraftingRecipeCategory getCategory() { return CraftingRecipeCategory.MISC; }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) { return false; }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) { return true; }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return output != null ? output.copy() : ItemStack.EMPTY;
    }

    @Override
    public Identifier getId() { return recipeId; }

    @Override
    public RecipeSerializer<?> getSerializer() { return WelkinRecipes.STORM_EYE_SERIALIZER; }

    public static class Serializer implements RecipeSerializer<StormEyeRecipe> {

        private static final Gson GSON = new Gson();

        @Override
        public StormEyeRecipe read(Identifier id, JsonObject json) {
            JsonObject multiDefinition = json.getAsJsonObject("multiblock");
            SerializedMultiblock serializedMultiblock = GSON.fromJson(multiDefinition, SerializedMultiblock.class);

            ItemStack output = json.has("output")
                    ? ShapedRecipe.outputFromJson(json.getAsJsonObject("output"))
                    : null;
            BlockState state = json.has("outputState")
                    ? Registries.BLOCK.get(new Identifier(json.get("outputState").getAsString())).getDefaultState()
                    : null;
            BlockPos offset = json.has("offset") ? parseOffset(json.getAsJsonObject("offset")) : BlockPos.ORIGIN;
            BlockPos outputOffset = json.has("outputOffset") ? parseOffset(json.getAsJsonObject("outputOffset")) : null;

            return new StormEyeRecipe(serializedMultiblock, output, state, offset, outputOffset, id);
        }

        @Override
        public StormEyeRecipe read(Identifier id, PacketByteBuf buf) {
            SerializedMultiblock serializedMultiblock = GSON.fromJson(buf.readString(), SerializedMultiblock.class);

            ItemStack output = buf.readBoolean() ? buf.readItemStack() : null;
            BlockState state = buf.readBoolean() ? Block.getStateFromRawId(buf.readVarInt()) : null;
            BlockPos offset = buf.readBlockPos();
            BlockPos outputOffset = buf.readBoolean() ? buf.readBlockPos() : null;

            return new StormEyeRecipe(serializedMultiblock, output, state, offset, outputOffset, id);
        }

        @Override
        public void write(PacketByteBuf buf, StormEyeRecipe recipe) {
            buf.writeString(GSON.toJson(recipe.serializedMultiblock));

            buf.writeBoolean(recipe.output != null);
            if (recipe.output != null) buf.writeItemStack(recipe.output);

            buf.writeBoolean(recipe.outputState != null);
            if (recipe.outputState != null) buf.writeVarInt(Block.getRawIdFromState(recipe.outputState));

            buf.writeBlockPos(recipe.offset);

            buf.writeBoolean(recipe.outputOffset != null);
            if (recipe.outputOffset != null) buf.writeBlockPos(recipe.outputOffset);
        }

        public BlockPos parseOffset(JsonObject json) {
            int x = json.has("x") ? json.get("x").getAsInt() : 0;
            int y = json.has("y") ? json.get("y").getAsInt() : 0;
            int z = json.has("z") ? json.get("z").getAsInt() : 0;
            return new BlockPos(x, y, z);
        }
    }
}
