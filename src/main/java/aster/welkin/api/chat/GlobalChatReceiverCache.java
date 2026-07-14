package aster.welkin.api.chat;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GlobalChatReceiverCache {
    private static final Map<RegistryKey<World>, Set<BlockPos>> RECEIVERS = new HashMap<>();

    public static void clear(){
        RECEIVERS.clear();
    }
    public static void register(World world, BlockPos pos) {
        if (world.isClient) return;
        RECEIVERS.computeIfAbsent(world.getRegistryKey(), k -> new HashSet<>()).add(pos.toImmutable());
    }

    public static void unregister(World world, BlockPos pos) {
        if (world.isClient) return;
        Set<BlockPos> set = RECEIVERS.get(world.getRegistryKey());
        if (set != null) {
            set.remove(pos);
        }
    }

    public static Set<BlockPos> getAll(ServerWorld world) {
        return RECEIVERS.getOrDefault(world.getRegistryKey(), Set.of());
    }
}
