package aster.welkin.datagen;

import aster.welkin.registry.LoomFluids;
import aster.welkin.registry.WelkinTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class WelkinFluidTagGen extends FabricTagProvider<Fluid> {
    public WelkinFluidTagGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.FLUID, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        getOrCreateTagBuilder(WelkinTags.LETHE).add(LoomFluids.LETHEAN_WATER_STATIC, LoomFluids.LETHEAN_WATER_FLOWING);
    }
}
