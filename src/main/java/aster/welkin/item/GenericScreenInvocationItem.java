package aster.welkin.item;


import aster.welkin.api.SCREEN_INVOCATIONS;
import aster.welkin.client.ClientScreenOpener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GenericScreenInvocationItem extends Item {
    private final boolean debug;
    private final SCREEN_INVOCATIONS screen;
    public GenericScreenInvocationItem(Settings settings, boolean debug, SCREEN_INVOCATIONS screen) {
        super(settings);
        this.debug = debug;
        this.screen = screen;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient) {
            ClientScreenOpener.open(screen, debug);
        }
        return TypedActionResult.success(stack, world.isClient);
    }

}