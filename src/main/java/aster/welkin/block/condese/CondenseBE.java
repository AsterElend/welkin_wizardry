package aster.welkin.block.condese;

import aster.welkin.registry.ModBlockEntities;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class CondenseBE extends BlockEntity {
    public CondenseBE(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONDENSER, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, CondenseBE be) {

        Direction facing = state.get(CondenseBlock.FACING);
        BlockPos targetPos = pos.offset(facing);

        Storage<FluidVariant> target = FluidStorage.SIDED.find(world, targetPos, facing.getOpposite());
        if (target == null) return;

        FluidVariant water = FluidVariant.of(Fluids.WATER);

        try (Transaction tx = Transaction.openOuter()) {
            target.insert(water, 10, tx); // 10mB/tick
            tx.commit();
        }
    }

}
