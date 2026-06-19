package aster.welkin.jsonstuff;

import aster.welkin.Welkin;
import aster.welkin.client.WelkinState;
import aster.welkin.recipes.RecyclerRecipeManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecyclerReloadListener implements SimpleSynchronousResourceReloadListener {
    @Override
    public Identifier getFabricId() {
        return Welkin.id("recycler_reload");
    }

    private static List<RecycleSet> TAGS_TO_ALCHEMIZE;
    public static List<RecycleSet> getTagsToAlchemize(){
        return List.copyOf(TAGS_TO_ALCHEMIZE);
    }


    public record RecycleSet(
            TagKey<Item> mainTag,
            List<TagKey<Item>> subtractedTags,
            List<Identifier> subtractedItems,
            int comboLength
    ){

    }

    @Override
    public void reload(ResourceManager manager) {

        TAGS_TO_ALCHEMIZE = new ArrayList<>();
        Gson gson = new Gson();
        for (Identifier id: manager.findResources(
                "alchemy_recycling", path -> path.getPath().endsWith(".json")
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
                int length = json.get("comboLength").getAsInt();
                List<TagKey<Item>> keyList = new ArrayList<>();
                List<Identifier> itemList = new ArrayList<>();
                for (JsonElement el: itemArray){
                    itemList.add(new Identifier(el.getAsString()));
                }
                for (JsonElement element: tagArray){
                    keyList.add(TagKey.of(RegistryKeys.ITEM, new Identifier(element.getAsString())));
                }

                RecycleSet set = new RecycleSet(
                        mainTag, keyList, itemList, length
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

        RecyclerRecipeManager.generateRecipes(WelkinState.worldSeed);


    }

    @Override
    public Collection<Identifier> getFabricDependencies() {
        return List.of(new Identifier("minecraft", "recipes"));
    }
}
