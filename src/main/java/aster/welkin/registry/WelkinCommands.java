package aster.welkin.registry;

import aster.welkin.api.state.WarpLinkState;
import aster.welkin.recipes.AlchemyRecipe;
import aster.welkin.recipes.AlchemyRecipeManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Map;
import java.util.UUID;

public class WelkinCommands {
    public static void evoke(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("welkin")
                    .then(CommandManager.literal("alchemy")
                            .requires(src -> src.hasPermissionLevel(2))
                            .then(CommandManager.literal("check")
                                    .then(CommandManager.argument("item", ItemStackArgumentType.itemStack(registryAccess))
                                            .executes(WelkinCommands::checkItem)))));

            dispatcher.register(CommandManager.literal("welkin")
                    .then(CommandManager.literal("warplinks")
                            .requires(source -> source.hasPermissionLevel(2))
                            .executes(WelkinCommands::outputLinks)
                    )
            );
        });
    }

    private static int outputLinks(CommandContext<ServerCommandSource> context){
        var source = context.getSource();
        var world = source.getWorld();

        // Fetch the state using your custom static getter
        WarpLinkState state = WarpLinkState.get(world);
        Map<UUID, WarpLinkState.WarpRegistryEntry> entries = state.getWarpArrays();

        if (entries.isEmpty()) {
            source.sendFeedback(() -> Text.literal("No active warp links found.").formatted(Formatting.YELLOW), false);
            return Command.SINGLE_SUCCESS;
        }

        source.sendFeedback(() -> Text.literal("=== Active Warp Links ===").formatted(Formatting.GOLD), false);

        for (Map.Entry<UUID, WarpLinkState.WarpRegistryEntry> entry : entries.entrySet()) {
            UUID id = entry.getKey();
            WarpLinkState.WarpRegistryEntry data = entry.getValue();

            // Format coordinates safely
            String blackStr = data.getBlackPos() != null ? data.getBlackPos().toShortString() : "None";
            String whiteStr = data.getWhitePos() != null ? data.getWhitePos().toShortString() : "None";
            String chargedStr = data.isReturnWarpCharged() ? "Yes" : "No";

            // Build a clean, formatted text message for each entry
            Text line = Text.literal("ID: ").formatted(Formatting.GRAY)
                    .append(Text.literal(id.toString().substring(0, 8) + "... ").formatted(Formatting.AQUA))
                    .append(Text.literal("| Black: ").formatted(Formatting.GRAY))
                    .append(Text.literal(blackStr).formatted(Formatting.DARK_GRAY))
                    .append(Text.literal(" | White: ").formatted(Formatting.GRAY))
                    .append(Text.literal(whiteStr).formatted(Formatting.WHITE))
                    .append(Text.literal(" | Charged: ").formatted(Formatting.GRAY))
                    .append(Text.literal(chargedStr).formatted(data.isReturnWarpCharged() ? Formatting.GREEN : Formatting.RED));

            source.sendFeedback(() -> line, false);
        }

        return Command.SINGLE_SUCCESS;
    }


    private static int checkItem(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        Item item = ItemStackArgumentType.getItemStackArgument(ctx, "item").getItem();
        ServerCommandSource source = ctx.getSource();

        boolean base = AlchemyRecipeManager.hasRecipeOfItem(item);
        boolean cat = AlchemyRecipeManager.hasACatalystRecipe(item);
        source.sendFeedback(() -> Text.literal(Registries.ITEM.getId(item)
                + " -> base(false-pair)=" + base + ", catalyst(true-pair)=" + cat), false);

        if (base) {
            AlchemyRecipe r = AlchemyRecipeManager.getRecipe(item, false);
            source.sendFeedback(() -> Text.literal("  base output: " + Registries.ITEM.getId(r.getOutput())), false);
        }
        if (cat) {
            AlchemyRecipe r = AlchemyRecipeManager.getRecipe(item, true);
            source.sendFeedback(() -> Text.literal("  catalyst output: " + Registries.ITEM.getId(r.getOutput())
                    + ", needs catalyst block: " + Registries.BLOCK.getId(r.getCatalyst())), false);
        }
        return (base || cat) ? 1 : 0;
    }
}
