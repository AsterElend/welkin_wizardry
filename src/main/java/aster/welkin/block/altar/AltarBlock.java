package aster.welkin.block.altar;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AltarBlock extends Block {

    public AltarBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit){
        if (!world.isClient) {

            ServerWorld serverWorld = (ServerWorld) world;
            LightningEntity zapper = EntityType.LIGHTNING_BOLT.create(world);


            if (zapper != null){
                zapper.refreshPositionAfterTeleport(
                        pos.getX()+0.5,
                        pos.getY()+1,
                        pos.getZ()+0.5
                );
                world.spawnEntity(zapper);
            }

        }
        return ActionResult.SUCCESS;
    }




}
