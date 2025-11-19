package aster.welkin.mixin;

import aster.welkin.recipes.LightningHandler;
import net.minecraft.entity.LightningEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningEntity.class)
public class LightningMixin {


    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        LightningEntity self = (LightningEntity) (Object) this;
        World world = self.getWorld();

        if (!world.isClient) {


            BlockPos pos = self.getBlockPos();
            LightningHandler.handleSmite(world, pos);
        }
    }
}
