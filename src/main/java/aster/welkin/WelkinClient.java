package aster.welkin;

import aster.welkin.api.RainbowColorProvider;
import aster.welkin.client.*;
import aster.welkin.client.beRenderers.FalseStarBlockEntityRenderer;
import aster.welkin.client.beRenderers.LinkableLineRenderer;
import aster.welkin.client.beRenderers.PedestalRenderableRenderer;
import aster.welkin.client.beRenderers.SigilBlockEntityRenderer;
import aster.welkin.packet.WeatherSyncPackets;
import aster.welkin.registry.WelkinBlockEntities;
import aster.welkin.registry.WelkinBlocks;
import aster.welkin.registry.WelkinFluids;
import aster.welkin.registry.particle.WelkinParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static aster.welkin.packet.WelkinPackets.FIRE_NADIR_TOAST;
import static aster.welkin.packet.WelkinPackets.SYNC_WARDS;

public class WelkinClient implements ClientModInitializer {
    public static int particleCount = 0;
    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.BLOCK.register(new RainbowColorProvider(), WelkinBlocks.WARDED_STONE);
        WelkinFogState.register();
        WeatherSyncPackets.registerClientReceivers();
        WeatherParticleSummoner.register();
        WelkinParticles.registerClient();
        ClientPlayConnectionEvents.JOIN.register((handler, packet, client) ->{
            particleCount = 0;
        });
        BlockEntityRendererFactories.register(WelkinBlockEntities.TENPO_SIGIL, SigilBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(WelkinBlockEntities.SULI_SIGIL, SigilBlockEntityRenderer::new);
        OculatorLensOverlays.addOculatorLensStuff();
        HudRenderCallback.EVENT.register(OculatorRendererHandler::overlayGui);
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



    BlockRenderLayerMap.INSTANCE.putBlock(WelkinBlocks.NODE, RenderLayer.getCutout());
    HaloBatonRenderer.register();

        WorldRenderEvents.AFTER_TRANSLUCENT.register(WardedBlockRenderer::render);
        FluidRenderHandlerRegistry.INSTANCE.register(WelkinFluids.LETHEAN_WATER_STATIC, WelkinFluids.LETHEAN_WATER_FLOWING, SimpleFluidRenderHandler.coloredWater(0xff209f));
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), WelkinFluids.LETHEAN_WATER_STATIC, WelkinFluids.LETHEAN_WATER_FLOWING);

        FluidRenderHandlerRegistry.INSTANCE.register(WelkinFluids.FALSE_MILK_STATIC, WelkinFluids.FALSE_MILK_FLOWING, SimpleFluidRenderHandler.coloredWater(0xffffff));


        BlockEntityRendererFactories.register(
                WelkinBlockEntities.NODE,
                ctx -> new PedestalRenderableRenderer<>(ctx, 0.5f)
        );

        BlockEntityRendererFactories.register(
                WelkinBlockEntities.FALSE_STAR,
                FalseStarBlockEntityRenderer::new
        );

        BlockEntityRendererFactories.register(
                WelkinBlockEntities.PYLON,
                ctx -> new PedestalRenderableRenderer<>(ctx, 1f)
        );


        BlockEntityRendererFactories.register(
                WelkinBlockEntities.AGONITE_TRANSMUTER,
                ctx -> new PedestalRenderableRenderer<>(ctx, 1f)
        );


        BlockEntityRendererFactories.register(
                 WelkinBlockEntities.ITEM_TRANSDUCER,
                 LinkableLineRenderer::new
         );

        BlockEntityRendererFactories.register(
                 WelkinBlockEntities.FLUID_TRANSDUCER,
                 LinkableLineRenderer::new
         );

        BlockEntityRendererFactories.register(
                 WelkinBlockEntities.AETHER_TRANSDUCER,
                 LinkableLineRenderer::new
         );


    BlockEntityRendererFactories.register(
                 WelkinBlockEntities.ALCHEMY_ENTITY,
                 LinkableLineRenderer::new
         );

    }


}
