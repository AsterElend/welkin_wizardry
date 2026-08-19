package aster.welkin.block.entity;

import aster.welkin.api.AetherHolder;
import aster.welkin.api.Linkable;
import aster.welkin.recipes.AlchemyRecipe;
import aster.welkin.recipes.AlchemyRecipeManager;
import aster.welkin.registry.WelkinBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class AlchemyBlockEntity extends Linkable {
    public AlchemyBlockEntity(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.ALCHEMY_ENTITY, pos, state);
    }



    private List<NodeBlockEntity> getAdjacentNodes(World world, BlockPos pos) {
        List<NodeBlockEntity> list = new ArrayList<>();
        for (Direction direction: Direction.values()){
            BlockPos operatingPos = pos.offset(direction);
            BlockEntity entity = world.getBlockEntity(operatingPos);
            if (entity instanceof NodeBlockEntity node){
                list.add(node);
            }
        }
        return list;
    }
    public void alchemicalInvocation(World world, BlockPos pos) {
        if (world.isClient) return;
        List<NodeBlockEntity> nodes = getAdjacentNodes(world, pos);
        AetherHolder thunderhead;
        if (!(world.getBlockEntity(linkPos) instanceof ThunderheadBlockEntity) && !(world.getBlockEntity(linkPos) instanceof DebugThunderhead)) {
            return;
        }
        thunderhead = (AetherHolder) world.getBlockEntity(linkPos);


        for (NodeBlockEntity node : nodes) {
            ItemStack stack = node.getStack();
            if (stack.isEmpty()) continue;

            Item item = stack.getItem();

            // 1. Check if we have the energy required for any transmutation first
            if (!thunderhead.hasSufficientAether(777)) {
                continue;
            }

            AlchemyRecipe recipeToExecute = null;

            // 2. PRIORITY 1: Check for a catalyst (loop-jumping) recipe
            if (AlchemyRecipeManager.hasACatalystRecipe(item)) {
                AlchemyRecipe catRecipe = AlchemyRecipeManager.getRecipe(item, true);
                // Verify if the physical catalyst block matches what is below the node
                if (catRecipe != null && world.getBlockState(node.getPos().down()).getBlock() == catRecipe.getCatalyst()) {
                    recipeToExecute = catRecipe;

                }
            }

            // 3. PRIORITY 2: Fall back to the standard cyclic recipe if no catalyst match occurred
            if (recipeToExecute == null && AlchemyRecipeManager.hasRecipeOfItem(item)) {
                recipeToExecute = AlchemyRecipeManager.getRecipe(item, false);

            }

            // 4. EXECUTION: If a valid recipe path was found, perform the invocation
            if (recipeToExecute != null) {
                node.transmuteTo(recipeToExecute.getOutput());
                thunderhead.acceptAether(-777);
                spawnDustBurst(node.getPos());
            }
        }
    }



    private void spawnDustBurst(BlockPos pos) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }

        Vector3f paleBlue = new Vector3f(0.5f, 0.8f, 1.0f);
        float size = 1.0f;
        DustParticleEffect dust = new DustParticleEffect(paleBlue, size);
        serverWorld.spawnParticles(
                dust,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                30,           // count
                0.3, 0.4, 0.3, // spread on x/y/z
                1.0            // speed multiplier
        );
    }

    @Override
    public void readNbt(NbtCompound nbt){
        readLink(nbt);


    }

    @Override
    public void writeNbt(NbtCompound nbt){
        writeLink(nbt);

    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }


}
