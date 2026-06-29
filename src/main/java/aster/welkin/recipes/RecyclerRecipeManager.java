package aster.welkin.recipes;

import aster.welkin.Welkin;
import aster.welkin.jsonstuff.RecyclerReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RecyclerRecipeManager {

    public static final List<RecyclerRecipe> RECYCLER_RECIPES = new ArrayList<>();
    public List<ItemStack> getAllOutputs(){
        List<ItemStack> list = new ArrayList<>();
        for (RecyclerRecipe recipe: RECYCLER_RECIPES){
            list.add(recipe.getOutput());
        }
        return list;
    }


    public static void generateRecipes(long worldSeed) {
        RECYCLER_RECIPES.clear();
        Registry<Item> itemRegistry = Registries.ITEM;

        for (RecyclerReloadListener.RecycleSet set : RecyclerReloadListener.getTagsToAlchemize()) {

            // 1. Collect all items in the main tag
            List<Item> pool = new ArrayList<>();
            for (Item item : itemRegistry) {
                if (itemRegistry.getEntry(item).isIn(set.mainTag())) {
                    pool.add(item);
                }
            }

            // 2. Build excluded set
            Set<Identifier> excluded = new HashSet<>();
            for (TagKey<Item> subtractedTag : set.subtractedTags()) {
                for (Item item : itemRegistry) {
                    if (itemRegistry.getEntry(item).isIn(subtractedTag)) {
                        excluded.add(itemRegistry.getId(item));
                    }
                }
            }
            excluded.addAll(set.subtractedItems());

            // 3. Filter pool
            pool.removeIf(item -> excluded.contains(itemRegistry.getId(item)));

            // 4. Per output item, pick comboLength distinct ingredients at random
            for (Item output : pool) {
                Identifier outputId = itemRegistry.getId(output);

                // Ingredient candidates: pool minus this output item
                List<Item> candidates = pool.stream()
                        .filter(i -> !itemRegistry.getId(i).equals(outputId))
                        .collect(Collectors.toCollection(ArrayList::new));

                // Need enough candidates to fill comboLength slots without repeats
                if (candidates.size() < set.comboLength()) {
                    Welkin.LOGGER.warn(
                            "RecyclerRecipeManager: not enough candidates to fill {} ingredient slots for output {} — only {} available, skipping",
                            set.comboLength(), outputId, candidates.size()
                    );
                    continue;
                }

                // Seed per-recipe so each output gets a stable independent shuffle,
                // regardless of iteration order of other outputs
                long recipeSpecificSeed = worldSeed ^ outputId.hashCode();
                Random recipeRandom = Random.create(recipeSpecificSeed);

                // Fisher-Yates partial shuffle to pick comboLength items without repeats
                List<Item> shuffled = new ArrayList<>(candidates);
                for (int i = 0; i < set.comboLength(); i++) {
                    int swapTarget = i + recipeRandom.nextInt(shuffled.size() - i);
                    Item tmp = shuffled.get(i);
                    shuffled.set(i, shuffled.get(swapTarget));
                    shuffled.set(swapTarget, tmp);
                }
                List<Item> chosenItems = shuffled.subList(0, set.comboLength());

                // One Ingredient per chosen item (exact match)
                List<Ingredient> ingredientList = chosenItems.stream()
                        .map(Ingredient::ofItems)
                        .collect(Collectors.toList());

                Identifier recipeId = Welkin.id(
                        "/recycle/" + outputId.getNamespace() + "/" + outputId.getPath()
                );

                RECYCLER_RECIPES.add(new RecyclerRecipe(
                        recipeId,
                        ingredientList,
                        new ItemStack(output)
                ));


            }
        }
    }
}