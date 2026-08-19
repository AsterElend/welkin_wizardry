package aster.welkin.registry;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class WarpCooldownEffect extends StatusEffect {
    protected WarpCooldownEffect() {
        super(StatusEffectCategory.NEUTRAL, 0x2323FF);
    }
    @Override
    public boolean canApplyUpdateEffect(int duration, int ampli){
        return true;
    }
}
