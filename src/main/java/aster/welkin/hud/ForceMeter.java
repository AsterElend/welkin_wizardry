package aster.welkin.hud;


import aster.welkin.cc.SkyForceComponent;
import aster.welkin.cc.WelkinEntityCC;
import aster.welkin.registry.WelkinTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ForceMeter implements HudRenderCallback {

    private static final Identifier TEXTURE_FULL =
            new Identifier("welkin", "textures/hud/jarfull.png");
    private static final Identifier TEXTURE_BG =
            new Identifier("welkin", "textures/hud/jar.png");

    private static final int TEX_WIDTH = 32;
    private static final int TEX_HEIGHT = 32;

    private static final int OFFSET_X = 95;
    private static final int OFFSET_Y = 45;

    @Override
    public void onHudRender(DrawContext ctx, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;

        PlayerEntity player = client.player;
        ItemStack held = player.getMainHandStack();
        SkyForceComponent force = WelkinEntityCC.FORCE.get(player);

        // Only render if holding the correct item or force is not full
        if (!held.isIn(WelkinTags.SHOW_FORCE) && force.getForce() == SkyForceComponent.MAX_FORCE) return;

        float fill = (float) force.getForce() / SkyForceComponent.MAX_FORCE;

        int screenW = client.getWindow().getScaledWidth();
        int screenH = client.getWindow().getScaledHeight();

        int x = screenW / 2 + OFFSET_X;
        int y = screenH - OFFSET_Y;

        // Draw background first
        ctx.drawTexture(
                TEXTURE_BG,
                x,
                y - TEX_HEIGHT, // align top-left of texture
                0,
                0,
                TEX_WIDTH,
                TEX_HEIGHT,
                TEX_WIDTH,
                TEX_HEIGHT
        );

        // Draw the filled portion on top
        drawMeter(ctx, x, y, fill);

        //debug
        String debugText = "Force: " + force.getForce() + "/" + SkyForceComponent.MAX_FORCE;
        int textX = x;           // You can adjust this to move text horizontally
        int textY = y - TEX_HEIGHT - 10; // Draw above the meter
        ctx.drawText(
                client.textRenderer,
                debugText,
                textX,
                textY,
                0xFFFFFF, // White color
                true      // Shadow
        );
    }

    private void drawMeter(DrawContext ctx, int x, int y, float fill) {
        int drawH = (int) (TEX_HEIGHT * fill);
        if (drawH <= 0) return;

        int v = TEX_HEIGHT - drawH;

        ctx.drawTexture(
                TEXTURE_FULL,
                x,
                y - drawH,   // Move up as it fills
                0,
                v,           // Start from bottom of texture
                TEX_WIDTH,
                drawH,
                TEX_WIDTH,
                TEX_HEIGHT
        );
    }
}