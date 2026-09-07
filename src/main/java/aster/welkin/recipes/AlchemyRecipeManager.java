package aster.welkin.recipes;

import aster.welkin.Welkin;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

import java.util.*;

public class AlchemyRecipeManager {
    static long worldSeed = 0L;
    public static long getSeed(){
        return worldSeed;
    }
    public static void setSeed(long seed){
        worldSeed = seed;
    }
    private static final Map<Identifier, Recipe<?>> RECYCLER_RECIPES = new HashMap<>();
    public static Map<Identifier, Recipe<?>> getAlchemyRecipes() { return Collections.unmodifiableMap(RECYCLER_RECIPES); }
    // Separate maps eliminate the Pair bug and avoid allocating objects during lookup
    private static final Map<Item, AlchemyRecipe> BASE_RECIPES = new HashMap<>();
    private static final Map<Item, AlchemyRecipe> CATALYST_RECIPES = new HashMap<>();

    public static boolean hasRecipeOfItem(Item item) {
        return BASE_RECIPES.containsKey(item);
    }

    public static boolean hasACatalystRecipe(Item item) {
        return CATALYST_RECIPES.containsKey(item);
    }

    public static AlchemyRecipe getRecipe(Item item, boolean withCatalyst) {
        return withCatalyst ? CATALYST_RECIPES.get(item) : BASE_RECIPES.get(item);
    }



    public static Map<Item, AlchemyRecipe> getBaseRecipes() {
        return Collections.unmodifiableMap(BASE_RECIPES);
    }

    public static Map<Item, AlchemyRecipe> getCatalystRecipes() {
        return Collections.unmodifiableMap(CATALYST_RECIPES);
    }

    public static void generateRecipes() {
        Random random = Random.create(worldSeed);

        RECYCLER_RECIPES.clear();
        BASE_RECIPES.clear();
        CATALYST_RECIPES.clear();

        Registry<Item> itemRegistry = Registries.ITEM;

        for (AlchemyReloadListener.RecycleSet set : AlchemyReloadListener.getTagsToAlchemize()) {
            Welkin.LOGGER.info("Found an alchemy set for {}, trying to build loops", set.mainTag().id());

            // 1. Collect all items in the main tag
            List<Item> pool = new ArrayList<>();
            var entryListOpt = Registries.ITEM.getEntryList(set.mainTag());

            if (entryListOpt.isEmpty()) {
                Welkin.LOGGER.warn("Tag {} was not found on Registries.ITEM at all", set.mainTag().id());
                continue;
            } else {
                for (RegistryEntry<Item> item : entryListOpt.get()) {
                    pool.add(item.value());
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

            int loopLength = set.loopLength();
            Block catalyst = set.catalyst();

            // --- Chain-of-loops generation ---
            int chainIndex = 0;
            Item veryVeryFirst;
            Item bridgeSourceFromPrevLoop = null;
            String baseName = set.mainTag().id().getPath().replace('/', '_');

            while (!pool.isEmpty()) {
                // Always start a brand new circle with a fresh item from the pool
                Item firstCache = pickRandom(pool, random);

                List<Item> loopItems = new ArrayList<>();
                loopItems.add(firstCache);
                veryVeryFirst = firstCache;
                Item prevCache = firstCache;
                int recipesMade = 0;

                // Build standard chain links until loopLength reached or pool runs dry
                while (recipesMade < loopLength && !pool.isEmpty()) {
                    Item nextCache = pickRandom(pool, random);
                    loopItems.add(nextCache);

                    Identifier id = idFor(baseName, chainIndex, recipesMade);
                    addRecipe(id, prevCache, null, nextCache);

                    prevCache = nextCache;
                    recipesMade++;
                }

                // Close the loop back to the first item (no catalyst)
                if (recipesMade > 0) {
                    Identifier closeId = idFor(baseName, chainIndex, recipesMade);
                    addRecipe(closeId, prevCache, null, firstCache);
                }

                // BRIDGE LOGIC: Connect the previous circle to this new circle
                if (chainIndex > 0 && bridgeSourceFromPrevLoop != null) {
                    Identifier bridgeId = new Identifier(Welkin.MOD_ID, "alchemy/" + baseName + "/bridge_" + (chainIndex - 1) + "_to_" + chainIndex);
                    // This catalyst recipe creates the "exit" for the last loop and the "entry" for this loop
                    addRecipe(bridgeId, bridgeSourceFromPrevLoop, catalyst, firstCache);
                }

                // Select a bridge anchor from the current loop to serve as the exit into the next loop
                if (!loopItems.isEmpty()) {
                    bridgeSourceFromPrevLoop = loopItems.get(loopItems.size() / 2);
                }

                chainIndex++;

                // Bail if zero links were created to prevent accidental hangs
                if (recipesMade == 0) {
                    Identifier bridgeId = new Identifier(Welkin.MOD_ID, "alchemy/" + baseName + "/bridge_" + (chainIndex - 1) + "_to_" + chainIndex);
                    addRecipe(bridgeId, prevCache, catalyst, veryVeryFirst);
                    break;
                }
            }
        }
    }

    private static Item pickRandom(List<Item> pool, Random random) {
        int index = random.nextInt(pool.size());
        return pool.remove(index);
    }

    private static void addRecipe(Identifier id, Item input, Block catalyst, Item output) {
        AlchemyRecipe recipe = new AlchemyRecipe(id, input, catalyst, output);
        RECYCLER_RECIPES.put(id, recipe);

        if (catalyst != null) {
            CATALYST_RECIPES.put(input, recipe);
        } else {
            BASE_RECIPES.put(input, recipe);
        }
    }

    private static Identifier idFor(String baseName, int chainIndex, int recipeIndex) {
        return new Identifier(Welkin.MOD_ID, "alchemy/" + baseName + "/chain" + chainIndex + "_recipe" + recipeIndex);
    }
}