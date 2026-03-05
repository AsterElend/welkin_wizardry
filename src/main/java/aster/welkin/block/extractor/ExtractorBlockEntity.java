package aster.welkin.block.extractor;

import aster.welkin.recipes.ExtractorRecipe;
import aster.welkin.registry.ModBlockEntities;
import aster.welkin.registry.ModRecipes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class ExtractorBlockEntity extends BlockEntity {
    private ItemStack item = ItemStack.EMPTY;

    // track redstone power to detect edges
    protected int previousPower = 0;

    public ExtractorBlockEntity( BlockPos pos, BlockState state) {
        super(ModBlockEntities.EXTRACTOR, pos, state);
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack stack) {
        this.item = stack;

        markDirty();
        if (world != null && !world.isClient) {
            world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, ExtractorBlockEntity be) {
        if (world.isClient) return;

        int power = world.getReceivedRedstonePower(pos);

        // Trigger only when redstone power goes from 0 → >0
        if (power > 0 && be.previousPower == 0) {
            be.processRecipe();
        }

        be.previousPower = power;
    }

    private void processRecipe() {
        if (item.isEmpty() || world == null) return;

        RecipeManager recipeManager = world.getRecipeManager();

        // SimpleInventory only holds one slot: index 0
        SimpleInventory inv = new SimpleInventory(item);

        // Attempt to match your custom recipe
        Optional<ExtractorRecipe> match =
                recipeManager.getFirstMatch(ModRecipes.EXTRACT_TYPE, inv, world);

        if (match.isEmpty()) return;

        ExtractorRecipe recipe = match.get();
        ItemStack result = recipe.craft(inv, world.getRegistryManager());

        // Replace item with the crafted output
        setItem(result.copy());
    }


    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.put("Item", item.writeNbt(new NbtCompound()));
        nbt.putInt("PreviousPower", previousPower);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        item = ItemStack.fromNbt(nbt.getCompound("Item"));
        previousPower = nbt.getInt("PreviousPower");
    }
}
