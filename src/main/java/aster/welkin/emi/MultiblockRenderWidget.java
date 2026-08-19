package aster.welkin.emi;

import aster.welkin.recipes.StormEyeRecipe;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.glfw.GLFW;
import vazkii.patchouli.api.IMultiblock;

import java.util.Collection;


public class MultiblockRenderWidget extends Widget {
    private final Bounds bounds;
    private final StormEyeRecipe recipe;

    private float yaw = 45.0f;
    private float pitch = -30.0f;

    // Custom drag state tracking variables
    private boolean isDragging = false;
    private double lastMouseX = 0;
    private double lastMouseY = 0;

    public MultiblockRenderWidget(int x, int y, int width, int height, StormEyeRecipe recipe) {
        this.bounds = new Bounds(x, y, width, height);
        this.recipe = recipe;
    }

    @Override
    public Bounds getBounds() { return bounds; }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        // Process drag calculations directly inside the render framework loop
        if (this.isDragging) {
            // Confirm the user is actively holding down the primary click action
            if (GLFW.glfwGetMouseButton(client.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
                double deltaX = mouseX - this.lastMouseX;
                double deltaY = mouseY - this.lastMouseY;
                this.yaw += deltaX * 0.8f;
                this.pitch += deltaY * 0.8f;
                this.lastMouseX = mouseX;
                this.lastMouseY = mouseY;
            } else {
                this.isDragging = false;
            }
        }

        MatrixStack matrices = drawContext.getMatrices();
        matrices.push();

        matrices.translate(bounds.x() + bounds.width() / 2.0f, bounds.y() + bounds.height() / 2.0f, 100.0f);

        IMultiblock multiblock = recipe.getMultiblock();
        Vec3i size = multiblock.getSize();
        int maxDimension = Math.max(size.getX(), Math.max(size.getY(), size.getZ()));
        float scale = 45.0f / Math.max(maxDimension, 1);

        matrices.scale(scale, -scale, scale);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));

        matrices.translate(-size.getX() / 2.0f, -size.getY() / 2.0f, -size.getZ() / 2.0f);

        BlockRenderManager renderManager = client.getBlockRenderManager();
        VertexConsumerProvider.Immediate immediate = client.getBufferBuilders().getEntityVertexConsumers();
        long worldTicks = client.world.getTime();

        var simulation = multiblock.simulate(client.world, BlockPos.ORIGIN, BlockRotation.NONE, true);
        Collection<IMultiblock.SimulateResult> results = simulation.getSecond();

        for (IMultiblock.SimulateResult result : results) {
            if (result.getStateMatcher() != null) {
                // Patchouli natively cycles the 3D block scene tags automatically when fed client world ticks
                BlockState state = result.getStateMatcher().getDisplayedState(worldTicks);

                if (state != null && !state.isAir()) {
                    BlockPos pos = result.getWorldPosition();

                    matrices.push();
                    matrices.translate(pos.getX(), pos.getY(), pos.getZ());

                    renderManager.renderBlockAsEntity(
                            state,
                            matrices,
                            immediate,
                            0xF000F0,
                            OverlayTexture.DEFAULT_UV
                    );

                    matrices.pop();
                }
            }
        }

        immediate.draw();
        matrices.pop();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && bounds.contains(mouseX, mouseY)) {
            this.isDragging = true;
            this.lastMouseX = mouseX;
            this.lastMouseY = mouseY;
            return true;
        }
        return false;
    }
}