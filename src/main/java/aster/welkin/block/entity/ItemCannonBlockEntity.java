package aster.welkin.block.entity;

import aster.welkin.api.PedestalLikeBlockEntity;
import aster.welkin.registry.WelkinBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class ItemCannonBlockEntity extends PedestalLikeBlockEntity {
    public ItemCannonBlockEntity(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.ITEM_CANNON, pos, state);
    }
    public void fire(World world, BlockPos pos){
        if (!(world instanceof ServerWorld sworld)) return;
        if (this.isEmpty()) return;
        ItemEntity entity = new ItemEntity(world, pos.up().getX(), pos.up().getY(), pos.up().getZ(), getStack());

        entity.setVelocity(0, 16, 0);
       clearStack();
        world.spawnEntity(entity);
        Random random = world.getRandom();
        sworld.spawnParticles(ParticleTypes.WHITE_ASH, pos.getX(), pos.up().getY(), pos.getZ(), 3*entity.getStack().getCount(),
                random.nextFloat(), random.nextFloat(), random.nextFloat(), 1);

    }
}
