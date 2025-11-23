package aster.welkin.datagen;

import aster.welkin.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class BlockLootProvider extends FabricBlockLootTableProvider {
    public BlockLootProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.STARSTONE);
        addDrop(ModBlocks.CHARGEWOOD);
        addDrop(ModBlocks.STRIPPEDCHARGELOG);
        addDrop(ModBlocks.STRIPPEDCHARGEWOOD);
        addDrop(ModBlocks.CHARGEPLANKS);
        addDrop(ModBlocks.CHARGELOG);
        addDrop(ModBlocks.NODE);
        addDrop(ModBlocks.PYLON);
        addDrop(ModBlocks.ALTAR);
        addDrop(ModBlocks.BRAZIER);



    }
}
