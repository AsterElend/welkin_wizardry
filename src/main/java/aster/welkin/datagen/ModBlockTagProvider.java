package aster.welkin.datagen;

import aster.welkin.registry.WelkinBlocks;
import aster.welkin.registry.WelkinTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public  class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(
                WelkinBlocks.STARSTONE
        );
        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(
                WelkinBlocks.LIGHTNING_ALTAR,
                WelkinBlocks.CHARGELOG,
                WelkinBlocks.CHARGEWOOD,
                WelkinBlocks.CHARGEPLANKS,
                WelkinBlocks.STRIPPEDCHARGELOG,
                WelkinBlocks.STRIPPEDCHARGEWOOD,
                WelkinBlocks.FRACTAL_LOG,
                WelkinBlocks.FRACTAL_WOOD,
                WelkinBlocks.STRIPPED_FRACTAL_LOG,
                WelkinBlocks.STRIPPED_FRACTAL_WOOD,
                WelkinBlocks.WATCHER_LOG,
                WelkinBlocks.WATCHER_WOOD,
                WelkinBlocks.STRIPPED_WATCHER_LOG,
                WelkinBlocks.STRIPPED_WATCHER_WOOD
                );
        getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN).add(
                        WelkinBlocks.CHARGELOG,
                        WelkinBlocks.CHARGEWOOD,
                        WelkinBlocks.STRIPPEDCHARGELOG,
                        WelkinBlocks.STRIPPEDCHARGEWOOD,
                        WelkinBlocks.FRACTAL_LOG,
                        WelkinBlocks.FRACTAL_WOOD,
                        WelkinBlocks.STRIPPED_FRACTAL_LOG,
                        WelkinBlocks.STRIPPED_FRACTAL_WOOD,
                        WelkinBlocks.WATCHER_LOG,
                        WelkinBlocks.WATCHER_WOOD,
                        WelkinBlocks.STRIPPED_WATCHER_LOG,
                        WelkinBlocks.STRIPPED_WATCHER_WOOD

                );
        getOrCreateTagBuilder(WelkinTags.ABSCONDABLE).addOptionalTag(TagKey.of(RegistryKeys.BLOCK, new Identifier("c","chests")));
        getOrCreateTagBuilder(BlockTags.LOGS).add( WelkinBlocks.CHARGELOG,
                WelkinBlocks.CHARGEWOOD,
                WelkinBlocks.STRIPPEDCHARGELOG,
                WelkinBlocks.STRIPPEDCHARGEWOOD,
                WelkinBlocks.FRACTAL_LOG,
                WelkinBlocks.FRACTAL_WOOD,
                WelkinBlocks.STRIPPED_FRACTAL_LOG,
                WelkinBlocks.STRIPPED_FRACTAL_WOOD,
                WelkinBlocks.WATCHER_LOG,
                WelkinBlocks.WATCHER_WOOD,
                WelkinBlocks.STRIPPED_WATCHER_LOG,
                WelkinBlocks.STRIPPED_WATCHER_WOOD);
        getOrCreateTagBuilder(BlockTags.SAPLINGS).add(WelkinBlocks.WATCHER_SAPLING, WelkinBlocks.FRACTAL_SAPLING);
        getOrCreateTagBuilder(BlockTags.PLANKS).add(WelkinBlocks.CHARGEPLANKS, WelkinBlocks.WATCHER_PLANKS, WelkinBlocks.FRACTAL_PLANKS);
        getOrCreateTagBuilder(BlockTags.LEAVES).add(WelkinBlocks.FRACTAL_LEAVES, WelkinBlocks.WATCHER_LEAVES);


    }


}
