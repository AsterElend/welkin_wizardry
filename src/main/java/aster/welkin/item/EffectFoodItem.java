package aster.welkin.item;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EffectFoodItem extends Item {
    private final int use_time;
    @Nullable
    private final Item returnedItem;
    private boolean isDrink;
    //make sure to attach a foodComponent

    public EffectFoodItem(Settings settings, int useTime, boolean isDrink, @Nullable Item returnedItem) {
        super(settings);
        use_time = useTime;
        this.returnedItem = returnedItem;
        this.isDrink = isDrink;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        super.finishUsing(stack, world, user);
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }


        if (returnedItem != null){
            if (stack.isEmpty()) {
                return new ItemStack(returnedItem);
            } else {
                if (user instanceof PlayerEntity playerEntity && !playerEntity.getAbilities().creativeMode) {
                    ItemStack itemStack = new ItemStack(returnedItem);
                    if (!playerEntity.getInventory().insertStack(itemStack)) {
                        playerEntity.dropItem(itemStack, false);
                    }
                }
            }
        }

        return stack;
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public int getMaxUseTime(ItemStack stack){
        return use_time;
    }

    @Override
    public UseAction getUseAction(ItemStack stack){
        if (isDrink) return UseAction.DRINK;
        else return UseAction.EAT;
    }

    @Override
    public SoundEvent getEatSound(){
        if (isDrink)  return SoundEvents.ENTITY_GENERIC_DRINK;
        return SoundEvents.ENTITY_GENERIC_EAT;
    }

    @Override
    public SoundEvent getDrinkSound(){
        if (isDrink)  return SoundEvents.ENTITY_GENERIC_DRINK;
        return SoundEvents.ENTITY_GENERIC_EAT;
    }


}
