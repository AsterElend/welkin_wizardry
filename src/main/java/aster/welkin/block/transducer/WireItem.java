package aster.welkin.block.transducer;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WireItem extends Item {
    public WireItem(Settings settings) {
        super(settings);
    }


    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        World world = ctx.getWorld();
        if (world.isClient) return ActionResult.SUCCESS;


        PlayerEntity player = ctx.getPlayer();
        BlockPos pos = ctx.getBlockPos();
        BlockEntity be = world.getBlockEntity(pos);


        if (!(be instanceof TransducerBlockEntity trans))
            return ActionResult.PASS;


        ItemStack stack = ctx.getStack();
        if (!stack.getOrCreateNbt().contains("first")) {
// First selection
            stack.getOrCreateNbt().putLong("first", pos.asLong());
            player.sendMessage(Text.literal("Stored first transducer."), true);
        } else {
// Second selection
            BlockPos first = BlockPos.fromLong(stack.getOrCreateNbt().getLong("first"));
            BlockEntity be2 = world.getBlockEntity(first);
            if (be2 instanceof TransducerBlockEntity trans2) {
                trans.setLinkedPos(first);
                trans2.setLinkedPos(pos);


                WireConnectionComponent.addConnection(world, first, pos);


                player.sendMessage(Text.literal("Transducers connected!"), true);
                stack.getOrCreateNbt().remove("first");
            }
        }
        return ActionResult.SUCCESS;
    }
}
