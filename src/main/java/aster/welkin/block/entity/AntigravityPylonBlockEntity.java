package aster.welkin.block.entity;

import aster.welkin.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class AntigravityPylonBlockEntity extends BlockEntity  {
    private static final int INTERVAL = 20;
    private static final int BASE_RANGE = 16;
    public int multiplier = 1;
    private int counter = 0;

    public void tick(World world, BlockPos pos){
        if (++counter < INTERVAL) return;
        counter = 0;
        Box box = new Box(pos).expand(BASE_RANGE * multiplier);

        List<PlayerEntity> players = world.getEntitiesByClass(PlayerEntity.class, box, p -> !p.isCreative() && !p.isSpectator());

        for (PlayerEntity player: players){
            grantFlight((ServerPlayerEntity) player);
        }

    }
    public AntigravityPylonBlockEntity(BlockPos pos, BlockState state) {
        super(
                ModBlockEntities.ANTIGRAVITY_PYLON_ENTITY,
                pos,
                state
        );
    }
    private void grantFlight(ServerPlayerEntity player){
        player.getAbilities().allowFlying = true;
        player.sendAbilitiesUpdate();
    }
}
