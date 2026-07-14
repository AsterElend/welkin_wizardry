package aster.welkin.datagen;

import aster.welkin.registry.WelkinBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class BlockLootProvider extends FabricBlockLootTableProvider {
    public BlockLootProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(WelkinBlocks.STARSTONE);
        addDrop(WelkinBlocks.CHARGEWOOD);
        addDrop(WelkinBlocks.STRIPPEDCHARGELOG);
        addDrop(WelkinBlocks.STRIPPEDCHARGEWOOD);
        addDrop(WelkinBlocks.CHARGEPLANKS);
        addDrop(WelkinBlocks.CHARGELOG);
        addDrop(WelkinBlocks.PYLON);
        addDrop(WelkinBlocks.LIGHTNING_ALTAR);




    }
}
