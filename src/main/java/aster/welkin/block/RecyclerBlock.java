package aster.welkin.block;

import aster.welkin.block.entity.NodeBlockEntity;
import aster.welkin.recipes.RecyclerRecipe;
import aster.welkin.recipes.RecyclerRecipeManager;
import aster.welkin.registry.ModRecipes;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;

import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecyclerBlock extends Block {

    public static final BooleanProperty POWERED = Properties.POWERED;
    public RecyclerBlock(FabricBlockSettings fabricBlockSettings) {
        super(fabricBlockSettings);
        setDefaultState(getStateManager().getDefaultState().with(POWERED, false));
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient()) {
            boolean powered = world.isReceivingRedstonePower(pos);
            boolean wasPowered = state.get(POWERED);

            if (powered && !wasPowered) {
                alchemicalInvocation(world, pos);
            }

            // Keep the state in sync
            if (powered != wasPowered) {
                world.setBlockState(pos, state.with(POWERED, powered));
            }
        }
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }


    private void alchemicalInvocation(World world, BlockPos pos){
        List<NodeBlockEntity> nodes = findNearbyNodesWithItems(world, pos);
        List<ItemStack> stacks = new ArrayList<>();
        for (NodeBlockEntity node: nodes){
            stacks.add(node.getStack());
        }
      Optional<RecyclerRecipe> match = findRecipe(stacks, world);


        if (match.isEmpty()) return;
        RecyclerRecipe recipe = match.get();
        ItemStack result = recipe.getOutput(world.getRegistryManager()).copy();

        for (NodeBlockEntity node: nodes){
            spawnDustBurst(world, pos);
            node.removeItem();
        }

        ItemEntity output = new ItemEntity(
                world,
                pos.getX() + 0.5,
                pos.getY() + 1.0,
                pos.getZ() + 0.5,
                result
        );

        output.setVelocity(0, 0.1, 0);
        world.spawnEntity(output);



    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    private List<NodeBlockEntity> findNearbyNodesWithItems(World world, BlockPos pos) {
        List<NodeBlockEntity> bobbins = new ArrayList<>();
        int range = 6;

        BlockPos start = pos.add(-range, -range, -range);
        BlockPos end = pos.add(range, range, range);

        for (BlockPos p : BlockPos.iterate(start, end)) {
            BlockEntity be = world.getBlockEntity(p);
            if (be instanceof NodeBlockEntity bobbin) {
                if (!bobbin.isEmpty()){
                    bobbins.add(bobbin);
                }

            }
        }

        return bobbins;
    }

    private void spawnDustBurst(World world, BlockPos pos) {
        // Pale blue — RGB values are in 0.0–1.0 range
        Vector3f paleBlue = new Vector3f(0.5f, 0.8f, 1.0f);
        float size = 1.0f;
        DustParticleEffect dust = new DustParticleEffect(paleBlue, size);

        Random random = world.getRandom();
        int count = 20;

        for (int i = 0; i < count; i++) {
            // Spread particles randomly around the center of the block
            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 1.2;
            double y = pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 1.2;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 1.2;

            // Small random velocity for a burst feel
            double vx = (random.nextDouble() - 0.5) * 0.2;
            double vy = (random.nextDouble() - 0.5) * 0.2;
            double vz = (random.nextDouble() - 0.5) * 0.2;

            world.addParticle(dust, true,  x, y, z, vx, vy, vz);
        }
    }

    public Optional<RecyclerRecipe> findRecipe(List<ItemStack> inputs, World world) {
        SimpleInventory inv = new SimpleInventory();
        for (ItemStack stack: inputs){
            inv.addStack(stack);
        }
        return RecyclerRecipeManager.RECYCLER_RECIPES.stream()
                .filter(recipe -> recipe.matches(inv, world))
                .findFirst();
    }
}
