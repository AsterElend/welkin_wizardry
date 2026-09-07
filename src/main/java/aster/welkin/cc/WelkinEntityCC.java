package aster.welkin.cc;

import aster.welkin.Welkin;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class WelkinEntityCC implements EntityComponentInitializer {

    public static final ComponentKey<ForgottenAdvancementComponent> FORGOTTEN = ComponentRegistry.getOrCreate(
            new Identifier("welkin", "forgotten"), ForgottenAdvancementComponent.class);

    public static final ComponentKey<FrozenVelocityComponent> FROZEN_MOMENTUM =
            ComponentRegistry.getOrCreate(Welkin.id("frozen_velocity"), FrozenVelocityComponent.class);

    public static final ComponentKey<LastDeathSourceComponent> LAST_DEATH_SOURCE =
            ComponentRegistry.getOrCreate(Welkin.id("last_death_source"), LastDeathSourceComponent.class);
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(FORGOTTEN, ForgottenAdvancementComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(LAST_DEATH_SOURCE, LastDeathSourceComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(FROZEN_MOMENTUM, FrozenVelocityComponent::new, RespawnCopyStrategy.NEVER_COPY);
    }
}
