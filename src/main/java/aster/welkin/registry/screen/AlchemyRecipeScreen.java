package aster.welkin.registry.screen;

import aster.welkin.Welkin;
import aster.welkin.recipes.RecyclerRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class AlchemyRecipeScreen extends Screen {
    private static final Identifier BACKGROUND = Welkin.id("textures/gui/alchemy_recipe.png");
    private static final int BG_WIDTH = 176;
    private static final int BG_HEIGHT = 176;
    private static final int ORBIT_RADIUS = 50;
    private static final int ICON_SIZE = 16;

    private final Screen parent;
    private final RecyclerRecipe recipe;
    private float animationTicks;

    public AlchemyRecipeScreen(Screen parent, RecyclerRecipe recipe) {
        super(recipe.getOutput().getName());
        this.parent = parent;
        this.recipe = recipe;
    }

    @Override
    protected void init() {
        int bgY = (height - BG_HEIGHT) / 2;
        this.addDrawableChild(ButtonWidget.builder(
                        Text.translatable("gui.back"),
                        btn -> this.close()
                )
                .dimensions(width / 2 - 40, bgY + BG_HEIGHT - 24, 80, 20)
                .build());
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        int bgX = (width - BG_WIDTH) / 2;
        int bgY = (height - BG_HEIGHT) / 2;
        context.drawTexture(BACKGROUND, bgX, bgY, 0, 0, BG_WIDTH, BG_HEIGHT);

        int centerX = width / 2;
        int centerY = bgY + BG_HEIGHT / 2 - 10;

        // Output in the center
        ItemStack output = recipe.getOutput();
        context.drawItem(output, centerX - ICON_SIZE / 2, centerY - ICON_SIZE / 2);
        if (isHovering(mouseX, mouseY, centerX - ICON_SIZE / 2, centerY - ICON_SIZE / 2)) {
            context.drawItemTooltip(textRenderer, output, mouseX, mouseY);
        }

        // Ingredients orbiting around it
        List<Ingredient> ingredients = recipe.getIngredients();
        int count = ingredients.size();
        this.animationTicks += delta;

        for (int i = 0; i < count; i++) {
            double angle = (2 * Math.PI * i / count) + animationTicks * 0.008;
            int x = centerX + (int) (Math.cos(angle) * ORBIT_RADIUS) - ICON_SIZE / 2;
            int y = centerY + (int) (Math.sin(angle) * ORBIT_RADIUS) - ICON_SIZE / 2;

            ItemStack[] matching = ingredients.get(i).getMatchingStacks();
            if (matching.length == 0) continue;

            // Cycle through valid matches every second if there's more than one
            ItemStack display = matching[(int) (animationTicks / 20) % matching.length];
            context.drawItem(display, x, y);

            if (isHovering(mouseX, mouseY, x, y)) {
                context.drawItemTooltip(textRenderer, display, mouseX, mouseY);
            }
        }

        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, bgY + 8, 0xFFFFFF);
    }

    private boolean isHovering(int mouseX, int mouseY, int x, int y) {
        return mouseX >= x && mouseX < x + ICON_SIZE && mouseY >= y && mouseY < y + ICON_SIZE;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
