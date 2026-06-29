package aster.welkin;

import aster.welkin.client.*;

import aster.welkin.mixin.CameraMixin;
import aster.welkin.packet.WelkinPackets;
import aster.welkin.registry.LoomFluids;
import aster.welkin.registry.ModBlockEntities;
import aster.welkin.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import aster.welkin.packet.WelkinPackets;

import static aster.welkin.packet.WelkinPackets.FIRE_NADIR_TOAST;
import static aster.welkin.packet.WelkinPackets.SYNC_WARDS;

public class WelkinClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SYNC_WARDS, (client, handler, buf, responseSender) ->{
            if (buf.readableBytes() > 9) {

                int count = buf.readInt();
                boolean warded = buf.readBoolean();

                List<Long> positions = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    positions.add(buf.readLong());
                }

                client.execute(() -> {
                    for (long pos : positions) {
                        ClientWardedState.set(pos, warded);
                    }
                });

            } else {

                long pos = buf.readLong();
                boolean warded = buf.readBoolean();

                client.execute(() -> ClientWardedState.set(pos, warded));
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(FIRE_NADIR_TOAST, (client, handler, buf, responseSender) ->{
            Text advancement = buf.readText();
            boolean isForget = buf.readBoolean();

            if (isForget){
                client.execute(()-> client.getToastManager().add(NadirToast.buildForgetToast(advancement)));
            } else {
                client.execute(()-> client.getToastManager().add(NadirToast.buildRememberToast(advancement)));

            }
        });
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.NODE, RenderLayer.getCutout());
    ClientCutsceneManager.registerPacketReceivers();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (CameraMixin.isCutsceneActive) {
                // Rotates 3 degrees per tick (takes 120 ticks / 6 seconds total)
                CameraMixin.orbitAngle += 3.0f;

                if (CameraMixin.orbitAngle >= 360.0f) {
                    // Cutscene finished! Snap back camera and notify server to unlock player
                    ClientCutsceneManager.stopSequence();
                }
            }
        });

        WorldRenderEvents.AFTER_TRANSLUCENT.register(WardedBlockRenderer::render);
        FluidRenderHandlerRegistry.INSTANCE.register(LoomFluids.LETHEAN_WATER_STATIC, LoomFluids.LETHEAN_WATER_FLOWING, SimpleFluidRenderHandler.coloredWater(0xff209f));
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), LoomFluids.LETHEAN_WATER_STATIC, LoomFluids.LETHEAN_WATER_FLOWING);
        BlockEntityRendererRegistry.register(
                ModBlockEntities.NODE,
                NodeBlockEntityRenderer::new
        );

        BlockEntityRendererRegistry.register(
                ModBlockEntities.PEDESTAL_ENTITY,
                PedestalRenderableRenderer::new);

        BlockEntityRendererRegistry.register(
                ModBlockEntities.AGONITE_ENTITY,
                PedestalRenderableRenderer::new
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
