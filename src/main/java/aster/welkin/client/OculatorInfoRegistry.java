package aster.welkin.client;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.StringVisitable;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class OculatorInfoRegistry {
    //thank you, hexcasting, for working out how to do a system like this
    private static final ConcurrentMap<Identifier, OverlayBuilder> ID_LOOKUP = new ConcurrentHashMap<>();
    private static final List<Pair<OverlayPredicate, OverlayBuilder>> PREDICATE_LOOKUP = new Vector<>();

    // --- new entity-based fields ---
    private static final ConcurrentMap<Identifier, EntityOverlayBuilder> ENTITY_ID_LOOKUP = new ConcurrentHashMap<>();
    private static final List<Pair<EntityOverlayPredicate, EntityOverlayBuilder>> ENTITY_PREDICATE_LOOKUP = new Vector<>();

    // ... existing addDisplayer/addPredicateDisplayer/getLines methods unchanged ...

    /**
     * Add an entity type to display things when the player is holding a lens and looking at it.
     *
     * @throws IllegalArgumentException if the entity type is already registered.
     */
    public static void addEntityDisplayer(EntityType<?> type, EntityOverlayBuilder displayer) {
        addEntityDisplayer(EntityType.getId(type), displayer);
    }

    /**
     * Add an entity type ID to display things when the player is holding a lens and looking at it.
     *
     * @throws IllegalArgumentException if the entity type ID is already registered.
     */
    public static void addEntityDisplayer(Identifier entityTypeId, EntityOverlayBuilder displayer) {
        if (ENTITY_ID_LOOKUP.containsKey(entityTypeId)) {
            throw new IllegalArgumentException("Already have a displayer for entity " + entityTypeId);
        }
        ENTITY_ID_LOOKUP.put(entityTypeId, displayer);
    }

    /**
     * Display things when the player is holding a lens and looking at an entity matching a predicate.
     * <p>
     * These have lower priority than ID-based displays — if both match, only the ID-based one shows.
     */
    public static void addEntityPredicateDisplayer(EntityOverlayPredicate predicate, EntityOverlayBuilder displayer) {
        ENTITY_PREDICATE_LOOKUP.add(new Pair<>(predicate, displayer));
    }

    /**
     * Internal use only.
     */
    public static @NotNull List<Pair<ItemStack, StringVisitable>> getEntityLines(Entity entity,
                                                                                 PlayerEntity observer,
                                                                                 World world) {
        List<Pair<ItemStack, StringVisitable>> lines = Lists.newArrayList();
        var idLookedUp = ENTITY_ID_LOOKUP.get(EntityType.getId(entity.getType()));
        if (idLookedUp != null) {
            idLookedUp.addLines(lines, entity, observer, world);
        }

        for (var pair : ENTITY_PREDICATE_LOOKUP) {
            if (pair.getLeft().test(entity, observer, world)) {
                pair.getRight().addLines(lines, entity, observer, world);
            }
        }

        return lines;
    }

    /**
     * Return the lines displayed by the cursor for an entity target: an item and some text.
     */
    @FunctionalInterface
    public interface EntityOverlayBuilder {
        void addLines(List<Pair<ItemStack, StringVisitable>> lines,
                      Entity entity, PlayerEntity observer, World world);
    }

    /**
     * Predicate for matching on an entity.
     */
    @FunctionalInterface
    public interface EntityOverlayPredicate {
        boolean test(Entity entity, PlayerEntity observer, World world);
    }
    /**
     * Add the block to display things when the player is holding a lens and looking at it.
     *
     * @throws IllegalArgumentException if the block is already registered.
     */
    public static void addDisplayer(Block block, OverlayBuilder displayer) {
        addDisplayer(Registries.BLOCK.getId(block), displayer);
    }

    /**
     * Add the block to display things when the player is holding a lens and looking at it.
     *
     * @throws IllegalArgumentException if the block ID is already registered.
     */
    public static void addDisplayer(Identifier blockID, OverlayBuilder displayer) {
        if (ID_LOOKUP.containsKey(blockID)) {
            throw new IllegalArgumentException("Already have a displayer for " + blockID);
        }
        ID_LOOKUP.put(blockID, displayer);
    }

    /**
     * Display things when the player is holding a lens and looking at some block via a predicate.
     * <p>
     * These have a lower priority than the standard ID-based displays, so if an ID and predicate both match,
     * this won't be displayed.
     */
    public static void addPredicateDisplayer(OverlayPredicate predicate, OverlayBuilder displayer) {
        PREDICATE_LOOKUP.add(new Pair<>(predicate, displayer));
    }

    /**
     * Internal use only.
     */
    public static @NotNull List<Pair<ItemStack, StringVisitable>> getLines(BlockState state, BlockPos pos,
                                                                           PlayerEntity observer, World world,
                                                                           Direction hitFace) {
        List<Pair<ItemStack, StringVisitable>> lines = Lists.newArrayList();
        var idLookedup = ID_LOOKUP.get(Registries.BLOCK.getKey(state.getBlock()));
        if (idLookedup != null) {
            idLookedup.addLines(lines, state, pos, observer, world, hitFace);
        }

        for (var pair : PREDICATE_LOOKUP) {
            if (pair.getLeft().test(state, pos, observer, world, hitFace)) {
                pair.getRight().addLines(lines, state, pos, observer, world, hitFace);
            }
        }

        return lines;
    }

    /**
     * Return the lines displayed by the cursor: an item and some text.
     * <p>
     * The ItemStack can be empty; if it is, the text isn't shifted over for it.
     */
    @FunctionalInterface
    public interface OverlayBuilder {
        void addLines(List<Pair<ItemStack, StringVisitable>> lines,
                      BlockState state, BlockPos pos, PlayerEntity observer,
                      World world,
                      Direction hitFace);
    }

    /**
     * Predicate for matching on a block state.
     */
    @FunctionalInterface
    public interface OverlayPredicate {
        boolean test(BlockState state, BlockPos pos, PlayerEntity observer,
                     World world,
                     Direction hitFace);
    }
}