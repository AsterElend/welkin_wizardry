package aster.welkin.item;

import aster.welkin.Welkin;
import aster.welkin.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class ModItemGroups {
	public static final ItemGroup welkin = Registry.register(Registries.ITEM_GROUP,
		new Identifier(Welkin.MOD_ID, "welkin"),
			FabricItemGroup.builder().displayName(Text.translatable("itemgroup.welkin"))
					.icon(()-> new ItemStack(ModItems.CHARGESTONE)).entries((displayContext, entries) -> {

						entries.add(ModItems.CHARGESTONE);
						entries.add(ModItems.STORMPHRAX);
						entries.add(ModItems.ZEPHYRITE);
						entries.add(ModItems.STELLARIUM);
						entries.add(ModItems.GRIMORE);
						entries.add(ModItems.WAND);

						entries.add(ModBlocks.STARSTONE);

						entries.add(ModBlocks.CHARGEPLANKS);
						entries.add(ModBlocks.CHARGEWOOD);
						entries.add(ModBlocks.CHARGELOG);
						entries.add(ModBlocks.STRIPPEDCHARGELOG);
						entries.add(ModBlocks.STRIPPEDCHARGEWOOD);

						entries.add(ModItems.STORMCYCLE_DISC);
						entries.add(ModItems.WINDTUNNEL_DISC);


						entries.add(ModBlocks.UNLIGHT);

						//entries.add(ModBlocks.PYLON);
						entries.add(ModBlocks.FOCUS);






					}).build());

	public static void registerItemGroups() {
		Welkin.LOGGER.info("Registering Item Groups for " + Welkin.MOD_ID);
	}
}
