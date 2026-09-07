package aster.welkin.api;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public enum BiomeCategory {
    MYSTERY, HOT_WET, COLD_WET, TEMPERATE_WET, HOT_DRY, COLD_DRY, TEMPERATE_DRY;
    public static BiomeCategory fromBiomeEntry(RegistryEntry<Biome> biome){
        if (!biome.isIn(ConventionalBiomeTags.IN_OVERWORLD)) return MYSTERY;
       if (biome.isIn(ConventionalBiomeTags.CLIMATE_WET)){
           if (biome.isIn(ConventionalBiomeTags.CLIMATE_HOT)) return HOT_WET;
           if (biome.isIn(ConventionalBiomeTags.CLIMATE_COLD)) return COLD_WET;
           if (biome.isIn(ConventionalBiomeTags.CLIMATE_TEMPERATE)) return TEMPERATE_WET;

       } else if (biome.isIn(ConventionalBiomeTags.CLIMATE_DRY)){
           if (biome.isIn(ConventionalBiomeTags.CLIMATE_HOT)) return HOT_DRY;
           if (biome.isIn(ConventionalBiomeTags.CLIMATE_COLD)) return COLD_DRY;
           if (biome.isIn(ConventionalBiomeTags.CLIMATE_TEMPERATE)) return TEMPERATE_DRY;

       }
       return fromBiome(biome.value());
    }
    //fallback, try the other one first
    public static BiomeCategory fromBiome(Biome biome){
        float temp = biome.getTemperature();
        if (biome.hasPrecipitation()){
            if (temp >= 0.95f) return HOT_WET;
            if (temp <= 0.15f) return COLD_WET;
            return TEMPERATE_WET;

        } else {
            if (temp >= 0.95f) return HOT_DRY;
            if (temp <= 0.15f) return COLD_DRY;
            return TEMPERATE_DRY;
        }
    }
}
