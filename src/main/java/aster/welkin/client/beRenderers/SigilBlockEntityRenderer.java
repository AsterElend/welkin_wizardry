package aster.welkin.client.beRenderers;

import aster.welkin.Welkin;
import aster.welkin.block.sigil.Sigil;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

public class SigilBlockEntityRenderer<T extends Sigil> implements BlockEntityRenderer<T> {
    private static final float CIRCLE_SPEED = -1.2f; // degrees/tick, negative = counterclockwise
    private static final float SIGIL_SPEED  =  0.8f; // degrees/tick, clockwise
    private static final float QUAD_HALF_SIZE = 7.0f / 16.0f; // matches the 1px inset from your shapes
    private static final float FACE_INSET = 1.0f / 16.0f;     // pushes quad flush to the face
    private static final float LAYER_GAP = 1.0f / 512.0f;     // avoids z-fighting between the two quads

    public SigilBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, int overlay) {

        if (!Welkin.CONFIG.FancySigils) return; // safety net in case the flag toggles without a block update
        if (entity == null) return;
        World world = entity.getWorld();
        if (world == null) return;

        BlockState state = entity.getCachedState();
        Direction facing = state.get(Properties.FACING);

        float age = world.getTime() + tickDelta;
        float circleAngle = (age * CIRCLE_SPEED) % 360.0f;
        float sigilAngle  = (age * SIGIL_SPEED) % 360.0f;

        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        alignToFace(matrices, facing);
        matrices.translate(0, 0, 0.5 - FACE_INSET);

        Identifier circleTex = resolveTexture(Welkin.id("block/sigil/sigil_circle"));
        Identifier sigilTex = resolveTexture(entity.getTexture());

        VertexConsumer circleBuffer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(circleTex));
        drawQuad(matrices, circleBuffer, circleAngle, -LAYER_GAP, light, overlay);

        VertexConsumer sigilBuffer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(sigilTex));
        drawQuad(matrices, sigilBuffer, sigilAngle, 0, light, overlay);

        matrices.pop();
    }

    /** Rotates local +Z to point along the block's outward facing normal. */
    private void alignToFace(MatrixStack matrices, Direction facing) {
        switch (facing) {
            case UP    -> matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));
            case DOWN  -> matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
            case NORTH -> matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
            case SOUTH -> {}
            case EAST  -> matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));  // was -90
            case WEST  -> matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90)); // was 90
        }
    }

    private void drawQuad(MatrixStack matrices, VertexConsumer buffer, float spinDegrees, float zOffset,
                          int light, int overlay) {
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(spinDegrees));
        matrices.translate(0, 0, zOffset);

        MatrixStack.Entry entry = matrices.peek();
        float s = QUAD_HALF_SIZE;

        // front face, normal +Z
        vertex(buffer, entry, -s, -s, 0, 1, light, overlay, 1);
        vertex(buffer, entry,  s, -s, 1, 1, light, overlay, 1);
        vertex(buffer, entry,  s,  s, 1, 0, light, overlay, 1);
        vertex(buffer, entry, -s,  s, 0, 0, light, overlay, 1);

        // back face, reversed winding, normal -Z
        vertex(buffer, entry, -s,  s, 0, 0, light, overlay, -1);
        vertex(buffer, entry,  s,  s, 1, 0, light, overlay, -1);
        vertex(buffer, entry,  s, -s, 1, 1, light, overlay, -1);
        vertex(buffer, entry, -s, -s, 0, 1, light, overlay, -1);

        matrices.pop();
    }
    private void vertex(VertexConsumer buffer, MatrixStack.Entry entry, float x, float y, float u, float v,
                        int light, int overlay, float normalZ) {
        buffer.vertex(entry.getPositionMatrix(), x, y, 0)
                .color(255, 255, 255, 255)
                .texture(u, v)
                .overlay(overlay)
                .light(light)
                .normal(entry.getNormalMatrix(), 0, 0, normalZ)
                .next();
    }

    private static Identifier resolveTexture(Identifier id) {
        return id.withPath("textures/" + id.getPath() + ".png");
    }
}

