package aster.welkin.block.entity;

import aster.welkin.api.AetherHolder;
import aster.welkin.api.WelkinUtil;
import aster.welkin.registry.WelkinBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ThunderheadBlockEntity extends AetherHolder {

    public ThunderheadBlockEntity(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.THUNDERHEAD, pos, state);
        WelkinUtil.yellAtEverything(this);
    }



    @Override
    public NbtCompound toInitialChunkDataNbt(){
        return createNbt();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }


}
