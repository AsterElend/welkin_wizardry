package aster.welkin.datagen;

import aster.welkin.registry.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public  class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(
                ModBlocks.STARSTONE
        );
        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(
                        ModBlocks.ALTAR,
                        ModBlocks.CHARGELOG,
                        ModBlocks.CHARGEWOOD,
                        ModBlocks.CHARGEPLANKS,
                        ModBlocks.STRIPPEDCHARGELOG,
                        ModBlocks.STRIPPEDCHARGEWOOD
                );
        getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN).add(
                        ModBlocks.CHARGELOG,
                        ModBlocks.CHARGEWOOD,
                        ModBlocks.STRIPPEDCHARGELOG,
                        ModBlocks.STRIPPEDCHARGEWOOD
                );



    }


}
