package aster.welkin.mixin;

import aster.welkin.recipes.LightningRecipe;
import aster.welkin.registry.ModRecipes;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(LightningEntity.class)
public abstract class LightningHandler {

    @Shadow
    protected abstract BlockPos getAffectedBlockPos();

    @Inject(method = "spawnFire", at = @At("HEAD"), cancellable = true)
    private void stopFireCreation(CallbackInfo ci) {
        ci.cancel();
    }





    @Inject(method = "tick", at = @At("HEAD"))
    private void doTheSmiting(CallbackInfo info){
        LightningEntity self = (LightningEntity) (Object) this;
        World world = self.getWorld();

        if (world.isClient || self.age != 1) return;


        ServerWorld server = (ServerWorld) world;
        final double RADIUS = 2;
        BlockPos pos = this.getAffectedBlockPos();
        Box aura = new Box( pos).expand(RADIUS);

        List<ItemEntity> items = server.getEntitiesByClass(ItemEntity.class, aura, e -> true);
        if (items.isEmpty()) return;

        for (ItemEntity item : items){
            ItemStack stack = item.getStack();

            SimpleInventory inv = new SimpleInventory(1);
            inv.setStack(0, stack);

            Optional<LightningRecipe> match =
                    server.getRecipeManager().getFirstMatch(
                            ModRecipes.LIGHTNING_TYPE, inv, server
                    );

            if (match.isEmpty()) continue;

            LightningRecipe recipe = match.get();
            ItemStack input = item.getStack();
            int count = input.getCount();
            ItemStack base = recipe.getBaseOutput();
            ItemStack result = new ItemStack(base.getItem(), base.getCount() * count);

            // Remove the original item
            item.discard();

            // Spawn the result
            ItemEntity output = new ItemEntity(
                    server,
                    item.getX(),
                    item.getY(),
                    item.getZ(),
                    result
            );
server.spawnParticles(ParticleTypes.SCRAPE, item.getX(), item.getY(), item.getZ(), 300, 1, 1, 1,1 );
            server.spawnEntity(output);
        }

    }

}