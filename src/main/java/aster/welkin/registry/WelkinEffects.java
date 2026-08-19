package aster.welkin.registry;

import aster.welkin.Welkin;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class WelkinEffects {
    public static void invoke() {}
    public static final StatusEffect WARP_COOLDOWN = Registry.register(Registries.STATUS_EFFECT,
            Welkin.id("warp_cooldown"), new WarpCooldownEffect());
}
