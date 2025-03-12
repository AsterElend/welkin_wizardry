package aster.welkin_wizardry.datagen;

import aster.welkin_wizardry.block.ModBlocks;
import aster.welkin_wizardry.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator){
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.STARSTONE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.CHARGEPLANKS);

        blockStateModelGenerator.registerLog(ModBlocks.CHARGELOG).log(ModBlocks.CHARGELOG).wood(ModBlocks.CHARGEWOOD);
        blockStateModelGenerator.registerLog(ModBlocks.STRIPPEDCHARGELOG).log(ModBlocks.STRIPPEDCHARGELOG).wood(ModBlocks.STRIPPEDCHARGEWOOD);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator){
        itemModelGenerator.register(ModItems.GRIMORE, Models.GENERATED);
        itemModelGenerator.register(ModItems.STORMPHRAX, Models.GENERATED);
        itemModelGenerator.register(ModItems.STELLARIUM, Models.GENERATED);
        itemModelGenerator.register(ModItems.ZEPHYRITE, Models.GENERATED);
        itemModelGenerator.register(ModItems.CHARGESTONE, Models.GENERATED);
        itemModelGenerator.register(ModItems.WAND, Models.HANDHELD_ROD);
        itemModelGenerator.register(ModItems.STORMCYCLE_DISC, Models.GENERATED);



    }
}
