package aster.welkin.datagen;

import aster.welkin.registry.WelkinRecipes;
import com.google.gson.JsonObject;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.CriterionMerger;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
public class LightningRecipeBuilder extends RecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final Item output;
    private final int count;
    private Ingredient input = Ingredient.EMPTY;
    private final Advancement.Builder advancementBuilder = Advancement.Builder.createUntelemetered();
    @Nullable
    private String group;

    public LightningRecipeBuilder(ItemConvertible output, int count) {
        this.output = output.asItem();
        this.count = count;
    }

    public static LightningRecipeBuilder create(ItemConvertible output) {
        return new LightningRecipeBuilder(output, 1);
    }

    public static LightningRecipeBuilder create(ItemConvertible output, int count) {
        return new LightningRecipeBuilder(output, count);
    }

    // Accept an ItemConvertible directly
    public LightningRecipeBuilder input(ItemConvertible itemProvider) {
        this.input = Ingredient.ofItems(itemProvider);
        return this;
    }

    // Accept an item TagKey
    public LightningRecipeBuilder input(TagKey<Item> tag) {
        this.input = Ingredient.fromTag(tag);
        return this;
    }

    // Accept a raw pre-built Ingredient
    public LightningRecipeBuilder input(Ingredient ingredient) {
        this.input = ingredient;
        return this;
    }

    @Override
    public LightningRecipeBuilder criterion(String string, CriterionConditions criterionConditions) {
        this.advancementBuilder.criterion(string, criterionConditions);
        return this;
    }

    @Override
    public LightningRecipeBuilder group(@Nullable String string) {
        this.group = string;
        return this;
    }

    @Override
    public Item getOutputItem() {
        return this.output;
    }

    @Override
    public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
        this.validate(recipeId);

        // Setup vanilla advancement hierarchy matching vanilla standard recipe builders
        this.advancementBuilder.parent(ROOT)
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
                .rewards(net.minecraft.advancement.AdvancementRewards.Builder.recipe(recipeId))
                .criteriaMerger(CriterionMerger.OR);

        exporter.accept(new LightningRecipeJsonProvider(
                recipeId,
                this.output,
                this.count,
                this.group == null ? "" : this.group,
                this.input,
                this.advancementBuilder,
                recipeId.withPrefixedPath("recipes/lightning/")
        ));
    }

    private void validate(Identifier recipeId) {
        if (this.advancementBuilder.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeId);
        }
        if (this.input == Ingredient.EMPTY) {
            throw new IllegalStateException("Lightning recipe " + recipeId + " requires a valid input ingredient!");
        }
    }

    public static class LightningRecipeJsonProvider implements RecipeJsonProvider {
        private final Identifier recipeId;
        private final Item output;
        private final int count;
        private final String group;
        private final Ingredient input;
        private final Advancement.Builder advancementBuilder;
        private final Identifier advancementId;

        public LightningRecipeJsonProvider(Identifier recipeId, Item output, int outputCount, String group,
                                           Ingredient input, Advancement.Builder advancementBuilder, Identifier advancementId) {
            this.recipeId = recipeId;
            this.output = output;
            this.count = outputCount;
            this.group = group;
            this.input = input;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
        }

        @Override
        public void serialize(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            // Matches your serializer: json.get("ingredient")
            json.add("ingredient", this.input.toJson());

            // Matches your serializer: json.getAsJsonObject("result")
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("item", Registries.ITEM.getId(this.output).toString());
            if (this.count > 1) {
                jsonObject.addProperty("count", this.count);
            }
            json.add("result", jsonObject);
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            // Replace with your registered serializer instance
            return WelkinRecipes.LIGHTNING_SERIALIZER;
        }

        @Override
        public Identifier getRecipeId() {
            return this.recipeId;
        }

        @Nullable
        @Override
        public JsonObject toAdvancementJson() {
            return this.advancementBuilder.toJson();
        }

        @Nullable
        @Override
        public Identifier getAdvancementId() {
            return this.advancementId;
        }
    }
}