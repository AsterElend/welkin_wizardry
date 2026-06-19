package aster.welkin;

import aster.welkin.client.*;

import aster.welkin.packet.WelkinPackets;
import aster.welkin.registry.ModBlockEntities;
import aster.welkin.registry.ModBlocks;
import dev.emi.emi.api.EmiApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
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
                ModBlockEntities.PEDESTAL_ENTITY,
                PedestalBlockEntityRenderer::new);

        BlockEntityRendererRegistry.register(
                ModBlockEntities.VOID_BRAZIER_ENTITY,
                VoidBrazierRenderer::new
        );

         BlockEntityRendererRegistry.register(
                ModBlockEntities.ATTRIBUTE_EXTRACTOR,
                ExtractorRenderer::new
        );

         BlockEntityRendererRegistry.register(
                 ModBlockEntities.ITEM_TRANSDUCER,
                 TransducerRenderer::new
         );

   BlockEntityRendererRegistry.register(
                 ModBlockEntities.FLUID_TRANSDUCER,
                 TransducerRenderer::new
         );





    }
}
