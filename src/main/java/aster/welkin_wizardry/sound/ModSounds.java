package aster.welkin_wizardry.sound;

import aster.welkin_wizardry.WelkinWizardry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModSounds {
    private static SoundEvent registerSoundEvent(String name) {
        Identifier identifier = new Identifier(WelkinWizardry.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }




    public static final SoundEvent STORMCYCLE = registerSoundEvent("stormcycle");
    public static final SoundEvent WINDTUNNEL = registerSoundEvent("windtunnel");

            public static void registerSounds() {
                WelkinWizardry.LOGGER.info("registering sounds for ww");
            }
}
