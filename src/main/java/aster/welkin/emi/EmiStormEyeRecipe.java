package aster.welkin.emi;

import aster.welkin.recipes.StormEyeRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import vazkii.patchouli.api.IMultiblock;

import java.util.*;

public class EmiStormEyeRecipe implements EmiRecipe {
    private final StormEyeRecipe recipe;
    private final List<EmiIngredient> inputs = new ArrayList<>();
    private EmiStack itemOutput = EmiStack.EMPTY;
    private EmiStack blockOutput = EmiStack.EMPTY;

    public EmiStormEyeRecipe(StormEyeRecipe recipe) {
        this.recipe = recipe;
        if (recipe.hasBlockOutput()){
            this.blockOutput = EmiStack.of(recipe.getOutputState().getBlock());
        }
        if (recipe.hasItemOutput()){
            this.itemOutput = EmiStack.of(recipe.getOutput());
        }

        IMultiblock patchouliMulti = recipe.getMultiblock();
        var world = MinecraftClient.getInstance().world;

        if (world != null) {
            var simulation = patchouliMulti.simulate(world, BlockPos.ORIGIN, BlockRotation.NONE, true);
            Collection<IMultiblock.SimulateResult> results = simulation.getSecond();

            // Maps a structural position key string to all valid item variants for that position
            Map<String, List<EmiStack>> positionIngredients = new LinkedHashMap<>();
            // Track block totals for display multiplication if necessary, or group positions by matcher type
            Map<String, Integer> positionCounts = new LinkedHashMap<>();

            for (IMultiblock.SimulateResult result : results) {
                if (result.getStateMatcher() != null) {
                    // Unique token for grouping similar block configurations (e.g., character code or matcher string)
                    String matcherKey = result.getCharacter() != null ? String.valueOf(result.getCharacter()) : result.getStateMatcher().toString();

                    if (!positionIngredients.containsKey(matcherKey)) {
                        List<EmiStack> validStacks = new ArrayList<>();

                        // Cycle through up to 20 ticks to find all unique block variants this tag/matcher supports
                        for (int t = 0; t < 40; t++) {
                            BlockState state = result.getStateMatcher().getDisplayedState(t);
                            if (state != null && !state.isAir()) {
                                EmiStack emiStack = EmiStack.of(state.getBlock().asItem());
                                if (!emiStack.isEmpty() && !validStacks.contains(emiStack)) {
                                    validStacks.add(emiStack);
                                }
                            }
                        }

                        if (!validStacks.isEmpty()) {
                            positionIngredients.put(matcherKey, validStacks);
                            positionCounts.put(matcherKey, 1);
                        }
                    } else {
                        positionCounts.put(matcherKey, positionCounts.get(matcherKey) + 1);
                    }
                }
            }

            // Convert grouped variants into cycling ingredients with aggregated stack amounts
            for (Map.Entry<String, List<EmiStack>> entry : positionIngredients.entrySet()) {
                int totalRequired = positionCounts.get(entry.getKey());
                List<EmiStack> cycledVariants = entry.getValue().stream().map(stack -> {
                    ItemStack raw = stack.getItemStack().copy();
                    raw.setCount(totalRequired);
                    return EmiStack.of(raw);
                }).toList();

                this.inputs.add(EmiIngredient.of(cycledVariants));
            }
        }
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return WelkinEmiPlugin.STORM_EYE_CATEGORY;
    }

    @Override
    public Identifier getId() {
        return recipe.getId();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(blockOutput, itemOutput);
    }

    @Override
    public int getDisplayWidth() {
        return 144; // Standard comfortable EMI panel width
    }

    @Override
    public int getDisplayHeight() {
        return 160; // Expanded height to stack elements vertically
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        int slotX = 8;
        int slotY = 8;
        for (int i = 0; i < inputs.size(); i++) {
            widgets.addSlot(inputs.get(i), slotX + (i % 7) * 18, slotY + (i / 7) * 18);
        }

        int gridHeight = (((inputs.size() - 1) / 7) + 1) * 18;
        int arrowY = 12 + gridHeight;

        widgets.addTexture(EmiTexture.FULL_ARROW, 63, arrowY);

        int renderY = arrowY + 22;
        widgets.add(new MultiblockRenderWidget(8, renderY, 70, 70, recipe));

        int outputX = 96;
        int outputY = renderY + 24;

        if (recipe.hasBlockOutput()) {
            widgets.addSlot(blockOutput, outputX + 22, outputY).recipeContext(this);
        }
        if (recipe.hasItemOutput()){
            widgets.addSlot(blockOutput, outputX + 11, outputY).recipeContext(this);
        }

    }
}

