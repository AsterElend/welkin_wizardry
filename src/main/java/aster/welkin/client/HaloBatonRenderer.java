package aster.welkin.client;


import aster.welkin.item.baton.BricklayerBatonItem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class HaloBatonRenderer {

    private static final double ORBIT_RADIUS = 0.6;
    private static final double ORBIT_SPEED = 2.5; // radians per second
    private static final float BLOCK_SCALE = 0.25f;

    public static void register() {
        WorldRenderEvents.AFTER_ENTITIES.register(HaloBatonRenderer::render);
    }

    private static void render(WorldRenderContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = context.world();
        MatrixStack matrices = context.matrixStack();
        VertexConsumerProvider consumers = context.consumers();
        if (matrices == null || consumers == null) return;

        Vec3d cameraPos = context.camera().getPos();
        float tickDelta = context.tickDelta();

        for (AbstractClientPlayerEntity player : world.getPlayers()) {
            for (Hand hand : Hand.values()) {
                ItemStack stack = player.getStackInHand(hand);
                if (!(stack.getItem() instanceof BricklayerBatonItem)) continue;

                List<String> blockIds = readSavedBlocks(stack);
                if (blockIds.isEmpty()) continue;

                Vec3d handPos = getHandPos(player, hand, tickDelta);
                renderOrbit(player, blockIds, handPos, cameraPos, matrices, consumers,
                        world, client, tickDelta);
            }
        }
    }

    private static List<String> readSavedBlocks(ItemStack stack) {
        List<String> ids = new ArrayList<>();
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) return ids;
        for (int i = 0; i < 16; i++) {
            String key = "savedBlock" + i;
            if (nbt.contains(key)) {
                ids.add(nbt.getString(key));
            }
        }
        return ids;
    }

    // Approximate world-space hand position, offset from player body.
    private static Vec3d getHandPos(AbstractClientPlayerEntity player, Hand hand, float tickDelta) {
        Vec3d base = player.getLerpedPos(tickDelta).add(0, player.getStandingEyeHeight() * 0.6, 0);

        boolean mainIsRight = player.getMainArm() == Arm.RIGHT;
        boolean isMainHand = hand == Hand.MAIN_HAND;
        boolean useRightSide = isMainHand == mainIsRight;

        float bodyYaw = MathHelper.lerpAngleDegrees(tickDelta, player.prevBodyYaw, player.bodyYaw);
        double rad = Math.toRadians(bodyYaw);

        // Perpendicular offset to the player's facing direction, +linkSide offset, slight forward
        double sideSign = useRightSide ? -1.0 : 1.0;
        double sideX = Math.cos(rad) * sideSign * 0.4;
        double sideZ = Math.sin(rad) * sideSign * 0.4;
        double fwdX = -Math.sin(rad) * 0.3;
        double fwdZ = Math.cos(rad) * 0.3;

        return base.add(sideX + fwdX, 0, sideZ + fwdZ);
    }

    private static void renderOrbit(AbstractClientPlayerEntity player, List<String> blockIds,
                                    Vec3d handPos, Vec3d cameraPos,
                                    MatrixStack matrices, VertexConsumerProvider consumers,
                                    ClientWorld world, MinecraftClient client, float tickDelta) {

        double time = (player.age + tickDelta) / 20.0; // seconds
        int count = blockIds.size();
        int light = WorldRenderer.getLightmapCoordinates(world, BlockPos.ofFloored(handPos));

        for (int i = 0; i < count; i++) {
            String idStr = blockIds.get(i);
            Block block = Registries.BLOCK.get(new Identifier(idStr));
            if (block == Blocks.AIR) continue;
            BlockState state = block.getDefaultState();

            double angle = time * ORBIT_SPEED + (2 * Math.PI * i / count);
            double x = handPos.x + Math.cos(angle) * ORBIT_RADIUS;
            double y = handPos.y + Math.sin(angle * 1.7) * 0.1; // slight vertical bob
            double z = handPos.z + Math.sin(angle) * ORBIT_RADIUS;

            matrices.push();
            matrices.translate(x - cameraPos.x, y - cameraPos.y, z - cameraPos.z);
            matrices.scale(BLOCK_SCALE, BLOCK_SCALE, BLOCK_SCALE);
            matrices.translate(-0.5, -0.5, -0.5); // center the block model on its own origin

            client.getBlockRenderManager().renderBlockAsEntity(
                    state, matrices, consumers, light, OverlayTexture.DEFAULT_UV
            );

            matrices.pop();
        }
    }
}