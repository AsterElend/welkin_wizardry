package aster.welkin.mixin;

import aster.welkin.registry.WelkinItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
        if (this.getStack().isOf(Items.GLASS_BOTTLE)){
            Entity allegedEntity = (Entity) (Object) this;
            World world = allegedEntity.getWorld();
            if (world.getRegistryKey() == World.OVERWORLD && world.isOutOfHeightLimit(allegedEntity.getBlockPos().getY())){
                int count = this.getStack().getCount();
                this.setStack(new ItemStack(WelkinItems.CLOUD_BOTTLE, count));
            }
        }
    }
}
