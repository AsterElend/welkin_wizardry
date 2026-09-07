package aster.welkin.registry;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;

public class WelkinFoodComponents {
    public static final FoodComponent BONE_APPLE_TEA = new FoodComponent.Builder().hunger(2).saturationModifier(.6f).statusEffect(new StatusEffectInstance(WelkinEffects.PECKISHNESS, 10, 1), 1).build();
}
