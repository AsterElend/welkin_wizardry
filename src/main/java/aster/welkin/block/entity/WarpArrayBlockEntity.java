package aster.welkin.block.entity;

import aster.welkin.api.Linkable;
import aster.welkin.api.state.WarpLinkState;
import aster.welkin.registry.WelkinBlockEntities;
import aster.welkin.registry.WelkinEffects;
import aster.welkin.registry.WelkinItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

import static aster.welkin.api.WelkinUtil.yellAtEverything;

public class WarpArrayBlockEntity extends Linkable {
    private UUID linkId = null;
    private WARP_CORE core = WARP_CORE.NONE;



    public enum WARP_CORE {
        NONE,
        BLACK_HOLE,
        WHITE_HOLE
    }

    public WarpArrayBlockEntity(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.WARP_CONTROLLER, pos, state);
    }

    public WARP_CORE getCore() {
        return core;
    }
    public UUID getLinkId(){return linkId;}

    public void acceptOnUse(BlockPos controllerPos, World world, PlayerEntity player, Hand hand) {
        if (!(world instanceof ServerWorld serverWorld)) return;
        ItemStack stack = player.getStackInHand(hand);
        WarpLinkState link = WarpLinkState.get(serverWorld);

        if (stack.isEmpty()) { removeCore(player, link); return; }

        if (core == WARP_CORE.BLACK_HOLE && stack.isOf(WelkinItems.WHITE_HOLE_CORE) && !stack.hasNbt()) {
            stack.getOrCreateNbt().putUuid("linkId", this.linkId);
            return;
        }

        if (core != WARP_CORE.NONE) return;

        if (stack.isOf(WelkinItems.BLACK_HOLE_CORE)) {
            this.linkId = link.registerBlackEnd(controllerPos);
            core = WARP_CORE.BLACK_HOLE;
            stack.decrement(1);
            yellAtEverything(this);
        } else if (stack.isOf(WelkinItems.WHITE_HOLE_CORE)) {
            if (stack.hasNbt() && stack.getNbt().containsUuid("linkId")) {
                UUID id = stack.getNbt().getUuid("linkId");
                if (link.attachWhite(id, controllerPos)) {
                    this.linkId = id;
                    core = WARP_CORE.WHITE_HOLE;
                    stack.decrement(1);
                    yellAtEverything(this);
                }
            }
        }
    }

    private void removeCore(PlayerEntity player, WarpLinkState link) {
        if (core == WARP_CORE.NONE) return;

        if (core == WARP_CORE.BLACK_HOLE) {
            player.dropStack(new ItemStack(WelkinItems.BLACK_HOLE_CORE));
            if (linkId != null) link.removePair(linkId); // deletes the pair; any white end becomes orphaned
        } else if (core == WARP_CORE.WHITE_HOLE) {
            ItemStack toDrop = new ItemStack(WelkinItems.WHITE_HOLE_CORE);
            if (linkId != null) {
                toDrop.getOrCreateNbt().putUuid("linkId", linkId);
                link.clearWhiteEnd(linkId); // black survives, can accept a different white later
            }
            player.dropStack(toDrop);
        }

        core = WARP_CORE.NONE;
        linkId = null;
        yellAtEverything(this);
    }

    public void acceptSteppedOn(World world, BlockPos controllerPos, Entity entity) {
        if (!(entity instanceof LivingEntity living)) return;
        if (!(world instanceof ServerWorld serverWorld) || linkId == null) return;
        if (living.hasStatusEffect(WelkinEffects.WARP_COOLDOWN)) return;
        if (!world.isSkyVisible(controllerPos.up())) return;

        WarpLinkState link = WarpLinkState.get(serverWorld);
        if (core == WARP_CORE.BLACK_HOLE) {
            if (!(world.getBlockEntity(linkPos) instanceof ThunderheadBlockEntity thunderhead)) return;
            if (!thunderhead.hasSufficientAether(1600)) return;
            BlockPos target = link.getWhitePos(linkId);
            if (target == null) return;
            long t = ((world.getTimeOfDay() % 24000L) + 24000L) % 24000L;
            if (!isNearVertical((int) t)) return;
            thunderhead.acceptAether(-1600);
            teleportTo(target, living);
            link.setCharged(linkId, true);
        } else if (core == WARP_CORE.WHITE_HOLE && link.isCharged(linkId)) {

            BlockPos target = link.getBlackPos(linkId);
            if (target == null) return;

            teleportTo(target, living);
            link.setCharged(linkId, false);
        }
    }

    private void teleportTo(BlockPos target, LivingEntity entity) {
        Vec3d landing = target.up().toCenterPos();
        entity.addStatusEffect(new StatusEffectInstance(WelkinEffects.WARP_COOLDOWN, 80));
        entity.teleport(landing.getX(), landing.getY(), landing.getZ());
    }




    private static boolean isNearVertical(int time) {
        int t = ((time % 24000) + 24000) % 24000; // normalize to [0, 24000)
        boolean nearSun = t >= 5512 && t <= 6488;
        boolean nearMoon = t >= 17720 && t <= 18280;
        return nearSun || nearMoon;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("linkId")){
            linkId = nbt.getUuid("linkId");
        }

        core = WARP_CORE.values()[nbt.getInt("core")];
        readLink(nbt);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        if (linkId != null){
            nbt.putUuid("linkId", linkId);
        }
        nbt.putInt("core", core.ordinal());
        writeLink(nbt);

    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}