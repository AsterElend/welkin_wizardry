package aster.welkin_wizardry.block;

import aster.welkin_wizardry.WelkinWizardry;
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















	private static Block registerBlock(String name, Block block) {
		registerBlockItem(name, block);
		return Registry.register(Registries.BLOCK, new Identifier(WelkinWizardry.MOD_ID, name), block);
	}

	private static Item registerBlockItem(String name, Block block) {
		return Registry.register(Registries.ITEM, new Identifier(WelkinWizardry.MOD_ID, name),
				new BlockItem(block, new FabricItemSettings()));
	}


	public static void registerModBlocks() {
		WelkinWizardry.LOGGER.info("Registering ModBlocks for" + WelkinWizardry.MOD_ID);
	}
}
