package aster.welkin.datagen;

import aster.welkin.registry.WelkinBlocks;
import aster.welkin.registry.WelkinItems;
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



        Bgenerator.registerSimpleCubeAll(WelkinBlocks.CHARGEPLANKS);
        Bgenerator.registerRotatable(WelkinBlocks.MOTHTILE);
        Bgenerator.registerLog(WelkinBlocks.CHARGELOG).log(WelkinBlocks.CHARGELOG).wood(WelkinBlocks.CHARGEWOOD);
        Bgenerator.registerLog(WelkinBlocks.STRIPPEDCHARGELOG).log(WelkinBlocks.STRIPPEDCHARGELOG).wood(WelkinBlocks.STRIPPEDCHARGEWOOD);

        Bgenerator.registerSimpleCubeAll(WelkinBlocks.STARSTONE);
        Bgenerator.registerSimpleCubeAll(WelkinBlocks.VOIDSTONE);
        Bgenerator.registerSimpleCubeAll(WelkinBlocks.FRACTAL_PLANKS);
        Bgenerator.registerSimpleCubeAll(WelkinBlocks.FRACTAL_LEAVES);
        Bgenerator.registerLog(WelkinBlocks.FRACTAL_LOG).log(WelkinBlocks.FRACTAL_LOG).wood(WelkinBlocks.FRACTAL_WOOD);
        Bgenerator.registerLog(WelkinBlocks.STRIPPED_FRACTAL_LOG).log(WelkinBlocks.STRIPPED_FRACTAL_LOG).wood(WelkinBlocks.STRIPPED_FRACTAL_WOOD);

        Bgenerator.registerTintableCross(WelkinBlocks.FRACTAL_SAPLING, BlockStateModelGenerator.TintType.NOT_TINTED);

        Bgenerator.registerSimpleCubeAll(WelkinBlocks.WATCHER_PLANKS);
        Bgenerator.registerSimpleCubeAll(WelkinBlocks.WATCHER_LEAVES);
        Bgenerator.registerLog(WelkinBlocks.WATCHER_LOG).log(WelkinBlocks.WATCHER_LOG).wood(WelkinBlocks.WATCHER_WOOD);
        Bgenerator.registerLog(WelkinBlocks.STRIPPED_WATCHER_LOG).log(WelkinBlocks.STRIPPED_WATCHER_LOG).wood(WelkinBlocks.STRIPPED_WATCHER_WOOD);
        Bgenerator.registerTintableCross(WelkinBlocks.WATCHER_SAPLING, BlockStateModelGenerator.TintType.NOT_TINTED);




    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator){
        itemModelGenerator.register(WelkinItems.GRIMORE, Models.GENERATED);
        itemModelGenerator.register(WelkinItems.STORMPHRAX, Models.GENERATED);
        itemModelGenerator.register(WelkinItems.STELLARIUM, Models.GENERATED);
        itemModelGenerator.register(WelkinItems.ZEPHYRITE, Models.GENERATED);
        itemModelGenerator.register(WelkinItems.CHARGESTONE, Models.GENERATED);
        itemModelGenerator.register(WelkinItems.CONDUCTOR_BATON, Models.HANDHELD_ROD);
        itemModelGenerator.register(WelkinItems.STORMCYCLE_DISC, Models.GENERATED);
        itemModelGenerator.register(WelkinItems.WINDTUNNEL_DISC, Models.GENERATED);
        itemModelGenerator.register(WelkinItems.WINDSTORMER_BATON, Models.HANDHELD_ROD);
        itemModelGenerator.register(WelkinItems.BRICKLAYER_BATON, Models.HANDHELD_ROD);
        itemModelGenerator.register(WelkinItems.WARDING_PRISM, Models.GENERATED);
        itemModelGenerator.register(WelkinItems.LETHEAN_WATER_BUCKET, Models.GENERATED);
        itemModelGenerator.register(WelkinItems.LETHEAN_WATER_BOTTLE, Models.GENERATED);
        itemModelGenerator.register(WelkinItems.WORLD_SALTS, Models.GENERATED);
        itemModelGenerator.register(WelkinItems.CRYSTAL_HEART, Models.GENERATED);
        itemModelGenerator.register(WelkinItems.EXTRACTED_HEART, Models.GENERATED);
        itemModelGenerator.register(WelkinItems.OCULATOR_LENS, Models.GENERATED);





    }
}
