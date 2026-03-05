package aster.welkin.block.transducer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Matrix4f;

public class WireConnectionRenderer {
    public static void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Camera camera) {
        World world = MinecraftClient.getInstance().world;
        if (world == null) return;


        Matrix4f matrix = matrices.peek().getPositionMatrix();
        var consumer = vertexConsumers.getBuffer(RenderLayer.getLines());


        for (var conn : WireConnectionComponent.getConnections()) {
            BlockPos a = conn.a();
            BlockPos b = conn.b();


            double camX = camera.getPos().x;
            double camY = camera.getPos().y;
            double camZ = camera.getPos().z;


            float ax = (float)(a.getX() + 0.5 - camX);
            float ay = (float)(a.getY() + 0.5 - camY);
            float az = (float)(a.getZ() + 0.5 - camZ);


            float bx = (float)(b.getX() + 0.5 - camX);
            float by = (float)(b.getY() + 0.5 - camY);
            float bz = (float)(b.getZ() + 0.5 - camZ);


            consumer.vertex(matrix, ax, ay, az).color(255, 0, 0, 255).normal(0,1,0).next();
            consumer.vertex(matrix, bx, by, bz).color(255, 0, 0, 255).normal(0,1,0).next();
        }
    }
}
