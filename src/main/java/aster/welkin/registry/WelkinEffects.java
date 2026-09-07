package aster.welkin.registry;

import aster.welkin.Welkin;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class WelkinEffects {
    public static void invoke() {}
    public static final StatusEffect WARP_COOLDOWN = make("warp_cooldown", new WarpCooldownEffect());
    public static final StatusEffect PECKISHNESS = make("peckishness", new PeckishnessEffect());

    public static <T extends StatusEffect> StatusEffect make(String name, T effect){
        return Registry.register(Registries.STATUS_EFFECT, Welkin.id(name), effect);
    }
}
