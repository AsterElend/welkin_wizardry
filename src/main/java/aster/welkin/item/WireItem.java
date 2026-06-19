package aster.welkin.item;

import aster.welkin.block.transducer.TransducerEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WireItem extends Item {

    public WireItem(Settings settings) {
        super(settings);
    }
    public boolean hasNoFirstPos(NbtCompound nbt){
        return !nbt.contains("firstPos");
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        World world = context.getWorld();
        if (world.getBlockEntity(context.getBlockPos()) instanceof TransducerEntity entity){
            ItemStack stack = context.getStack();
            if (hasNoFirstPos(stack.getOrCreateNbt())){

                    savePosToNbt(stack, entity.getPos());


            } else {
                BlockPos firstPos = readPosFromNbt(stack);
                BlockPos secondPos = entity.getPos();
                double distance = Math.sqrt(firstPos.getSquaredDistance(secondPos));
                if (!(world.getBlockEntity(firstPos) instanceof TransducerEntity anotherEntity) || distance > 16){
                    stack.setNbt(new NbtCompound());
                    return ActionResult.FAIL;
                }



                    anotherEntity.linkNode(secondPos);
                    entity.linkNode(firstPos);
                    if (!context.getPlayer().isCreative()){
                        stack.decrement(1);
                    }
                    if (stack.getCount() >= 1){
                        stack.setNbt(new NbtCompound());
                    }


            }
            return ActionResult.SUCCESS;


        }
        return ActionResult.PASS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (hasNoFirstPos(stack.getOrCreateNbt())) return;
        tooltip.add(Text.literal(readPosFromNbt(stack).toString()));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand){
        user.setCurrentHand(hand);
        ItemStack stack = user.getStackInHand(hand);
        if (user.isSneaking()){
            stack.setNbt(new NbtCompound());
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.pass(stack);
    }



    private void savePosToNbt(ItemStack stack, BlockPos pos){
        var stackNbt = stack.getOrCreateNbt();
        stackNbt.putLong("firstPos", pos.asLong());
    }

    private BlockPos readPosFromNbt(ItemStack stack){
        NbtCompound nbt = stack.getNbt();
        return BlockPos.fromLong(nbt.getLong("firstPos"));

    }
}
