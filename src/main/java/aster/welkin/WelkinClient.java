package aster.welkin;

import aster.welkin.block.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class WelkinClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.NODE, RenderLayer.getCutout());

    }
}
