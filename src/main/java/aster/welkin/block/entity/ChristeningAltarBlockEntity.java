package aster.welkin.block.entity;

import aster.welkin.api.chat.ChatReceiver;
import aster.welkin.api.PedestalLikeBlockEntity;
import aster.welkin.registry.WelkinBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChristeningAltarBlockEntity extends PedestalLikeBlockEntity implements ChatReceiver {

    public ChristeningAltarBlockEntity(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.CHRISTENING_ALTAR, pos, state);
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        registerChatReceiver();
    }

    @Override
    public void markRemoved() {
        super.markRemoved();
        unregisterChatReceiver();
    }

    @Override
    public boolean onChatReceived(String message, ServerPlayerEntity sender) {
        ItemStack stack = getStack();
        if (stack.isEmpty()) {
            return false;
        }

        stack.setCustomName(Text.literal(message));
        inventoryChanged();
        spawnEnchantParticles();
        return true;
    }

    private void spawnEnchantParticles() {
        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }
        serverWorld.spawnParticles(
                ParticleTypes.ENCHANT,
                pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5,
                30,           // count
                0.3, 0.4, 0.3, // spread on x/y/z
                1.0            // speed multiplier
        );
    }
}