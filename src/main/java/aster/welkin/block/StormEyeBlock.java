package aster.welkin.block;

import aster.welkin.recipes.StormEyeRecipe;
import aster.welkin.registry.WelkinRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.patchouli.api.IMultiblock;

import java.util.List;

public class StormEyeBlock extends Block {
    public StormEyeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;

        List<StormEyeRecipe> list = world.getRecipeManager().listAllOfType(WelkinRecipes.STORM_EYE_TYPE);

        for (StormEyeRecipe recipe : list) {
            BlockRotation rot = recipe.testMulti(pos, world);

            if (rot == null) continue;

            craft((ServerWorld) world, recipe, rot, pos);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private void craft(ServerWorld world, StormEyeRecipe recipe, BlockRotation rot, BlockPos pos) {
        IMultiblock multiblock = recipe.getMultiblock();

        // 1. Clear every block that made up the structure
        var sim = multiblock.simulate(world, pos, rot, false);
        for (IMultiblock.SimulateResult r : sim.getSecond()) {
            world.setBlockState(r.getWorldPosition(), Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
        }


        {
            BlockPos outRel = recipe.getOutputOffset() != null ? recipe.getOutputOffset() : recipe.getOffset();
            BlockPos outputPos = pos.add(outRel);

            if (recipe.getOutputState() != null) {
                world.setBlockState(outputPos, recipe.getOutputState().rotate(rot), Block.NOTIFY_ALL);
            } else if (recipe.getOutput() != null && !recipe.getOutput().isEmpty()) {
                Block.dropStack(world, outputPos, recipe.getOutput().copy());
            }


            world.playSound(null, pos, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.spawnParticles(ParticleTypes.CLOUD,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    20, 0.5, 0.5, 0.5, 0.05);
        }


    }
}