package aster.welkin;

import aster.welkin.block.brazier.BrazierRenderer;
import aster.welkin.registry.ModBlockEntities;
import aster.welkin.registry.ModBlocks;
import aster.welkin.block.pylon.PylonBlockEntityRenderer;
import aster.welkin.block.node.NodeBlockEntityRenderer;
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
                ModBlockEntities.PYLON,
                PylonBlockEntityRenderer::new);

        BlockEntityRendererRegistry.register(
                ModBlockEntities.BRAZIER,
                BrazierRenderer::new
        );

    }
}
