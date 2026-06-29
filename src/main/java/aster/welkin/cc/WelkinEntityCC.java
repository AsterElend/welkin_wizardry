package aster.welkin.cc;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class WelkinEntityCC implements EntityComponentInitializer {
    public static final ComponentKey<AlchemyVisionsComponent> HALO = ComponentRegistry.getOrCreate(
            new Identifier("welkin", "discoveries"), AlchemyVisionsComponent.class);
    public static final ComponentKey<ForgottenAdvancementComponent> FORGOTTEN = ComponentRegistry.getOrCreate(
            new Identifier("songweaver", "forgotten"), ForgottenAdvancementComponent.class);
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(FORGOTTEN, ForgottenAdvancementComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(HALO, AlchemyVisionsComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
