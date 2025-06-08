package aster.welkin;

import aster.welkin.block.ModBlockEntities;
import aster.welkin.block.ModBlocks;
import aster.welkin.block.fancy.focus.FocusBlockEntityRenderer;
import aster.welkin.block.fancy.node.NodeBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class WelkinClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.NODE, RenderLayer.getCutout());

        BlockEntityRendererRegistry.register(
                ModBlockEntities.NODE,
                NodeBlockEntityRenderer::new
        );
        BlockEntityRendererRegistry.register(
                ModBlockEntities.FOCUS,
                FocusBlockEntityRenderer::new);

    }
}
