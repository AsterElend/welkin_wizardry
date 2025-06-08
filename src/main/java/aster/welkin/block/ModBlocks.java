package aster.welkin.block;

import aster.welkin.Welkin;

import aster.welkin.block.fancy.focus.FocusBlock;
import aster.welkin.block.fancy.node.NodeBlock;
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

	public static final Block UNLIGHT = registerBlock("unlight",
			new Block(FabricBlockSettings.copyOf(Blocks.SEA_LANTERN).luminance(-15)));
// now for blockentties
	public static final Block NODE =  registerBlock("node",
			new NodeBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_WIRE)));
	public static final Block FOCUS =  registerBlock("focus",
			new FocusBlock(FabricBlockSettings.copyOf(Blocks.CONDUIT)));



	private static Block registerBlock(String name, Block block) {
		registerBlockItem(name, block);
		return Registry.register(Registries.BLOCK, new Identifier(Welkin.MOD_ID, name), block);
	}

	private static Item registerBlockItem(String name, Block block) {
		return Registry.register(Registries.ITEM, new Identifier(Welkin.MOD_ID, name),
				new BlockItem(block, new FabricItemSettings()));
	}



	public static void registerModBlocks() {
		Welkin.LOGGER.info("Registering ModBlocks for" + Welkin.MOD_ID);
	}





}
