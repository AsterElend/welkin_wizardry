package aster.welkin.registry;


import aster.welkin.Welkin;
import aster.welkin.block.FalseMilkFluid;
import aster.welkin.block.LetheanFluid;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.concurrent.Flow;

public class WelkinFluids {
    public static final FlowableFluid LETHEAN_WATER_STATIC = Registry.register(Registries.FLUID, Welkin.id("lethean_water_static"), new LetheanFluid.Still());
    public static final FlowableFluid LETHEAN_WATER_FLOWING = Registry.register(Registries.FLUID, Welkin.id("lethean_water_flowing"), new LetheanFluid.Flowing());
    public static final FlowableFluid FALSE_MILK_STATIC = make("false_milk_static", new FalseMilkFluid.Still());
    public static final FlowableFluid FALSE_MILK_FLOWING = make("false_milk_flowing", new FalseMilkFluid.Flowing());
    private static FlowableFluid make(String name, FlowableFluid fluid){
        return Registry.register(Registries.FLUID, Welkin.id(name), fluid);
    }
    public static void invoke(){

    }
}
