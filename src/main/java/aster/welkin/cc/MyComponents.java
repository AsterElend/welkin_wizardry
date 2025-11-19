package aster.welkin.cc;

import aster.welkin.Welkin;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;

public class MyComponents implements EntityComponentInitializer {
    public static final ComponentKey<SkyForce> SKYFORCE = ComponentRegistry.getOrCreate(Welkin.id("skyforce"), SkyForce.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry){
        registry.registerForPlayers(SKYFORCE, SkyForce::new, RespawnCopyStrategy.NEVER_COPY);
    }
}
