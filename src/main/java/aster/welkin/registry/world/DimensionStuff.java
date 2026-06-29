package aster.welkin.registry.world;


import aster.welkin.Welkin;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class DimensionStuff {
    public static final Identifier DIMENSION_ID = Welkin.id("high_wilderness");
    public static final RegistryKey<World> DIMENSION_KEY = RegistryKey.of(RegistryKeys.WORLD, DIMENSION_ID);

    public static void init(){}
}
