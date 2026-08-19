package aster.welkin.item.baton;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GravitorBatonItem extends Item {
    public GravitorBatonItem(Settings settings) {
        super(settings);
    }
    //todo make this work like RoA neo
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
       if (world.isClient) return TypedActionResult.pass(user.getStackInHand(hand));
       user.setNoGravity(!user.hasNoGravity());
       return TypedActionResult.success(user.getStackInHand(hand));
    }
}
