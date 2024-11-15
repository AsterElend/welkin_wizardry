package aster.welkin_wizardry.item;

import aster.welkin_wizardry.WelkinWizardry;
import aster.welkin_wizardry.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class ModItemGroups {
	public static final ItemGroup WELKIN_WIZARDRY = Registry.register(Registries.ITEM_GROUP,
		new Identifier(WelkinWizardry.MOD_ID, "welkin_wizardry"),
			FabricItemGroup.builder().displayName(Text.translatable("itemgroup.welkin_wizardry"))
					.icon(()-> new ItemStack(ModItems.CHARGESTONE)).entries((displayContext, entries) -> {

						entries.add(ModItems.CHARGESTONE);
						entries.add(ModItems.STORMPHRAX);
						entries.add(ModItems.ZEPHYRITE);
						entries.add(ModItems.STELLARIUM);
						entries.add(ModItems.GRIMOIRE);

						entries.add(ModBlocks.STARSTONE);

					}).build());

	public static void registerItemGroups() {
		WelkinWizardry.LOGGER.info("Registering Item Groups for " + WelkinWizardry.MOD_ID);
	}
}
