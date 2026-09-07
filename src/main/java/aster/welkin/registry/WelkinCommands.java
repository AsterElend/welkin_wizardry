package aster.welkin.registry;

import aster.welkin.api.BiomeCategory;
import aster.welkin.api.WeatherState;
import aster.welkin.api.state.WarpLinkState;
import aster.welkin.api.state.WeatherManager;
import aster.welkin.recipes.AlchemyRecipe;
import aster.welkin.recipes.AlchemyRecipeManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    CommandManager.literal("welkin")
                            .then(CommandManager.literal("weather")
                                    .requires(source -> source.hasPermissionLevel(2)) // op-only
                                    .then(CommandManager.argument("category", StringArgumentType.word())
                                            .suggests(WelkinCommands::suggestCategories)
                                            .then(CommandManager.argument("level", StringArgumentType.word())
                                                    .suggests(WelkinCommands::suggestLevels)
                                                    .executes(WelkinCommands::setWeather)
                                            )
                                    )
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





    private static CompletableFuture<Suggestions> suggestCategories(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        for (BiomeCategory category : BiomeCategory.values()) {
            if (category == BiomeCategory.MYSTERY) continue; // not settable, no-op category
            builder.suggest(category.name().toLowerCase(Locale.ROOT));
        }
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> suggestLevels(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        for (WeatherState state : WeatherState.values()) {
            builder.suggest(state.name().toLowerCase(Locale.ROOT));
        }
        return builder.buildFuture();
    }

    private static int setWeather(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        String categoryArg = StringArgumentType.getString(context, "category");
        String levelArg = StringArgumentType.getString(context, "level");

        BiomeCategory category;
        try {
            category = BiomeCategory.valueOf(categoryArg.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            source.sendError(Text.literal("Unknown biome category: " + categoryArg));
            return 0;
        }

        if (category == BiomeCategory.MYSTERY) {
            source.sendError(Text.literal("MYSTERY is not a settable category — it covers untagged/non-overworld biomes and always stays calm."));
            return 0;
        }

        WeatherState state;
        try {
            state = WeatherState.valueOf(levelArg.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            source.sendError(Text.literal("Unknown weather level: " + levelArg));
            return 0;
        }

        ServerWorld world = source.getWorld();
        WeatherManager manager = WeatherManager.getServerState(world);

        // Reuse the same duration table the natural cycle uses, so forced weather
        // doesn't just instantly revert next tick
       int duration =  manager.forceStateWithRandomDuration(category, state, world);
        source.sendFeedback(() -> Text.literal(
                "Set " + category.name() + " weather to " + state.name() + " for " + duration + " ticks in " + world.getRegistryKey().getValue()
        ), true);

        return 1;
    }
}
