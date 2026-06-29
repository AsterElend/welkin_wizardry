package aster.welkin.registry.world.trees;


import aster.welkin.Welkin;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public class LoomFoliagePlacers {
    public static final FoliagePlacerType<FractalFoliagePlacer> FRACTAL_LEAF_PLACER =
            Registry.register(
                    Registries.FOLIAGE_PLACER_TYPE,
                    Welkin.id("fractal_leaves"),
                            new FoliagePlacerType<>(FractalFoliagePlacer.CODEC)
            );
public static void init(){}
}
