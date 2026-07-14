package aster.welkin.registry;

import aster.welkin.Welkin;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class WelkinItemGroups {
	public static final ItemGroup welkin = Registry.register(Registries.ITEM_GROUP,
		new Identifier(Welkin.MOD_ID, "welkin"),
			FabricItemGroup.builder().displayName(Text.translatable("itemgroup.welkin"))
					.icon(()-> new ItemStack(WelkinItems.CHARGESTONE)).entries((displayContext, entries) -> {
						for (Item item: WelkinItems.ALL_ITEMS){
							entries.add(item);
						}

						for (Block block: WelkinBlocks.ALL_BLOCKS){
							entries.add(block);
						}
					}).build());

	public static void registerItemGroups() {
		Welkin.LOGGER.info("Registering Item Groups for " + Welkin.MOD_ID);
	}
}
