package aster.welkin.mixin;

import aster.welkin.item.WardstoneItem;
import aster.welkin.registry.WelkinItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public class FarmlandBlockMixin {
    @Inject(
            method = "onLandedUpon",
            at = @At("HEAD"),
            cancellable = true
    )
    private void preventTrample(
            World world,
            BlockState state,
            BlockPos pos,
            Entity entity,
            float distance,
            CallbackInfo ci
    ){
        if (entity instanceof PlayerEntity player){
            if (!WardstoneItem.hasParticularWardstoneActive(player, (WardstoneItem) WelkinItems.SKIMSTEP_WARDSTONE)) return;
            ci.cancel();
        }
    }
}
