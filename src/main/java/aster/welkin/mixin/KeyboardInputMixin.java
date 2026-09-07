package aster.welkin.mixin;

import aster.welkin.cc.FrozenVelocityComponent;
import aster.welkin.cc.WelkinEntityCC;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void welkin$nullifyInputsWhenLocked(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;

        if (player != null) {
            FrozenVelocityComponent comp = WelkinEntityCC.FROZEN_MOMENTUM.get(player);
            if (comp.isLocked()) {
                // Erase all movement multiplier variables
                KeyboardInput input = (KeyboardInput) (Object) this;
                input.movementForward = 0.0F;
                input.movementSideways = 0.0F;

                // Clear the raw keystroke booleans
                input.pressingForward = false;
                input.pressingBack = false;
                input.pressingLeft = false;
                input.pressingRight = false;

                // Block jumping and crouching actions
                input.jumping = false;
                input.sneaking = false;
            }
        }
    }
}