package aster.welkin.mixin;

import aster.welkin.item.WardstoneItem;
import aster.welkin.registry.WelkinItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(
            method = "onSteppedOn",
            at = @At("HEAD"),
            cancellable = true
    )
    private void preventStep(
            World world,
            BlockPos pos,
            BlockState state,
            Entity entity,
            CallbackInfo ci
    ) {
        if (entity instanceof PlayerEntity player) {

            if (WardstoneItem.hasParticularWardstoneActive(player, (WardstoneItem) WelkinItems.SKIMSTEP_WARDSTONE)) {
                ci.cancel();
            }

        }
    }
}
