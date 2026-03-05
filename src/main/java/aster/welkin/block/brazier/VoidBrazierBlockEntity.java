package aster.welkin.block.brazier;
import aster.welkin.registry.ModRecipes;
import aster.welkin.recipes.ForsakeRecipe;
import aster.welkin.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class VoidBrazierBlockEntity extends BlockEntity {
    private static final double RANGE = 0.2;

    public VoidBrazierBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BRAZIER, pos, state);
    }

    /** Server-side tick */
    public static void tick(World world, BlockPos pos, BlockState state, VoidBrazierBlockEntity be) {
        if (world.isClient) return;
        if (!state.get(VoidBrazierBlock.LIT)) return;

        // area around brazier
        Box box = new Box(pos).expand(RANGE);
        List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class, box, e -> true);

        if (items.isEmpty()) return;

        for (ItemEntity item : items) {
            ItemStack stack = item.getStack();

            // Prepare inventory with a single slot
            SimpleInventory inv = new SimpleInventory(1);
            inv.setStack(0, stack);

            // Attempt to match a transmutation recipe
            Optional<ForsakeRecipe> match =
                    world.getRecipeManager().getFirstMatch(
                            ModRecipes.FORSAKE_TYPE,
                            inv,
                            world
                    );

            if (match.isEmpty()) continue;

            ForsakeRecipe recipe = match.get();
            ItemStack result = recipe.getOutput(world.getRegistryManager()).copy();

            // Remove the input item
            item.discard();

            // Spawn the output
            ItemEntity output = new ItemEntity(
                    world,
                    pos.getX() + 0.5,
                    pos.getY() + 1.0,
                    pos.getZ() + 0.5,
                    result
            );

            output.setVelocity(0, 0.1, 0);
            world.spawnEntity(output);
        }
    }
}
