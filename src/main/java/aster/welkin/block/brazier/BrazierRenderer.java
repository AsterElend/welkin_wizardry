package aster.welkin.block.brazier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

@Environment(EnvType.CLIENT)
public class BrazierRenderer implements BlockEntityRenderer<BrazierBlockEntity> {

    private final MinecraftClient client;
    public BrazierRenderer(BlockEntityRendererFactory.Context ctx){
        this.client = MinecraftClient.getInstance();
    }

    @Override
    public void render(BrazierBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ClientWorld world = (ClientWorld) entity.getWorld();
        if (world == null) return;

        BlockPos pos = entity.getPos();
        BlockState state = world.getBlockState(pos);

        if (!state.contains(BrazierBlock.LIT) || !state.get(BrazierBlock.LIT)){
            return;
        }

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.3;
        double z = pos.getZ() + 0.5;

        Random rand = world.getRandom();

        if (rand.nextFloat() < 0.6f) {
            double ox = (rand.nextDouble() - 0.5) * 0.25;
            double oz = (rand.nextDouble() - 0.5) * 0.25;
            client.worldRenderer.addParticle(ParticleTypes.END_ROD, true,
                    x + ox, y + 0.05, z + oz,
                    0.0, 0.01 + rand.nextDouble() * 0.02, 0.0);
        }
        if (rand.nextFloat() < 0.9f) {
            double ox = (rand.nextDouble() - 0.5) * 0.2;
            double oz = (rand.nextDouble() - 0.5) * 0.2;
            client.worldRenderer.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, true,
                    x + ox, y, z + oz,
                    0.0, 0.02 + rand.nextDouble() * 0.02, 0.0);
        }


    }
}
