package aster.welkin.block.sigil;

import aster.welkin.api.Yoinkable;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SigilBlock extends BlockWithEntity implements Yoinkable {

    public static final DirectionProperty FACING = Properties.FACING;

    private static final VoxelShape UP_SHAPE    = Block.createCuboidShape(1.0, 15.0, 1.0, 15.0, 16.0, 15.0);
    private static final VoxelShape DOWN_SHAPE  = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 1.0, 15.0);
    private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(1.0, 1.0, 0.0, 15.0, 15.0, 1.0);
    private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(1.0, 1.0, 15.0, 15.0, 15.0, 16.0);
    private static final VoxelShape EAST_SHAPE  = Block.createCuboidShape(15.0, 1.0, 1.0, 16.0, 15.0, 15.0);
    private static final VoxelShape WEST_SHAPE  = Block.createCuboidShape(0.0, 1.0, 1.0, 1.0, 15.0, 15.0);

    public SigilBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.UP));
    }


    public static final Map<Direction, VoxelShape> SHAPES = new HashMap<>() {{
        put(Direction.UP,    UP_SHAPE);
        put(Direction.DOWN,  DOWN_SHAPE);
        put(Direction.NORTH, NORTH_SHAPE);
        put(Direction.SOUTH, SOUTH_SHAPE);
        put(Direction.EAST,  EAST_SHAPE);
        put(Direction.WEST,  WEST_SHAPE);
    }};

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);

    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx){
        return SHAPES.get(state.get(FACING));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state){
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
