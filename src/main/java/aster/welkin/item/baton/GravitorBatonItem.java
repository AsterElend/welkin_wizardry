package aster.welkin.item.baton;

import aster.welkin.cc.FrozenVelocityComponent;
import aster.welkin.cc.WelkinEntityCC;
import aster.welkin.packet.WelkinPackets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GravitorBatonItem extends Item {
    public GravitorBatonItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        FrozenVelocityComponent comp = WelkinEntityCC.FROZEN_MOMENTUM.get(user);

        if (world.isClient) {
            // CLIENT SIDE: If not locked, capture the real walking velocity and send it to the server
            if (!comp.isLocked()) {
                Vec3d clientVelocity = user.getVelocity();
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeDouble(clientVelocity.x);
                buf.writeDouble(clientVelocity.y);
                buf.writeDouble(clientVelocity.z);
                ClientPlayNetworking.send(WelkinPackets.FREEZE_MOMENTUM_PACKET, buf);
            } else {
                // If unlocking, we can just send an empty packet to toggle it off
                ClientPlayNetworking.send(WelkinPackets.FREEZE_MOMENTUM_PACKET, PacketByteBufs.empty());
            }
            return TypedActionResult.success(user.getStackInHand(hand));
        }

        // SERVER SIDE: Just handle the sounds and logic that aren't packet-dependent
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
