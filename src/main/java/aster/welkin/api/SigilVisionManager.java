package aster.welkin.api;

import aster.welkin.Welkin;
import aster.welkin.api.state.SetYouOnFireRegistryState;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SigilVisionManager {
    private static final int CHECK_INTERVAL = 10;   // ticks between checks per player
    private static final int SEARCH_RADIUS_CHUNKS = 4;
    private static final double FOV_COS_THRESHOLD = 0.5; // ~60° half-angle
    private static final double MAX_DISTANCE = 32.0;
    private static final int IGNITE_THRESHOLD = 7;
    private static final int FIRE_SECONDS = 3;

    private static final Map<UUID, Integer> tickOffsets = new HashMap<>();

    public static void tick(ServerWorld world) {
        if (!Welkin.CONFIG.BurnySigils) return;
        for (ServerPlayerEntity player : world.getPlayers()) {
            int offset = tickOffsets.computeIfAbsent(player.getUuid(), u -> u.hashCode() & 0xFF);
            if ((world.getServer().getTicks() + offset) % CHECK_INTERVAL != 0) continue;
            checkPlayer(world, player);
        }
    }

    private static void checkPlayer(ServerWorld world, ServerPlayerEntity player) {
        Vec3d eyePos = player.getCameraPosVec(1f);
        Vec3d look = player.getRotationVec(1f);
        ChunkPos center = player.getChunkPos();

        int visibleCount = 0;
        for (int dx = -SEARCH_RADIUS_CHUNKS; dx <= SEARCH_RADIUS_CHUNKS; dx++) {
            for (int dz = -SEARCH_RADIUS_CHUNKS; dz <= SEARCH_RADIUS_CHUNKS; dz++) {
                ChunkPos cp = new ChunkPos(center.x + dx, center.z + dz);
                for (BlockPos pos : SetYouOnFireRegistryState.get(world).get(cp)) {
                    if (isVisible(world, eyePos, look, pos, player)) {
                        visibleCount++;
                    }
                }
            }
        }

        if (visibleCount > IGNITE_THRESHOLD) {
            player.setFireTicks(FIRE_SECONDS * 20);
        }
    }

    private static boolean isVisible(ServerWorld world, Vec3d eyePos, Vec3d look, BlockPos pos, Entity player) {
        Vec3d center = Vec3d.ofCenter(pos);
        Vec3d toBlock = center.subtract(eyePos);
        double distSq = toBlock.lengthSquared();
        if (distSq > MAX_DISTANCE * MAX_DISTANCE) return false;

        Vec3d dir = toBlock.normalize();
        if (dir.dotProduct(look) < FOV_COS_THRESHOLD) return false; // cheap reject first

        BlockHitResult hit = world.raycast(new RaycastContext(
                eyePos, center,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                player // no entity context needed for a world raycast
        ));

        return hit.getType() == HitResult.Type.MISS || hit.getBlockPos().equals(pos);
    }
}
