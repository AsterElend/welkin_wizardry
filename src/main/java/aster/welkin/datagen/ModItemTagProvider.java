package aster.welkin.datagen;

import aster.welkin.registry.WelkinBlocks;
import aster.welkin.registry.WelkinItems;
import aster.welkin.registry.WelkinTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider{
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ItemTags.PLANKS)
                .add(WelkinBlocks.CHARGEPLANKS.asItem(),
                        WelkinBlocks.WATCHER_PLANKS.asItem(),
                        WelkinBlocks.FRACTAL_PLANKS.asItem());
        getOrCreateTagBuilder(ItemTags.LOGS)
                .add(
                        WelkinBlocks.CHARGELOG.asItem(),
                        WelkinBlocks.CHARGEWOOD.asItem(),
                        WelkinBlocks.STRIPPEDCHARGELOG.asItem(),
                        WelkinBlocks.STRIPPEDCHARGEWOOD.asItem(),
                        WelkinBlocks.FRACTAL_LOG.asItem(),
                        WelkinBlocks.FRACTAL_WOOD.asItem(),
                        WelkinBlocks.STRIPPED_FRACTAL_LOG.asItem(),
                        WelkinBlocks.STRIPPED_FRACTAL_WOOD.asItem(),
                        WelkinBlocks.WATCHER_LOG.asItem(),
                        WelkinBlocks.WATCHER_WOOD.asItem(),
                        WelkinBlocks.STRIPPED_WATCHER_LOG.asItem(),
                        WelkinBlocks.STRIPPED_WATCHER_WOOD.asItem()
                );



        getOrCreateTagBuilder(ItemTags.MUSIC_DISCS)
                .add(WelkinItems.STORMCYCLE_DISC, WelkinItems.WINDTUNNEL_DISC);
        getOrCreateTagBuilder(WelkinTags.SHOW_FORCE)
                .add(
                        WelkinItems.CONDUCTOR_BATON,
                        WelkinItems.WINDSTORMER_BATON
                );
        getOrCreateTagBuilder(WelkinTags.TAKE_HEART)
                .add(WelkinItems.EXTRACTED_HEART);
        getOrCreateTagBuilder(ItemTags.SAPLINGS)
                .add(WelkinBlocks.FRACTAL_SAPLING.asItem(), WelkinBlocks.WATCHER_SAPLING.asItem());
        getOrCreateTagBuilder(ItemTags.LEAVES).add(
                WelkinBlocks.FRACTAL_LEAVES.asItem(), WelkinBlocks.WATCHER_LEAVES.asItem());
    }
}
