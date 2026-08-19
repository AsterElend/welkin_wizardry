package aster.welkin.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;

public class SunstoneBlock extends Block {
    public static IntProperty LUMINANCE = IntProperty.of("luminance", 0, 15);
    public static final ToIntFunction<BlockState> STATE_TO_LUMINANCE = (state) -> (Integer)state.get(LUMINANCE);
    public SunstoneBlock(Settings settings){
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(LUMINANCE, 15));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder){
        builder.add(LUMINANCE);
    }

    public static int getSkyLight(World world){
        return 15 - world.getAmbientDarkness();
    }




    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack){
    world.scheduleBlockTick(pos, this, 20);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.isClient) return;
        int light = getSkyLight(world);
        if (state.get(LUMINANCE) != light){
            world.setBlockState(pos, state.with(LUMINANCE, light), Block.NOTIFY_LISTENERS);
        }
        world.scheduleBlockTick(pos, this, 20);
    }

}
