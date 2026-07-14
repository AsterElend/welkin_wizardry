package aster.welkin.client;

import aster.welkin.registry.WelkinItems;
import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.util.Language;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;


import java.util.List;

public class OculatorRendererHandler {

    public static void overlayGui(DrawContext graphics, float partialTicks) {
        tryRenderOculatorOverlay(graphics, partialTicks);
    }
    private static void tryRenderOculatorOverlay(DrawContext graphics, float partialTicks) {
        var mc = MinecraftClient.getInstance();
        var ps = graphics.getMatrices();

        ClientPlayerEntity player = mc.player;
        ClientWorld level = mc.world;
        if (player == null || level == null) {
            return;
        }

        if (!player.getStackInHand(player.getActiveHand()).isOf(WelkinItems.OCULATOR_LENS))
            return;

        var hitRes = mc.crosshairTarget;
        if (hitRes == null) return;

        List<Pair<ItemStack, StringVisitable>> lines;

        if (hitRes.getType() == HitResult.Type.BLOCK) {
            var bhr = (BlockHitResult) hitRes;
            var pos = bhr.getBlockPos();
            var bs = level.getBlockState(pos);
            lines = OculatorInfoRegistry.getLines(bs, pos, player, level, bhr.getSide());
        } else if (hitRes.getType() == HitResult.Type.ENTITY) {
            var ehr = (EntityHitResult) hitRes;
            lines = OculatorInfoRegistry.getEntityLines(ehr.getEntity(), player, level);
        } else {
            return;
        }

        if (lines.isEmpty()) return;

        renderLines(graphics, ps, mc, lines);
    }

    private static void renderLines(DrawContext graphics, MatrixStack ps, MinecraftClient mc,
                                    List<Pair<ItemStack, StringVisitable>> lines) {
        int totalHeight = 8;
        List<Pair<ItemStack, List<StringVisitable>>> actualLines = Lists.newArrayList();

        var window = mc.getWindow();
        var maxWidth = (int) (window.getScaledWidth() / 2f * 0.8f);

        for (var pair : lines) {
            totalHeight += mc.textRenderer.fontHeight + 6;
            var text = pair.getRight();
            var textLines = mc.textRenderer.getTextHandler().wrapLines(text, maxWidth, Style.EMPTY);

            actualLines.add(new Pair<>(pair.getLeft(), textLines));

            if (textLines.size() > 1) {
                totalHeight += mc.textRenderer.fontHeight * (textLines.size() - 1);
            }
        }

        var x = window.getScaledWidth() / 2f + 8f;
        var y = window.getScaledHeight() / 2f - totalHeight;
        ps.push();
        ps.translate(x, y, 0);

        for (var pair : actualLines) {
            var stack = pair.getLeft();
            if (!stack.isEmpty()) {
                graphics.drawItem(pair.getLeft(), 0, 0);
            }
            int tx = stack.isEmpty() ? 0 : 18;
            int ty = 5;
            var text = pair.getRight();

            for (var line : text) {
                var actualLine = Language.getInstance().reorder(line);
                graphics.drawTextWithShadow(mc.textRenderer, actualLine, tx, ty, 0xffffffff);
                ps.translate(0, mc.textRenderer.fontHeight, 0);
            }
            if (text.isEmpty()) {
                ps.translate(0, mc.textRenderer.fontHeight, 0);
            }
            ps.translate(0, 6, 0);
        }

        ps.pop();
    }
}
