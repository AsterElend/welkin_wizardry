package aster.welkin.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LightningAltar extends Block {

    public LightningAltar(Settings settings) {
        super(settings);
    }

    public VoxelShape SHAPE = Block.createCuboidShape(1, 0, 1, 15, 16, 15);


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
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
