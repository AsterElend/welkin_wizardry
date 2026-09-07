package aster.welkin.mixin;

import aster.welkin.api.WeatherQueryHelper;
import aster.welkin.api.state.WeatherManager;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

    @Inject(method = "tickChunk", at = @At("HEAD")) // no longer cancellable
    private void welkin$tickChunkWeather(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        ServerWorld self = (ServerWorld)(Object)this;
        ChunkPos chunkPos = chunk.getPos();
        int startX = chunkPos.getStartX();
        int startZ = chunkPos.getStartZ();
        Random random = self.getRandom();
        Profiler profiler = ((ServerWorld) (Object) this).getProfiler();

        // --- Lightning ---
        profiler.push("thunder");
        if (random.nextInt(100000) == 0) {
            BlockPos candidate = self.getLightningPos(self.getRandomPosInChunk(startX, 0, startZ, 15));
            if (WeatherQueryHelper.isSevereAt(self, candidate)) {
                LightningEntity bolt = EntityType.LIGHTNING_BOLT.create(self);
                if (bolt != null) {
                    bolt.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(candidate));
                    bolt.setCosmetic(false);
                    self.spawnEntity(bolt);
                }
            }
        }

        // --- Ice & snow ---
        profiler.push("iceandsnow");
        if (random.nextInt(16) == 0) {
            BlockPos surfacePos = self.getTopPosition(Heightmap.Type.MOTION_BLOCKING, self.getRandomPosInChunk(startX, 0, startZ, 15));
            BlockPos belowPos = surfacePos.down();
            Biome biome = self.getBiome(surfacePos).value();
            WeatherQueryHelper.Precipitation precip = WeatherQueryHelper.precipitationAt(self, surfacePos);
            boolean active = WeatherQueryHelper.isActiveAt(self, surfacePos);

            if (biome.canSetIce(self, belowPos)) {
                self.setBlockState(belowPos, Blocks.ICE.getDefaultState());
            }

            if (active && precip == WeatherQueryHelper.Precipitation.SNOW) {
                int maxLayers = self.getGameRules().getInt(GameRules.SNOW_ACCUMULATION_HEIGHT);
                if (maxLayers > 0 && biome.canSetSnow(self, surfacePos)) {
                    BlockState state = self.getBlockState(surfacePos);
                    if (state.isOf(Blocks.SNOW)) {
                        int layers = state.get(SnowBlock.LAYERS);
                        if (layers < Math.min(maxLayers, 8)) {
                            BlockState newState = state.with(SnowBlock.LAYERS, layers + 1);
                            Block.pushEntitiesUpBeforeBlockChange(state, newState, self, surfacePos);
                            self.setBlockState(surfacePos, newState);
                        }
                    } else {
                        self.setBlockState(surfacePos, Blocks.SNOW.getDefaultState());
                    }
                }
            }

            if (active && precip != WeatherQueryHelper.Precipitation.NONE) {
                Biome.Precipitation vanillaPrecip = precip == WeatherQueryHelper.Precipitation.SNOW
                        ? Biome.Precipitation.SNOW
                        : Biome.Precipitation.RAIN;
                BlockState belowState = self.getBlockState(belowPos);
                belowState.getBlock().precipitationTick(belowState, self, belowPos, vanillaPrecip);
            }
        }
       profiler.pop();
    }


    @Inject(
            method = "tickWeather", at = @At("HEAD"), cancellable = true
    )
    private void welkin$stopVanillaWeather(CallbackInfo ci){
        ServerWorld world = (ServerWorld) (Object) this;
        WeatherManager manager = WeatherManager.getServerState(world);
        manager.tick(world);
        ci.cancel();
    }

    @WrapOperation(
            method = "tickChunk",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I")
    )
    private int forceFailRandomChecks(Random instance, int bound, Operation<Integer> original) {
        if (bound == 100000 || bound == 16) {
            return -1; // Returning -1 guarantees the '== 0' checks fail completely
        }
        return original.call(instance, bound); // Allow random tick speed checks to work perfectly
    }
}
