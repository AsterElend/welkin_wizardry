package aster.welkin.registry;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class PeckishnessEffect extends StatusEffect {
    public PeckishnessEffect() {
        // Category can be beneficial, harmful, or neutral. Color is in decimal.
        super(StatusEffectCategory.BENEFICIAL, 0xD4A373);
    }


}
