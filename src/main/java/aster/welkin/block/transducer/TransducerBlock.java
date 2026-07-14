package aster.welkin.block.transducer;

import aster.welkin.api.IHasLensInfo;
import aster.welkin.api.Yoinkable;
import aster.welkin.registry.WelkinItems;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public abstract class TransducerBlock extends BlockWithEntity implements IHasLensInfo, Yoinkable {


    public static final DirectionProperty FACING = Properties.FACING;

    public static final VoxelShape UP_SHAPE    = Block.createCuboidShape(5, 0,  5,  11, 13, 11);
    public static final VoxelShape DOWN_SHAPE  = Block.createCuboidShape(5, 3,  5,  11, 16, 11);
    public static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(5, 5,  0,  11, 11, 13);
    public static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(5, 5,  3,  11, 11, 16);
    public static final VoxelShape EAST_SHAPE  = Block.createCuboidShape(3, 5,  5,  16, 11, 11);
    public static final VoxelShape WEST_SHAPE  = Block.createCuboidShape(0, 5,  5,  13, 11, 11);

    public static final Map<Direction, VoxelShape> SHAPES = new HashMap<>() {{
        put(Direction.UP,    UP_SHAPE);
        put(Direction.DOWN,  DOWN_SHAPE);
        put(Direction.NORTH, NORTH_SHAPE);
        put(Direction.SOUTH, SOUTH_SHAPE);
        put(Direction.EAST,  EAST_SHAPE);
        put(Direction.WEST,  WEST_SHAPE);
    }};

    public TransducerBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.UP));
    }



    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx){
        Direction direction = ctx.getSide();
        BlockState state = ctx.getWorld().getBlockState(ctx.getBlockPos().offset(direction.getOpposite()));
        return state.isOf(this) && state.get(FACING) == direction ? this.getDefaultState().with(FACING, direction.getOpposite())
                : this.getDefaultState().with(FACING, direction);
    }

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
        return BlockRenderType.MODEL;
}





}
