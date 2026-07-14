package aster.welkin.api;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class WelkinUtil {
    public static BlockHitResult getTargetedBlock(PlayerEntity player, Boolean includeFluids) {
        double base;
        if (player.isCreative()){
            base = 6.0;
        } else {
            base = 4.5;
        }
        double interactionRange = ReachEntityAttributes.getReachDistance(player, base);
        HitResult hitResult = player.raycast(interactionRange, 0.0F, includeFluids);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return (BlockHitResult) hitResult;
        }

        return null;
    }
}
