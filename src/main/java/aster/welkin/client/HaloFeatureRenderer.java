package aster.welkin.client;


import aster.welkin.cc.WelkinEntityCC;
import aster.welkin.registry.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.util.ArrayList;
import java.util.List;

public class HaloFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    private final MinecraftClient client;

    public HaloFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context, MinecraftClient client) {
        super(context);
        this.client = client;
    }

    @Override
    public void render(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            AbstractClientPlayerEntity player,
            float limbSwing,
            float limbSwingAmount,
            float tickDelta,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {
    if (!player.getStackInHand(player.getActiveHand()).isOf(ModItems.HALO_BATON)) return;
    NbtCompound nbt = player.getStackInHand(player.getActiveHand()).getOrCreateNbt();
    if (nbt.isEmpty()) return;
        List<String> currentValues = new java.util.ArrayList<>(nbt.getKeys().stream().toList());
        List<ItemStack> stacks = new ArrayList<>();
        for (String string: currentValues){
            stacks.add(new ItemStack(Registries.ITEM.get(new Identifier(string))));
        }

        // Push once for the entire feature
        matrices.push();

        // Translate to the player's head
        ModelPart head = this.getContextModel().head;
        matrices.translate(
                head.pivotX / 16.0f,        // usually 0
                head.pivotY / 16.0f + 0.25, // 0.25 is a small offset to move above head center
                head.pivotZ / 16.0f         // usually 0
        );


        float time = (player.age + tickDelta) * 0.05f;
        int n = stacks.size();

        for (int i = 0; i < n; i++) {
            float angle = time + ((float) i / n) * 2f * (float) Math.PI;
            float radius = 0.3f;
            float x = (float)Math.cos(angle) * radius;
            float z = (float)Math.sin(angle) * radius;
            float y = -1f;

            // Push per item, pop after rendering it
            matrices.push();
            matrices.translate(x, y, z);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time * 20));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(headYaw));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(headPitch));
            matrices.scale(0.5f, 0.5f, 0.5f);

            client.getItemRenderer().renderItem(stacks.get(i), ModelTransformationMode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, player.getWorld(), i);

            matrices.pop(); // must pop here
        }

        matrices.pop(); // pop the initial push
    }

}
