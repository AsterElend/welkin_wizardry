package aster.welkin.client.beRenderers;

import aster.welkin.api.PedestalLikeBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

public class PedestalRenderableRenderer<T extends PedestalLikeBlockEntity> implements BlockEntityRenderer<T> {
    private final float height;
    public PedestalRenderableRenderer(BlockEntityRendererFactory.Context ctx, float height) {
        this.height = height;
    }


    @Override
    public void render(PedestalLikeBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay){
        ItemStack stack = entity.getStack();
        if (stack.isEmpty()) return;

        matrices.push();
        matrices.translate(0.5, height, 0.5);

        //time rotation
        float time = (entity.getWorld().getTime() + tickDelta) / 20.0F;
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(time));

        matrices.scale(1.2f, 1.2f, 1.2f);

        MinecraftClient.getInstance().getItemRenderer().renderItem(
                stack,
                ModelTransformationMode.GROUND,
                light,
                overlay,
                matrices,
                vertexConsumers,
                entity.getWorld(),
                (int) entity.getPos().asLong()
        );
        matrices.pop();
    }


}
