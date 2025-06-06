package aster.welkin.sound;

import aster.welkin.Welkin;
import net.minecraft.sound.SoundEvent;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModSounds {
    private static SoundEvent registerSoundEvent(String name) {
        Identifier identifier = new Identifier(Welkin.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }




    public static final SoundEvent STORMCYCLE = registerSoundEvent("stormcycle");
    public static final SoundEvent WINDTUNNEL = registerSoundEvent("windtunnel");

            public static void registerSounds() {
                Welkin.LOGGER.info("registering sounds for ww");
            }
}
