package aster.welkin.block.sigil;

import aster.welkin.Welkin;
import aster.welkin.registry.WelkinBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class SuliSigil extends Sigil {
    public SuliSigil(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.SULI_SIGIL, pos, state);
    }
    @Override
    public Identifier getTexture() {
        return Welkin.id("block/sigil/suli");
    }
}
