package aster.welkin.block.sigil;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public abstract class SigilBlockEntity extends BlockEntity {
    public SigilBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public Identifier TEXTURE_ID;
    public Identifier SIGIL_ID;
}
