package aster.welkin.client;

import aster.welkin.api.Linkable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class TransducerRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    public TransducerRenderer(BlockEntityRendererFactory.Context ctx) {

    }

    @Override
    public boolean rendersOutsideBoundingBox(T blockEntity) {
        return true;
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
       if (!(entity instanceof Linkable linkable)) return;
       if (linkable.getLinkPos() == null) return;
       BlockPos from = entity.getPos();
       BlockPos to = linkable.getLinkPos();

       Vec3d start = new Vec3d(0.5, 0.5, 0.5); // this block's center
        Vec3d end = new Vec3d(
                to.getX() - from.getX() + 0.5,
                to.getY() - from.getY() + 0.5,
                to.getZ() - from.getZ() + 0.5
        );
        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getLines());
        drawLine(matrices, consumer, start, end);
    }


    private static void drawLine(MatrixStack matrices, VertexConsumer consumer,
                                 Vec3d start, Vec3d end) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        Matrix3f normalMatrix = matrices.peek().getNormalMatrix();

        int color = 0xbff7ff;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8)  & 0xFF;
        int b =  color        & 0xFF;

        Vec3d dir = end.subtract(start).normalize();
        float nx = (float) dir.x;
        float ny = (float) dir.y;
        float nz = (float) dir.z;

        consumer.vertex(matrix, (float) start.x, (float) start.y, (float) start.z)
                .color(r, g, b, 255)
                .normal(normalMatrix, nx, ny, nz)
                .next();
        consumer.vertex(matrix, (float) end.x, (float) end.y, (float) end.z)
                .color(r, g, b, 255)
                .normal(normalMatrix, nx, ny, nz)
                .next();
    }
}
