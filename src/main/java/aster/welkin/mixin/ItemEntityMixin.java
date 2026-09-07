package aster.welkin.mixin;

import aster.welkin.recipes.CaelumRecipe;
import aster.welkin.recipes.ItemEntityTransmutationRecipe;
import aster.welkin.recipes.VoidRecipe;
import aster.welkin.registry.WelkinItems;
import aster.welkin.registry.WelkinRecipes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.apache.logging.log4j.core.jmx.Server;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow
    public abstract ItemStack getStack();

    @Shadow
    public abstract void setStack(ItemStack stack);

    @Inject(
            method = "tick",
    at = @At("TAIL")
    )
    private void tick(CallbackInfo ci){

            Entity allegedEntity = (Entity) (Object) this;
            if (this == null) return;
            ItemEntity entity = (ItemEntity) allegedEntity;
            World world = allegedEntity.getWorld();
            if (world instanceof ServerWorld server){
                int y = allegedEntity.getBlockPos().getY();

                if (world.isOutOfHeightLimit(allegedEntity.getBlockPos().getY())){
                    SimpleInventory inv = new SimpleInventory(this.getStack());

                    if ( y > world.getTopY()){
                        Optional<CaelumRecipe> match = world.getRecipeManager().getFirstMatch(
                                WelkinRecipes.CAELUM_TYPE,
                                inv,
                                world
                        );
                        if (match.isPresent()){

                            return;
                        }
                    }

                    if (y < world.getBottomY()){
                        Optional<VoidRecipe> match = world.getRecipeManager().getFirstMatch(
                                WelkinRecipes.VOID_TYPE,
                                inv,
                                world
                        );
                        if (match.isPresent()){
                            ItemEntity letsGoFlying = match.get().transmuteAStack(entity, server, ParticleTypes.SMOKE);
                            letsGoFlying.setNoGravity(true);
                            letsGoFlying.setVelocity(new Vec3d(0, 1, 0));
                        }

                    }
                }
            }
        }

}
