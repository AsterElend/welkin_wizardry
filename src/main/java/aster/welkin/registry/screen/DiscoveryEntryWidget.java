package aster.welkin.registry.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class DiscoveryEntryWidget extends ClickableWidget {
    private final Item item;
    private final Identifier outputId;
    private final Consumer<Identifier> onClick;

    public DiscoveryEntryWidget(int x, int y, Item item, Identifier outputId, Consumer<Identifier> onClick) {
        super(x, y, 18, 18, ItemStack.EMPTY.getName());
        this.item = item;
        this.outputId = outputId;
        this.onClick = onClick;
    }

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        if (isHovered()) {
            context.fill(getX(), getY(), getX() + width, getY() + height, 0x80FFFFFF);
        }
        ItemStack stack = new ItemStack(item);
        context.drawItem(stack, getX() + 1, getY() + 1);
        if (isHovered()) {
            context.drawItemTooltip(MinecraftClient.getInstance().textRenderer, stack, mouseX, mouseY);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        onClick.accept(outputId);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, new ItemStack(item).getName());
    }
}
