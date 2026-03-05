package aster.welkin.cc;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class WelkinEntityCC implements EntityComponentInitializer {
    public static final ComponentKey<SkyForceComponent> FORCE =
            ComponentRegistryV3.INSTANCE.getOrCreate(
                    new Identifier("welkin", "force"),
                    SkyForceComponent.class
            );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(
                FORCE,
                SkyForceComponent::new,
                RespawnCopyStrategy.ALWAYS_COPY
        );
    }
}
