package aster.welkin.block;

import aster.welkin.api.state.WarpLinkState;
import aster.welkin.block.entity.WarpArrayBlockEntity;
import aster.welkin.registry.WelkinItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class WarpArrayBlock extends Block {


    public WarpArrayBlock(Settings settings) {

        super(settings);
    }


    public static class Controller extends BlockWithEntity {

       public Controller(Settings settings) {
           super(settings);
       }

        @Override
        public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
            return new WarpArrayBlockEntity(pos, state);
        }

       @Override
       public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
           if (world.isClient) return ActionResult.SUCCESS;


           if (world.getBlockEntity(pos) instanceof WarpArrayBlockEntity controller) {
               // Execute your logic on the central controller

               controller.acceptOnUse(pos, world, player, hand);
               return ActionResult.CONSUME;
           }

           return ActionResult.PASS;
       }


       @Override
       public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
           if (world.isClient) return;

           if (world.getBlockEntity(pos) instanceof WarpArrayBlockEntity controller) {

               controller.acceptSteppedOn(world, pos, entity);
           }
       }


       @Override
       public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
           super.onStateReplaced(state, world, pos, newState, moved);
           if (!(world.getBlockEntity(pos) instanceof WarpArrayBlockEntity entity)) return;
           WarpLinkState warpState = WarpLinkState.get((ServerWorld) world);

           switch (entity.getCore()) {

               case BLACK_HOLE -> {
                   ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(),
                           new ItemStack(WelkinItems.BLACK_HOLE_CORE));
                   warpState.removePair(entity.getLinkId());
               }
               case WHITE_HOLE -> {
                   ItemStack toDrop = new ItemStack(WelkinItems.WHITE_HOLE_CORE);
                   UUID link = entity.getLinkId();
                   if (link != null) {
                       toDrop.getOrCreateNbt().putUuid("linkId", link);
                       warpState.clearWhiteEnd(link);
                   }
                   ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), toDrop);
               }
               case NONE -> { /* nothing to drop */ }
           }
       }
   }


}