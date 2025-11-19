package aster.welkin;

import aster.welkin.block.ModBlockEntities;
import aster.welkin.block.ModBlocks;
import aster.welkin.recipes.LightningHandler;
//import aster.welkin.block.fancy.brazier2.TestRecipe;
import aster.welkin.item.ModItemGroups;
import aster.welkin.item.ModItems;
import aster.welkin.recipes.SmiteRecipeSerializer;
import aster.welkin.recipes.SmiteRecipeType;
import aster.welkin.sound.ModSounds;
import net.fabricmc.api.ModInitializer;

import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Welkin implements ModInitializer {
	public static final String MOD_ID = "welkin";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Identifier id(String it){
		return new Identifier(MOD_ID, it);
	}

	//public static final RecipeType<TestRecipe> RECIPE_TYPE  = RecipeType.register("welkin:mergify");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModSounds.registerSounds();
		ModBlockEntities.registerBlockEntities();

		Registry.register(Registries.RECIPE_TYPE,
				SmiteRecipeType.ID, SmiteRecipeType.INSTANCE);

		Registry.register(Registries.RECIPE_SERIALIZER,
				SmiteRecipeType.ID, SmiteRecipeSerializer.INSTANCE);


		LOGGER.info("Hello Fabric world!");
	}
}