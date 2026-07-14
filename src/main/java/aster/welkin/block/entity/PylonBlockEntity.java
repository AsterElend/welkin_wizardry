package aster.welkin.block.entity;

import aster.welkin.api.PedestalLikeBlockEntity;
import aster.welkin.registry.WelkinBlockEntities;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class PylonBlockEntity extends PedestalLikeBlockEntity {


    public PylonBlockEntity(BlockPos pos, BlockState state){
        super(WelkinBlockEntities.PYLON, pos, state);
    }



}