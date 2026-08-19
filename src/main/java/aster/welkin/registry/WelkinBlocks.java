package aster.welkin.registry;

import aster.welkin.Welkin;
import aster.welkin.api.GenericSpaceSapling;
import aster.welkin.block.*;
import aster.welkin.block.entity.DebugThunderhead;
import aster.welkin.block.sigil.SimpleSigilBlocks;
import aster.welkin.block.sigil.SuliSigilBlock;
import aster.welkin.block.transducer.FluidTransducerBlock;
import aster.welkin.block.transducer.ItemTransducerBlock;
import aster.welkin.registry.world.trees.FractalSaplingGenerator;
import aster.welkin.registry.world.trees.WatcherSaplingGenerator;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static aster.welkin.Welkin.id;


public class WelkinBlocks {
	public static final List<Block> ALL_BLOCKS = new ArrayList<>();

	public static final Block CHARGEWOOD = registerBlock("chargewood",
			new PillarBlock(FabricBlockSettings.copyOf(Blocks.WARPED_HYPHAE)));
	public static final Block STRIPPEDCHARGELOG = registerBlock("strippedchargelog",
			new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_WARPED_STEM)));
	public static final Block STRIPPEDCHARGEWOOD = registerBlock("strippedchargewood",
			new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_WARPED_HYPHAE)));
	public static final Block CHARGELOG = registerBlock("chargelog",
			new PillarBlock(FabricBlockSettings.copyOf(Blocks.WARPED_STEM)));

	public static final Block PUMICE = registerBlock("pumice", new Block(FabricBlockSettings.copyOf(Blocks.TUFF)));

	public static final Block CHARGEPLANKS = registerBlock("chargeplanks",
			new Block(FabricBlockSettings.copyOf(Blocks.WARPED_PLANKS)));


	public static final Block MOTHTILE = registerBlock("mothtile",
			new Block(FabricBlockSettings.copyOf(Blocks.BLACK_GLAZED_TERRACOTTA)));
	public static final Block STARSTONE = registerBlock("starstone", new Block(FabricBlockSettings.copyOf(Blocks.BEDROCK)));
	public static final Block VOIDSTONE = registerBlock("voidstone", new Block(FabricBlockSettings.copyOf(Blocks.DEEPSLATE)));

	public static final Block FRACTAL_LOG = registerBlock("fractal_log", new PillarBlock(FabricBlockSettings.copyOf(Blocks.CRIMSON_STEM)));
	public static final Block STRIPPED_FRACTAL_LOG = registerBlock("stripped_fractal_log", new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_CRIMSON_STEM)));
	public static final Block FRACTAL_WOOD = registerBlock("fractal_wood",
			new PillarBlock(FabricBlockSettings.copyOf(Blocks.CRIMSON_HYPHAE)));
	public static final Block STRIPPED_FRACTAL_WOOD = registerBlock("stripped_fractal_wood",
			new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_CRIMSON_HYPHAE)));
	public static final Block FRACTAL_PLANKS=
			registerBlock("fractal_planks", new Block(FabricBlockSettings.copyOf(Blocks.WARPED_PLANKS)));
	public static final Block FRACTAL_SAPLING = registerBlock("fractal_sapling", new GenericSpaceSapling(new FractalSaplingGenerator(),
			FabricBlockSettings.copyOf(Blocks.BIRCH_SAPLING)));

	public static final Block FRACTAL_LEAVES = registerBlock("fractal_leaves", new LeavesBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_LEAVES)));

	public static final Block STORM_EYE = registerBlock("storm_eye", new StormEyeBlock(FabricBlockSettings.copyOf(Blocks.BEACON)));



	public static final Block WATCHER_LOG = registerBlock("watcher_log", new PillarBlock(FabricBlockSettings.copyOf(Blocks.CRIMSON_STEM)));
	public static final Block STRIPPED_WATCHER_LOG = registerBlock("stripped_watcher_log", new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_CRIMSON_STEM)));
	public static final Block WATCHER_WOOD = registerBlock("watcher_wood",
			new PillarBlock(FabricBlockSettings.copyOf(Blocks.CRIMSON_HYPHAE)));
	public static final Block STRIPPED_WATCHER_WOOD = registerBlock("stripped_watcher_wood",
			new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_CRIMSON_HYPHAE)));
	public static final Block WATCHER_PLANKS=
			registerBlock("watcher_planks", new Block(FabricBlockSettings.copyOf(Blocks.WARPED_PLANKS)));
	public static final Block WATCHER_SAPLING = registerBlock("watcher_sapling", new GenericSpaceSapling(new WatcherSaplingGenerator(),
			FabricBlockSettings.copyOf(Blocks.BIRCH_SAPLING)));

	public static final Block WATCHER_LEAVES = registerBlock("watcher_leaves", new LeavesBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_LEAVES)));

	public static final Block LETHEAN_WATER_BLOCK = registerBlockWithNoItem("lethean_water", new FluidBlock(WelkinFluids.LETHEAN_WATER_STATIC, FabricBlockSettings.copyOf(Blocks.WATER)));

	public static final Block SUNSTONE = registerBlock("sunstone", new SunstoneBlock(FabricBlockSettings.copyOf(Blocks.TUFF).luminance(SunstoneBlock.STATE_TO_LUMINANCE)));








	// now for blockentties

	//modeled!
	public static final Block NODE =  registerBlockWithNoItem("node",
			new NodeBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_WIRE)));
	//bad model, but good enough
	public static final Block PYLON =  registerBlock("pylon",
			new PylonBlock(FabricBlockSettings.copyOf(Blocks.CONDUIT)));
	//same here
	public static final Block CONDENSER = registerBlock("condenser",
			new CondenserBlock(FabricBlockSettings.copyOf(Blocks.CAULDRON)));



	public static final Block WARP_ARRAY = registerBlockWithNoItem("warp_array", new WarpArrayBlock(FabricBlockSettings.copyOf(Blocks.DARK_PRISMARINE_SLAB)));
	public static final Block WARP_CONTROLLER = registerBlockWithNoItem("warp_controller", new WarpArrayBlock.Controller(FabricBlockSettings.copyOf(Blocks.DARK_PRISMARINE_SLAB)));
	//acceptable for now
	public static final Block ITEM_TRANSDUCER = registerBlock("item_transducer", new ItemTransducerBlock(FabricBlockSettings.copyOf(Blocks.CONDUIT).nonOpaque()));
	public static final Block FLUID_TRANSDUCER = registerBlock("fluid_transducer", new FluidTransducerBlock(FabricBlockSettings.copyOf(Blocks.CONDUIT).nonOpaque()));
	//no models
	public static final Block HEART_EXTRACTOR = registerBlock("heart_extractor", new HeartExtractorBlock(FabricBlockSettings.copyOf(Blocks.BEACON).nonOpaque()));
	public static final Block ALCHEMICAL_ENGINE = registerBlock("alchemical_engine", new AlchemyBlock(FabricBlockSettings.copyOf(Blocks.CAULDRON).nonOpaque()));
	public static final Block TEAPOT = registerBlock("teapot", new TeapotBlock(FabricBlockSettings.copyOf(Blocks.CAULDRON).nonOpaque()));
	public static final Block MAELSTROM_GENERATOR = registerBlock("maelstrom_generator", new MaelstromGeneratorBlock(FabricBlockSettings.copyOf(Blocks.DAYLIGHT_DETECTOR)));
	public static final Block THUNDERHEAD = registerBlock("thunderhead", new ThunderheadBlock(FabricBlockSettings.copyOf(Blocks.POWDER_SNOW)));
	public static final Block CREATIVE_THUNDERHEAD = registerBlock("creative_thunderhead", new DebugThunderhead.Block(FabricBlockSettings.copyOf(Blocks.POWDER_SNOW)));
	public static final Block AETHER_TRANSDUCER = registerBlock("aether_transducer", new AetherTransducerBlock(FabricBlockSettings.copyOf(Blocks.CONDUIT)));
	public static final Block ECHOING_RECEIVER = registerBlock("echoing_receiver", new EchoingReceiverBlock(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK)));
	public static final Block CHRISTENING_ALTAR = registerBlock("christening_altar", new ChristeningAltarBlock(FabricBlockSettings.copy(Blocks.COBBLESTONE_WALL)));
	public static final Block AGONITE_TRANSMUTER = registerBlock("agonite_transmuter",
			new AgoniteTransmuterBlock(FabricBlockSettings.copyOf(Blocks.BREWING_STAND)));
	public static final Block ENCHANTMENT_DISINTEGRATOR = registerBlock("enchantment_disintegrator", new EnchantmentDisintegratorBlock(FabricBlockSettings.copyOf(Blocks.CAULDRON)));
	public static final Block FALSE_STAR = registerBlock("false_star", new FalseStarBlock(FabricBlockSettings.copyOf(Blocks.BEACON)));

	//no models needed
	public static final Block TENPO_SIGIL = registerBlock("tenpo_sigil", new SimpleSigilBlocks.TenpoSigilBlock(FabricBlockSettings.copyOf(WelkinBlocks.NODE)));
	public static final Block SULI_SIGIL = registerBlock("suli_sigil", new SuliSigilBlock(FabricBlockSettings.copyOf(WelkinBlocks.NODE)));
	//functional blocks that aren't entites

	//acceptable model
	public static final Block LIGHTNING_ALTAR = registerBlock("lightning_altar", new LightningAltar(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).nonOpaque()));
	//no model
	public static final Block ANTIGRAVITY_PYLON = registerBlock("antigravity_pylon", new AntigravityPylon(FabricBlockSettings.copyOf(Blocks.CONDUIT)));


	//public static final Block TANK_FRAME = registerBlock("tankframe", new TankFrameBlock(FabricBlockSettings.copyOf(Blocks.COPPER_BLOCK)));



	private static Block registerBlock(String name, Block block) {
		registerBlockItem(name, block);
		return Registry.register(Registries.BLOCK, new Identifier(Welkin.MOD_ID, name), block);
	}

	private static Block registerBlockWithNoItem(String name, Block block){
		return Registry.register(Registries.BLOCK, id(name), block);
	}

	private static void registerBlockItem(String name, Block block) {
		ALL_BLOCKS.add(block);
		Registry.register(Registries.ITEM, new Identifier(Welkin.MOD_ID, name),
				new BlockItem(block, new FabricItemSettings()));
	}



	public static void registerModBlocks() {
		Welkin.LOGGER.info("Registering ModBlocks for" + Welkin.MOD_ID);
	}





}
