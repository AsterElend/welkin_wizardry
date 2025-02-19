package aster.welkin_wizardry.datagen;

import aster.welkin_wizardry.block.ModBlocks;
import aster.welkin_wizardry.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;

public class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator){
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.STARSTONE);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator){
        itemModelGenerator.register(ModItems.GRIMOIRE, Models.GENERATED);
        itemModelGenerator.register(ModItems.STORMPHRAX, Models.GENERATED);
        itemModelGenerator.register(ModItems.STELLARIUM, Models.GENERATED);
        itemModelGenerator.register(ModItems.ZEPHYRITE, Models.GENERATED);

    }
}
