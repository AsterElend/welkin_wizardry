package aster.welkin.api.chat;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatReceiverCache {

    private static final Map<RegistryKey<World>, Multimap<ChunkPos, BlockPos>> RECEIVERS = new HashMap<>();
    public static void clear(){
        RECEIVERS.clear();
    }

    public static void register(World world, BlockPos pos) {
        if (world.isClient) return;
        RECEIVERS
                .computeIfAbsent(world.getRegistryKey(), k -> HashMultimap.create())
                .put(new ChunkPos(pos), pos);
    }

    public static void unregister(World world, BlockPos pos) {
        if (world.isClient) return;
        Multimap<ChunkPos, BlockPos> map = RECEIVERS.get(world.getRegistryKey());
        if (map != null) {
            map.remove(new ChunkPos(pos), pos);
        }
    }

    public static List<BlockPos> getNearby(ServerWorld world, BlockPos center, int radius) {
        Multimap<ChunkPos, BlockPos> map = RECEIVERS.get(world.getRegistryKey());
        if (map == null || map.isEmpty()) return List.of();

        int minChunkX = (center.getX() - radius) >> 4;
        int maxChunkX = (center.getX() + radius) >> 4;
        int minChunkZ = (center.getZ() - radius) >> 4;
        int maxChunkZ = (center.getZ() + radius) >> 4;

        List<BlockPos> result = new ArrayList<>();
        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                for (BlockPos pos : map.get(new ChunkPos(cx, cz))) {
                    if (pos.isWithinDistance(center, radius)) {
                        result.add(pos);
                    }
                }
            }
        }
        return result;
    }
}