package aster.welkin.mixin.interop;

import aster.welkin.api.WeatherQueryHelper;
import aster.welkin.api.WeatherState;
import aster.welkin.api.state.WeatherManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.block.TeruTeruBozuBlock;
import vazkii.botania.common.block.block_entity.TeruTeruBozuBlockEntity;
import vazkii.botania.common.helper.EntityHelper;

@Mixin(value = TeruTeruBozuBlock.class, remap = false)
public class TeruTeruBozuMixin {
    @Shadow
    private boolean isSunflower(ItemStack stack) {
        throw new AssertionError();
    }

    @Shadow
    private boolean isBlueOrchid(ItemStack stack){
        throw new AssertionError();
    }

  @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    public void welkin$OverrideBozuCall(BlockState state, World world, BlockPos pos, Entity e, CallbackInfo ci){
      if (!world.isClient && e instanceof ItemEntity item) {
          ServerWorld sworld = (ServerWorld) world ;
          ItemStack stack = item.getStack();
          if (this.isSunflower(stack) && this.welkin$removeRain(sworld, pos) || this.isBlueOrchid(stack) && this.welkin$startRain(sworld,pos)) {
              EntityHelper.shrinkItem(item);
          }
      }

      ci.cancel();
  }

    @Unique
    private boolean welkin$removeRain(ServerWorld world, BlockPos pos) {
        if (WeatherQueryHelper.isActiveAt(world, pos)) return false;
        WeatherManager manager = WeatherManager.getServerState(world);
        manager.forceStateWithRandomDuration(WeatherQueryHelper.categoryAt(world, pos), WeatherState.MODERATE,  world);
        TeruTeruBozuBlockEntity.resetRainTime(world);
        return true;
    }

    @Unique
    private boolean welkin$startRain(ServerWorld world, BlockPos pos) {
        if (!WeatherQueryHelper.isActiveAt(world, pos)) return false;
        if (world.getRandom().nextInt(10) == 0){
            WeatherManager manager = WeatherManager.getServerState(world);
            manager.forceStateWithRandomDuration(WeatherQueryHelper.categoryAt(world, pos), WeatherState.MODERATE, world);
            TeruTeruBozuBlockEntity.resetRainTime(world);
            return true;
        }
        return false;
    }

}
