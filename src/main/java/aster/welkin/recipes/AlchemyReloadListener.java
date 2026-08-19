package aster.welkin.recipes;

import aster.welkin.Welkin;
import aster.welkin.registry.WelkinRecipes;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AlchemyReloadListener implements SimpleSynchronousResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return Welkin.id("alchemy_reloader");
    }

    private static List<RecycleSet> TAGS_TO_ALCHEMIZE;
    public static List<RecycleSet> getTagsToAlchemize(){
        return List.copyOf(TAGS_TO_ALCHEMIZE);
    }


    public record RecycleSet(
            TagKey<Item> mainTag,
            List<TagKey<Item>> subtractedTags,
            List<Identifier> subtractedItems,
            int loopLength,
            Block catalyst
    ){

    }

    @Override
    public void reload(ResourceManager manager) {
        Welkin.LOGGER.info("reloading alchemy jsons");
        TAGS_TO_ALCHEMIZE = new ArrayList<>();
        Gson gson = new Gson();
        for (Identifier id: manager.findResources(
                "alchemy", path -> path.getPath().endsWith(".json")
        ).keySet()) {
            try {
                Resource resource = manager.getResource(id).orElseThrow();
                Reader reader = new InputStreamReader(
                        resource.getInputStream(),
                        StandardCharsets.UTF_8
                );

                JsonObject json = gson.fromJson(reader, JsonObject.class);
                if (json == null){
                    Welkin.LOGGER.info("Alchemy Recycling type {} returned null, skipping", id);
                    continue;
                }

                TagKey<Item> mainTag = TagKey.of(RegistryKeys.ITEM, new Identifier(json.get("mainTag").getAsString()));
                JsonArray itemArray = json.getAsJsonArray("removeItems");
                JsonArray tagArray = json.getAsJsonArray("removeTags");
                int length = json.get("loopLength").getAsInt();
                Block catalyst = Registries.BLOCK.get(new Identifier(json.get("catalyst").getAsString()));
                List<TagKey<Item>> keyList = new ArrayList<>();
                List<Identifier> itemList = new ArrayList<>();
                for (JsonElement el: itemArray){
                    itemList.add(new Identifier(el.getAsString()));
                }
                for (JsonElement element: tagArray){
                    keyList.add(TagKey.of(RegistryKeys.ITEM, new Identifier(element.getAsString())));
                }

                RecycleSet set = new RecycleSet(
                        mainTag, keyList, itemList, length, catalyst
                );

                TAGS_TO_ALCHEMIZE.add(set);

            } catch (Exception e) {
                Welkin.LOGGER.error(
                        "Failed to load alchemical recycling set {}",
                        id,
                        e
                );
            }
        }



    }

    @Override
    public Collection<Identifier> getFabricDependencies() {
        return List.of(ResourceReloadListenerKeys.TAGS); // ensures tags are loaded first
    }

    public static void injectRecipes(MinecraftServer currentServer) {
        if (currentServer == null) return;

        RecipeManager manager = currentServer.getRecipeManager();

        Welkin.LOGGER.info("Doing horrible things to the recipe manager...");

        AlchemyRecipeManager.generateRecipes();
        Map<Identifier, Recipe<?>> dynamicRecipes = AlchemyRecipeManager.getAlchemyRecipes();

        Map<RecipeType<?>, Map<Identifier, Recipe<?>>> managerCopy = new HashMap<>(manager.recipes);
        managerCopy.put(WelkinRecipes.ALCHEMY_TYPE, dynamicRecipes);

        manager.recipes = managerCopy;

    }

}
