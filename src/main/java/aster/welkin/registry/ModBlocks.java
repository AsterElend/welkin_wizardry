package aster.welkin.registry;

import aster.welkin.Welkin;

import aster.welkin.block.extractor.ExtractorBlock;
import aster.welkin.block.solo.LightningAltar;
import aster.welkin.block.brazier.VoidBrazierBlock;
import aster.welkin.block.condese.CondenseBlock;
import aster.welkin.block.node.NodeBlock;
import aster.welkin.block.pylon.PylonBlock;
//import aster.welkin.block.tank.TankController;
//import aster.welkin.block.tank.TankFrame;
import aster.welkin.block.transducer.TransducerBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import net.minecraft.block.PillarBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import static aster.welkin.Welkin.id;


public class ModBlocks {
	public static final Block STARSTONE = registerBlock("starstone",
			new Block(FabricBlockSettings.copyOf(Blocks.TUFF).sounds(BlockSoundGroup.SCULK)));

	public static final Block CHARGEWOOD = registerBlock("chargewood",
			new PillarBlock(FabricBlockSettings.copyOf(Blocks.WARPED_HYPHAE)));
	public static final Block STRIPPEDCHARGELOG = registerBlock("strippedchargelog",
			new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_WARPED_STEM)));
	public static final Block STRIPPEDCHARGEWOOD = registerBlock("strippedchargewood",
			new PillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_WARPED_HYPHAE)));
	public static final Block CHARGELOG = registerBlock("chargelog",
			new PillarBlock(FabricBlockSettings.copyOf(Blocks.WARPED_STEM)));


	public static final Block CHARGEPLANKS = registerBlock("chargeplanks",
			new Block(FabricBlockSettings.copyOf(Blocks.WARPED_PLANKS)));


	public static final Block MOTHTILE = registerBlock("mothtile",
			new Block(FabricBlockSettings.copyOf(Blocks.BLACK_GLAZED_TERRACOTTA)));



	//public static final Block UNLIGHT = registerBlock("unlight",
			//new Block(FabricBlockSettings.copyOf(Blocks.SEA_LANTERN).luminance(-15)));
// now for blockentties
	public static final Block NODE =  registerBlockNoItem("node",
			new NodeBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_WIRE)));
	public static final Block PYLON =  registerBlock("pylon",
			new PylonBlock(FabricBlockSettings.copyOf(Blocks.CONDUIT)));
	public static final Block BRAZIER = registerBlock("brazier",
			new VoidBrazierBlock(FabricBlockSettings.copyOf(Blocks.SOUL_CAMPFIRE)));
	public static final Block CONDENSER = registerBlock("condenser",
			new CondenseBlock(FabricBlockSettings.copyOf(Blocks.CAULDRON)));
	//public static final Block TANKCONTROLLER = registerBlock("controller", new TankController(FabricBlockSettings.copyOf(Blocks.CONDUIT)));
	public static final Block TRANSDUCER = registerBlock("transducer", new TransducerBlock(FabricBlockSettings.copyOf(Blocks.CONDUIT).nonOpaque()));
	public static final Block EXTRACTOR = registerBlock("extractor", new ExtractorBlock(FabricBlockSettings.copyOf(Blocks.BEACON).nonOpaque()));

	//functional blocks that aren't entites
	public static final Block ALTAR = registerBlock("altar", new LightningAltar(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).nonOpaque()));


    //public static final Block TANK_FRAME = registerBlock("tankframe", new TankFrame(FabricBlockSettings.copyOf(Blocks.COPPER_BLOCK)));



	private static Block registerBlock(String name, Block block) {
		registerBlockItem(name, block);
		return Registry.register(Registries.BLOCK, new Identifier(Welkin.MOD_ID, name), block);
	}

	private static Block registerBlockNoItem(String name, Block block){
		return Registry.register(Registries.BLOCK, id(name), block);
	}

	private static Item registerBlockItem(String name, Block block) {
		return Registry.register(Registries.ITEM, new Identifier(Welkin.MOD_ID, name),
				new BlockItem(block, new FabricItemSettings()));
	}



	public static void registerModBlocks() {
		Welkin.LOGGER.info("Registering ModBlocks for" + Welkin.MOD_ID);
	}





}
