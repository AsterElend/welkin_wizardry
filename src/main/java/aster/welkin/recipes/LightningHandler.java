package aster.welkin.recipes;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class LightningHandler {
    public static void handleSmite(World world, BlockPos pos) {


        if (world.isClient) return;

        List<ItemEntity> items = world.getEntitiesByClass(
                ItemEntity.class,
                new Box(pos).expand(2.5),
                ie -> !ie.getStack().isEmpty()
        );

        for (ItemEntity itemEntity : items) {
            itemEntity.setInvulnerable(true);
            itemEntity.setFireTicks(0);
            ItemStack stack = itemEntity.getStack();
            stack.getOrCreateNbt().putBoolean("LightningProt", true);


            SimpleInventory inv = new SimpleInventory(stack.copy());

            Optional<SmiteRecipe> match =
                    world.getRecipeManager().getFirstMatch(
                            SmiteRecipeType.INSTANCE, inv, world);

            if (match.isEmpty())
                continue;

            SmiteRecipe recipe = match.get();

            // ----- Consume 1 item safely -----
            ItemStack newStack = stack.copy();
            newStack.decrement(1);

            if (newStack.isEmpty()) {
                itemEntity.discard();
            } else {
                itemEntity.setStack(newStack);    // <-- CRITICAL FIX
            }

            // ----- Spawn the result -----
            ItemStack result = recipe.getOutput(world.getRegistryManager());
            ItemEntity out = new ItemEntity(
                    world,
                    itemEntity.getX(),
                    itemEntity.getY(),
                    itemEntity.getZ(),
                    result
            );
            world.spawnEntity(out);
        }
    }










}