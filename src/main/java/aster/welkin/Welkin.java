package aster.welkin;


import aster.welkin.api.ExtraEnchantsHolder;
import aster.welkin.api.SigilVisionManager;
import aster.welkin.api.chat.ChatCatchHelper;
import aster.welkin.api.state.WeatherManager;
import aster.welkin.compat.SpectrumWeatherOverrides;
import aster.welkin.config.WelkinConfig;
import aster.welkin.packet.WelkinPackets;
import aster.welkin.recipes.AlchemyReloadListener;
import aster.welkin.registry.*;
import aster.welkin.registry.particle.WelkinParticles;
import aster.welkin.registry.sound.ModSounds;
import aster.welkin.registry.world.DimensionStuff;
import aster.welkin.registry.world.trees.WelkinFoliagePlacers;
import aster.welkin.registry.world.trees.WelkinTrunkPlacers;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Welkin implements ModInitializer {
	public static final String MOD_ID = "welkin";
	public static final WelkinConfig CONFIG;

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Identifier id(String it){
		return new Identifier(MOD_ID, it);
	}

	static {
		Welkin.LOGGER.info("loading config for welkin...");
		AutoConfig.register(WelkinConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(WelkinConfig.class).getConfig();
		Welkin.LOGGER.info("loaded!");
	}


	@Override
	public void onInitialize() {
		WelkinItems.registerModItems();
		WelkinTags.register();
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerWorld world = handler.getPlayer().getServerWorld();
			WeatherManager.getServerState(world).sendFullSync(handler.getPlayer());
		});
		ExtraEnchantsHolder.registerExtraTag(Enchantments.SWIFT_SNEAK, ItemTags.BOATS);
		ExtraEnchantsHolder.registerExtraTag(Enchantments.FROST_WALKER, ItemTags.BOATS);
		ExtraEnchantsHolder.registerExtraTag(Enchantments.DEPTH_STRIDER, ItemTags.BOATS);
		ServerTickEvents.END_WORLD_TICK.register(SigilVisionManager::tick);
		WelkinCommands.evoke();

		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		WelkinItemGroups.registerItemGroups();
		WelkinRecipes.register();

		WelkinBlocks.registerModBlocks();
		ModSounds.registerSounds();
		WelkinBlockEntities.registerBlockEntities();
		WelkinFluids.invoke();
		WelkinTrunkPlacers.init();
		WelkinFoliagePlacers.init();
		DimensionStuff.init();
		WelkinPackets.registerServer();
		ChatCatchHelper.register();
		WelkinEffects.invoke();
		WelkinEvents.register();
		WelkinParticles.register();


		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new AlchemyReloadListener());




		LOGGER.info("Hello Fabric world!");

		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			if (FabricLoader.getInstance().isModLoaded("spectrum")){
				SpectrumWeatherOverrides.register();
			}
		});


	}




}