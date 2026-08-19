package aster.welkin.client.beRenderers;

import aster.welkin.block.entity.FalseStarBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class FalseStarBlockEntityRenderer implements BlockEntityRenderer<FalseStarBlockEntity> {
    public FalseStarBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}
    @Override
    public void render(FalseStarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        // Center the rays inside the block
        matrices.translate(0.5, 0.5, 0.5);

        // Animate rotation based on world time to prevent floating-point decay
        long time = entity.getWorld().getTime();
        float angle = (time + tickDelta) * 2.0F;
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angle));

        // Get the translucent glowing render layer
        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getLightning());

        // Draw multiple beam segments/cones
        for (int i = 0; i < 8; i++) {
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45.0F * i));
            drawRay(matrices, consumer, 0.1F, 2.0F); // 2 blocks long beam
        }

        matrices.pop();
    }

    private void drawRay(MatrixStack matrices, VertexConsumer consumer, float width, float length) {
        MatrixStack.Entry entry = matrices.peek();

        // Tip of the ray (bright, opaque center)
        consumer.vertex(entry.getPositionMatrix(), 0, length, 0)
                .color(255, 255, 200, 255).next();

        // Base points forming a triangle/cone (fading out to 0 alpha)
        consumer.vertex(entry.getPositionMatrix(), -width, 0, -width)
                .color(255, 200, 100, 0).next();

        consumer.vertex(entry.getPositionMatrix(), width, 0, -width)
                .color(255, 200, 100, 0).next();
    }

    @Override
    public boolean rendersOutsideBoundingBox(FalseStarBlockEntity falseStar){
        return true;
    }

}
