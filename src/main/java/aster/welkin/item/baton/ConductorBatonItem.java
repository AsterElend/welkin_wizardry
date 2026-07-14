package aster.welkin.item.baton;

import aster.welkin.api.Linkable;
import aster.welkin.api.WelkinUtil;
import aster.welkin.api.Yoinkable;
import aster.welkin.registry.WelkinBlocks;
import aster.welkin.registry.WelkinTags;
import com.unascribed.lib39.recoil.api.DirectClickItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class ConductorBatonItem extends Item implements DirectClickItem {


    public ConductorBatonItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onDirectAttack(PlayerEntity player, Hand hand) {
        if (player.getWorld().isClient) return ActionResult.SUCCESS;
        ItemUsageContext ctx = new ItemUsageContext(player, hand, WelkinUtil.getTargetedBlock(player, false));
       if (player.isSneaking()){
          return doGrabFunction(ctx);
       } else {
           return doWrenchFunction(ctx);
       }
    }
    @Override
    public ActionResult onDirectUse(PlayerEntity player, Hand hand) {
        if (player.getWorld().isClient) return ActionResult.SUCCESS;
        ItemUsageContext ctx = new ItemUsageContext(player, hand, WelkinUtil.getTargetedBlock(player, false));
        if (player.isSneaking()){
            return doNodeFunction(ctx);
        } else {
            return doLinkFunction(ctx);
        }
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return false;
    }




    private static ActionResult doLinkFunction(ItemUsageContext ctx) {
        ItemStack stack = ctx.getStack();
        NbtCompound nbt = stack.getNbt();

        if (nbt != null && nbt.contains("storedpos")) {
            if (!(ctx.getWorld().getBlockEntity(BlockPos.fromLong(nbt.getLong("storedpos"))) instanceof Linkable zelda)) {
                if (!ctx.getWorld().isClient) {
                    stack.getOrCreateNbt().remove("storedpos");
                }
                return ActionResult.SUCCESS;
            }

            if (!ctx.getWorld().isClient) {
                boolean success = zelda.setLink(ctx.getBlockPos(), ctx.getSide());
                if (!success) {
                    ctx.getPlayer().sendMessage(Text.literal("Failed to link: out of range.")
                            .setStyle(Style.EMPTY.withColor(TextColor.parse("red"))), true); // true sets action bar overlay
                }
                stack.getOrCreateNbt().remove("storedpos");
            }
            return ActionResult.SUCCESS;
        }

        if (ctx.getWorld().getBlockEntity(ctx.getBlockPos()) instanceof Linkable) {
            if (!ctx.getWorld().isClient) {
                stack.getOrCreateNbt().putLong("storedpos", ctx.getBlockPos().asLong());
                ctx.getPlayer().playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 1, 1);
            }
            return ActionResult.SUCCESS;
        }

        return ActionResult.SUCCESS;
    }

    private static ActionResult doGrabFunction(ItemUsageContext ctx) {
        BlockState state = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (state.isIn(WelkinTags.YOINKABLE) || state.getBlock() instanceof Yoinkable) {
            if (!ctx.getWorld().isClient) {
                if (!ctx.getPlayer().isCreative()) {
                    Block.getDroppedStacks(state, (ServerWorld) ctx.getWorld(), ctx.getBlockPos(), ctx.getWorld().getBlockEntity(ctx.getBlockPos()),
                            ctx.getPlayer(), ctx.getStack()).forEach(itemStack -> {
                        if (!ctx.getPlayer().getInventory().insertStack(itemStack)) {
                            ctx.getPlayer().dropStack(itemStack);
                        }
                    });
                }
                ctx.getWorld().breakBlock(ctx.getBlockPos(), false);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    private static ActionResult doNodeFunction(ItemUsageContext ctx) {
        World world = ctx.getWorld();
        PlayerEntity player = ctx.getPlayer();
        BlockPos pos = ctx.getBlockPos();
        Direction side = ctx.getSide();

        BlockPos placePos = pos.offset(side);
        BlockState placeState = world.getBlockState(placePos);

        if (player != null && player.isSneaking() && placeState.isAir() && world.getBlockState(pos).isSolidBlock(world, pos)) {
            if (!world.isClient) {
                world.setBlockState(placePos, WelkinBlocks.NODE.getDefaultState());
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    private static ActionResult doWrenchFunction(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        Direction side = context.getSide();

        BlockState rotated;

        if (state.contains(Properties.FACING)) {
            // Full 6-way facing (e.g. your TransducerBlock, dispensers, droppers):
            // rotate around whichever axis was clicked, leaving that face fixed.
            Direction.Axis axis = side.getAxis();
            Direction newFacing = state.get(Properties.FACING).rotateClockwise(axis);
            rotated = state.with(Properties.FACING, newFacing);

        } else if (state.contains(Properties.HORIZONTAL_FACING)) {
            // 4-way horizontal facing (e.g. repeaters, comparators, furnaces):
            // there's no vertical axis to rotate around, so always do a
            // horizontal turn regardless of which face was clicked.
            rotated = state.with(Properties.HORIZONTAL_FACING,
                    state.get(Properties.HORIZONTAL_FACING).rotateYClockwise());

        } else {
            // Fallback for anything else with rotatable state (AXIS, ROTATION, stairs, etc.)
            // — defer to vanilla's generic horizontal rotation.
            rotated = state.rotate(BlockRotation.CLOCKWISE_90);
        }

        if (rotated == state) {
            return ActionResult.PASS;
        }

        if (!world.isClient) {
            world.setBlockState(pos, rotated, Block.NOTIFY_ALL);
            world.emitGameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, pos);
        }

        return ActionResult.SUCCESS;
    }


}
