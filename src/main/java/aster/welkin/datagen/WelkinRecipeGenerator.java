package aster.welkin.datagen;

import aster.welkin.Welkin;
import aster.welkin.recipes.LightningRecipe;
import aster.welkin.registry.WelkinBlocks;
import aster.welkin.registry.WelkinItems;
import aster.welkin.registry.WelkinTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import java.util.function.Consumer;

public class WelkinRecipeGenerator extends FabricRecipeProvider {
    public WelkinRecipeGenerator(FabricDataOutput output) {
        super(output);
    }



    public void makePlanksRecipe(ItemConvertible plank, ItemConvertible log, Consumer<RecipeJsonProvider> exporter){
        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, plank, 4)
                .input(log).criterion(FabricRecipeProvider.hasItem(log), FabricRecipeProvider.conditionsFromItem(log))
                .offerTo(exporter, Welkin.id("crafting/" + plank.asItem().toString().replace("welkin:", "")));
    }

    public void makeWoodRecipe(ItemConvertible wood, ItemConvertible log, Consumer<RecipeJsonProvider> exporter){
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, wood, 3)
                .pattern("ll")
                .pattern("ll")
                .input('l', log)
                .criterion(FabricRecipeProvider.hasItem(log), FabricRecipeProvider.conditionsFromItem(log))
                .offerTo(exporter, Welkin.id("crafting/" + wood.asItem().toString().replace("welkin:" ,"")));
    }
    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        LightningRecipeBuilder.create(Items.SUGAR)
                .input(Items.BEETROOT)
                .criterion(FabricRecipeProvider.hasItem(Items.BEETROOT), FabricRecipeProvider.conditionsFromItem(Items.BEETROOT)).offerTo(exporter);

        LightningRecipeBuilder.create(WelkinItems.STORMPHRAX)
                .input(ConventionalItemTags.COPPER_INGOTS)
                .criterion(FabricRecipeProvider.hasItem(Items.COPPER_INGOT), FabricRecipeProvider.conditionsFromTag(ConventionalItemTags.COPPER_INGOTS))
                .offerTo(exporter);

         LightningRecipeBuilder.create(Items.GLASS)
                .input(ItemTags.SMELTS_TO_GLASS)
                .criterion(FabricRecipeProvider.hasItem(Items.SAND), FabricRecipeProvider.conditionsFromTag(ItemTags.SMELTS_TO_GLASS))
                .offerTo(exporter);

         LightningRecipeBuilder.create(WelkinBlocks.CHARGELOG)
                 .input(ItemTags.LOGS)
                 .criterion("has_log", FabricRecipeProvider.conditionsFromTag(ItemTags.LOGS))
                 .offerTo(exporter);


        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, WelkinBlocks.STORM_EYE)
                .pattern("gsg").pattern("sbs").pattern("gsg")
                .input('g', ConventionalItemTags.GLASS_BLOCKS)
                .input('s', WelkinTags.CUT_COPPER_ITEM_TAG)
                .input('b', Items.REDSTONE)
                .criterion(FabricRecipeProvider.hasItem(Items.REDSTONE), FabricRecipeProvider.conditionsFromItem(Items.REDSTONE))
                .offerTo(exporter, Welkin.id("crafting/default_storm_eye"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, WelkinBlocks.STORM_EYE)
                .pattern("gsg").pattern("sbs").pattern("gsg")
                .input('g', ConventionalItemTags.GLASS_BLOCKS)
                .input('s', WelkinItems.STORMPHRAX)
                .input('b', WelkinItems.CONDUCTOR_BATON)
                .criterion(FabricRecipeProvider.hasItem(WelkinItems.STORMPHRAX), FabricRecipeProvider.conditionsFromItem(WelkinItems.STORMPHRAX))
                .offerTo(exporter, Welkin.id("crafting/cheaper_storm_eye"));

        makePlanksRecipe(WelkinBlocks.CHARGEPLANKS, WelkinBlocks.CHARGELOG, exporter);
        makePlanksRecipe(WelkinBlocks.FRACTAL_PLANKS, WelkinBlocks.FRACTAL_LOG, exporter);
        makePlanksRecipe(WelkinBlocks.WATCHER_PLANKS, WelkinBlocks.WATCHER_LOG, exporter);

        makeWoodRecipe(WelkinBlocks.FRACTAL_WOOD, WelkinBlocks.FRACTAL_LOG, exporter);
        makeWoodRecipe(WelkinBlocks.CHARGEWOOD, WelkinBlocks.CHARGELOG, exporter);
        makeWoodRecipe(WelkinBlocks.WATCHER_WOOD, WelkinBlocks.WATCHER_LOG, exporter);


        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, WelkinItems.CONDUCTOR_BATON)
                .pattern(" c ")
                .pattern(" l ")
                .pattern(" r ")
                .input('c', WelkinItems.STORMPHRAX)
                .input('l', WelkinItems.WISHFUL_BATON)
                .input('r', Items.REDSTONE)
                .criterion(FabricRecipeProvider.hasItem(WelkinItems.STORMPHRAX), FabricRecipeProvider.conditionsFromItem(WelkinItems.STORMPHRAX))
                .offerTo(exporter, Welkin.id("crafting/conductor_baton"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, WelkinItems.WISHFUL_BATON)
                .pattern("  l")
                .pattern(" l ")
                .pattern("l  ")
                .input('l', WelkinTags.CHARGED_LOGS)
                .criterion(FabricRecipeProvider.hasItem(WelkinBlocks.CHARGELOG), FabricRecipeProvider.conditionsFromTag(WelkinTags.CHARGED_LOGS))
                .offerTo(exporter, Welkin.id("crafting/wishful_baton"));

    }



}
