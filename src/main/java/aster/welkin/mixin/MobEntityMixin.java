package aster.welkin.mixin;

import aster.welkin.api.WeatherQueryHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public class MobEntityMixin {

    @Inject(method = "isAffectedByDaylight", at = @At("HEAD"), cancellable = true)
    private void welkin$blockBurnInWeather(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity)(Object)this;
        World world = self.getWorld();
        if (world.isClient || !(world instanceof ServerWorld serverWorld)) return;

        BlockPos pos = BlockPos.ofFloored(self.getX(), self.getEyeY(), self.getZ());
        if (WeatherQueryHelper.isActiveAt(serverWorld, pos)) {
            cir.setReturnValue(false);
        }
    }
}
