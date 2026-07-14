package aster.welkin.registry.screen;

import aster.welkin.Welkin;
import aster.welkin.cc.WelkinEntityCC;
import aster.welkin.recipes.RecyclerRecipe;
import aster.welkin.recipes.RecyclerRecipeManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class AlchemySlateScreen extends Screen {
    private static final Identifier BACKGROUND = Welkin.id("textures/gui/alchemy_slate.png");
    private static final int BG_WIDTH = 176;
    private static final int BG_HEIGHT = 200;
    private static final int ENTRIES_PER_ROW = 7;
    private static final int SLOT_SIZE = 20;
    private  final boolean debug;
    private final List<Identifier> discoveries;

    public AlchemySlateScreen(PlayerEntity player, boolean debug) {
        super(Text.translatable("screen.welkin.alchemy_slate"));
        this.discoveries = player != null
                ? WelkinEntityCC.DISCOVERIES.get(player).getDiscoveries()
                : List.of();
        this.debug = debug;
    }

    @Override
    protected void init() {
        int bgX = (width - BG_WIDTH) / 2;
        int bgY = (height - BG_HEIGHT) / 2;
        int startX = bgX + 12;
        int startY = bgY + 24;
        if (!debug){
            for (int i = 0; i < discoveries.size(); i++) {
                Identifier outputId = discoveries.get(i);
                Item item = Registries.ITEM.get(outputId);
                int col = i % ENTRIES_PER_ROW;
                int row = i / ENTRIES_PER_ROW;
                int x = startX + col * SLOT_SIZE;
                int y = startY + row * SLOT_SIZE;

                this.addDrawableChild(new DiscoveryEntryWidget(x, y, item, outputId, this::openRecipe));
            }
        } else {
            for (int i = 0; i < RecyclerRecipeManager.getOutputIdentifiersForAlchemySlate().size(); i++) {
                Item item = RecyclerRecipeManager.getOutputItemsForDebugAlchemySlate().get(i);
                 Identifier outputId = RecyclerRecipeManager.getOutputIdentifiersForAlchemySlate().get(i);
                int col = i % ENTRIES_PER_ROW;
                int row = i / ENTRIES_PER_ROW;
                int x = startX + col * SLOT_SIZE;
                int y = startY + row * SLOT_SIZE;

                this.addDrawableChild(new DiscoveryEntryWidget(x, y, item, outputId, this::openRecipe));
            }
        }

    }

    private void openRecipe(Identifier outputId) {
        RecyclerRecipe recipe = RecyclerRecipeManager.getRecipeForOutput(outputId);
        if (recipe != null) {
            MinecraftClient.getInstance().setScreen(new AlchemyRecipeScreen(this, recipe));
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        int bgX = (width - BG_WIDTH) / 2;
        int bgY = (height - BG_HEIGHT) / 2;
        context.drawTexture(BACKGROUND, bgX, bgY, 0, 0, BG_WIDTH, BG_HEIGHT);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, bgY + 8, 0xFFFFFF);

        if (discoveries.isEmpty()) {
            context.drawCenteredTextWithShadow(textRenderer,
                    Text.translatable("screen.welkin.alchemy_slate.empty"),
                    width / 2, height / 2, 0x888888);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}