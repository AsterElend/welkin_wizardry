package aster.welkin_wizardry.datagen;

import aster.welkin_wizardry.block.ModBlocks;
import aster.welkin_wizardry.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider{
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ItemTags.PLANKS)
                .add(ModBlocks.CHARGEPLANKS.asItem());
        getOrCreateTagBuilder(ItemTags.LOGS)
                .add(ModBlocks.CHARGELOG.asItem(), ModBlocks.STRIPPEDCHARGELOG.asItem());
        getOrCreateTagBuilder(ItemTags.MUSIC_DISCS)
                .add(ModItems.STORMCYCLE_DISC);
    }
}
