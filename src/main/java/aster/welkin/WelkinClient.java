package aster.welkin;

import aster.welkin.client.*;
import aster.welkin.item.GenericScreenInvocationItem;
import aster.welkin.registry.LoomFluids;
import aster.welkin.registry.WelkinBlockEntities;
import aster.welkin.registry.WelkinBlocks;
import aster.welkin.registry.screen.AlchemySlateScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;

import java.util.ArrayList;
import java.util.List;

import static aster.welkin.packet.WelkinPackets.FIRE_NADIR_TOAST;
import static aster.welkin.packet.WelkinPackets.SYNC_WARDS;

public class WelkinClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

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
        FluidRenderHandlerRegistry.INSTANCE.register(LoomFluids.LETHEAN_WATER_STATIC, LoomFluids.LETHEAN_WATER_FLOWING, SimpleFluidRenderHandler.coloredWater(0xff209f));
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), LoomFluids.LETHEAN_WATER_STATIC, LoomFluids.LETHEAN_WATER_FLOWING);
        BlockEntityRendererRegistry.register(
                WelkinBlockEntities.NODE,
                NodeBlockEntityRenderer::new
        );

        BlockEntityRendererRegistry.register(
                WelkinBlockEntities.PYLON,
                PedestalRenderableRenderer::new);

        BlockEntityRendererRegistry.register(
                WelkinBlockEntities.AGONITE_TRANSMUTER,
                PedestalRenderableRenderer::new
        );



         BlockEntityRendererRegistry.register(
                 WelkinBlockEntities.ITEM_TRANSDUCER,
                 TransducerRenderer::new
         );

   BlockEntityRendererRegistry.register(
                 WelkinBlockEntities.FLUID_TRANSDUCER,
                 TransducerRenderer::new
         );





    }
}
