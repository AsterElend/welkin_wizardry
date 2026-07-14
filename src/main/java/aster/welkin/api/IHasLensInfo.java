package aster.welkin.api;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.StringVisitable;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;


public interface IHasLensInfo {
    //inherit this from both the block and the block entity, but only fill out the method in the blockEntity
     default void applyLensOverlay(List<Pair<ItemStack, StringVisitable>> lines,
                              BlockState state, BlockPos pos,
                              PlayerEntity observer, World world, Direction hitFace){

    };
}
