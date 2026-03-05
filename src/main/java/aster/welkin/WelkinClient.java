package aster.welkin;

import aster.welkin.block.brazier.VoidBrazierRenderer;
import aster.welkin.block.extractor.ExtractorRenderer;
import aster.welkin.block.transducer.WireConnectionRenderer;

import aster.welkin.hud.ForceMeter;
import aster.welkin.registry.ModBlockEntities;
import aster.welkin.registry.ModBlocks;
import aster.welkin.block.pylon.PylonBlockEntityRenderer;
import aster.welkin.block.node.NodeBlockEntityRenderer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
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
                VoidBrazierRenderer::new
        );

         BlockEntityRendererRegistry.register(
                ModBlockEntities.EXTRACTOR,
                ExtractorRenderer::new
        );



        WorldRenderEvents.AFTER_TRANSLUCENT.register((context) -> {
            WireConnectionRenderer.render(context.matrixStack(),
                    context.consumers(), context.camera());
        });

     HudRenderCallback.EVENT.register(new ForceMeter());


    }
}
