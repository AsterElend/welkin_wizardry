package aster.welkin;


import aster.welkin.client.WelkinState;
import aster.welkin.jsonstuff.RecyclerReloadListener;
import aster.welkin.registry.*;
import aster.welkin.sound.ModSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.resource.ResourceType;
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



	@Override
	public void onInitialize() {
		ModItems.registerModItems();



		
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModItemGroups.registerItemGroups();
		ModRecipes.register();

		ModBlocks.registerModBlocks();
		ModSounds.registerSounds();
		ModBlockEntities.registerBlockEntities();

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			WelkinState.worldSeed = server.getOverworld().getSeed();
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			WelkinState.worldSeed = server.getOverworld().getSeed();

		});
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new RecyclerReloadListener());


		LOGGER.info("Hello Fabric world!");
	}
}