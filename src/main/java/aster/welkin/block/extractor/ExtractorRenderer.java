package aster.welkin.block.extractor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

public class ExtractorRenderer implements BlockEntityRenderer<ExtractorBlockEntity> {

    public ExtractorRenderer(BlockEntityRendererFactory.Context ctx){}

    @Override
    public void render(ExtractorBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {

        ItemStack stack = entity.getItem();
        if (stack.isEmpty()) return;

        float time = (entity.getWorld() != null ? entity.getWorld().getTime() : 0) + tickDelta;
        float rotation = time % 360;

        matrices.push();

        // Position
        matrices.translate(0.5, 1.2, 0.5);

        // Rotation
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));

        // Scale
        matrices.scale(0.5f, 0.5f, 0.5f);

        MinecraftClient.getInstance().getItemRenderer().renderItem(
                stack, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, null, 0
        );

        matrices.pop();
    }
}
