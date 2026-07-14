package aster.welkin.block.entity;

import aster.welkin.api.PedestalLikeBlockEntity;
import aster.welkin.registry.WelkinBlockEntities;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class NodeBlockEntity extends PedestalLikeBlockEntity {

    public NodeBlockEntity(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.NODE, pos, state);
    }

    @Override
    public int getMaxCount() {
        return 1;
    }

}