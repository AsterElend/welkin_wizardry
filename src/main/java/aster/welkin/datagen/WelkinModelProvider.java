package aster.welkin.datagen;

import aster.welkin.registry.ModBlocks;
import aster.welkin.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class WelkinModelProvider extends FabricModelProvider {
    public WelkinModelProvider(FabricDataOutput output) {
        super(output);
    }



    @Override
    public void generateBlockStateModels(BlockStateModelGenerator Bgenerator){



        Bgenerator.registerSimpleCubeAll(ModBlocks.CHARGEPLANKS);
        Bgenerator.registerRotatable(ModBlocks.MOTHTILE);
        Bgenerator.registerLog(ModBlocks.CHARGELOG).log(ModBlocks.CHARGELOG).wood(ModBlocks.CHARGEWOOD);
        Bgenerator.registerLog(ModBlocks.STRIPPEDCHARGELOG).log(ModBlocks.STRIPPEDCHARGELOG).wood(ModBlocks.STRIPPEDCHARGEWOOD);

        Bgenerator.registerSimpleCubeAll(ModBlocks.STARSTONE);
        Bgenerator.registerSimpleCubeAll(ModBlocks.VOIDSTONE);
        Bgenerator.registerSimpleCubeAll(ModBlocks.FRACTAL_PLANKS);
        Bgenerator.registerSimpleCubeAll(ModBlocks.FRACTAL_LEAVES);
        Bgenerator.registerLog(ModBlocks.FRACTAL_LOG).log(ModBlocks.FRACTAL_LOG).wood(ModBlocks.FRACTAL_WOOD);
        Bgenerator.registerLog(ModBlocks.STRIPPED_FRACTAL_LOG).log(ModBlocks.STRIPPED_FRACTAL_LOG).wood(ModBlocks.STRIPPED_FRACTAL_WOOD);

        Bgenerator.registerTintableCross(ModBlocks.FRACTAL_SAPLING, BlockStateModelGenerator.TintType.NOT_TINTED);

        Bgenerator.registerSimpleCubeAll(ModBlocks.WATCHER_PLANKS);
        Bgenerator.registerSimpleCubeAll(ModBlocks.WATCHER_LEAVES);
        Bgenerator.registerLog(ModBlocks.WATCHER_LOG).log(ModBlocks.WATCHER_LOG).wood(ModBlocks.WATCHER_WOOD);
        Bgenerator.registerLog(ModBlocks.STRIPPED_WATCHER_LOG).log(ModBlocks.STRIPPED_WATCHER_LOG).wood(ModBlocks.STRIPPED_WATCHER_WOOD);
        Bgenerator.registerTintableCross(ModBlocks.WATCHER_SAPLING, BlockStateModelGenerator.TintType.NOT_TINTED);




    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator){
        itemModelGenerator.register(ModItems.GRIMORE, Models.GENERATED);
        itemModelGenerator.register(ModItems.STORMPHRAX, Models.GENERATED);
        itemModelGenerator.register(ModItems.STELLARIUM, Models.GENERATED);
        itemModelGenerator.register(ModItems.ZEPHYRITE, Models.GENERATED);
        itemModelGenerator.register(ModItems.CHARGESTONE, Models.GENERATED);
        itemModelGenerator.register(ModItems.GALVANIC_WAND, Models.HANDHELD_ROD);
        itemModelGenerator.register(ModItems.STORMCYCLE_DISC, Models.GENERATED);
        itemModelGenerator.register(ModItems.WINDTUNNEL_DISC, Models.GENERATED);
        itemModelGenerator.register(ModItems.GUST_BATON, Models.HANDHELD_ROD);
        itemModelGenerator.register(ModItems.HALO_BATON, Models.HANDHELD_ROD);
        itemModelGenerator.register(ModItems.WIRE, Models.GENERATED);
        itemModelGenerator.register(ModItems.WARDING_PRISM, Models.GENERATED);
        itemModelGenerator.register(ModItems.LETHEAN_WATER_BUCKET, Models.GENERATED);
        itemModelGenerator.register(ModItems.LETHEAN_WATER_BOTTLE, Models.GENERATED);
        itemModelGenerator.register(ModItems.WORLD_SALTS, Models.GENERATED);
        itemModelGenerator.register(ModItems.CRYSTAL_HEART, Models.GENERATED);
        itemModelGenerator.register(ModItems.EXTRACTED_HEART, Models.GENERATED);





    }
}
