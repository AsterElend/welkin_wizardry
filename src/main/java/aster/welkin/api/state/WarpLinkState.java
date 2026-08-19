package aster.welkin.api.state;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WarpLinkState extends PersistentState {

    private final Map<UUID, WarpRegistryEntry> warpArrays = new HashMap<>();


    public UUID registerBlackEnd(BlockPos pos){
        UUID id = UUID.randomUUID();
        warpArrays.put(id, new WarpRegistryEntry(pos));
        markDirty();
        return id;
    }

    public boolean attachWhite(UUID id, BlockPos pos){
        WarpRegistryEntry entry = warpArrays.get(id);
        if (entry == null || entry.whitePos != null) return false;
        entry.whitePos = pos;
        markDirty();
        return true;
    }

    public void clearWhiteEnd(UUID id){
        WarpRegistryEntry d = warpArrays.get(id);
        if (d != null && d.whitePos != null){
            d.whitePos = null;
            d.returnWarpCharged = false;
        }
    }
    @Nullable
    public BlockPos getBlackPos(UUID id) { var d = warpArrays.get(id); return d == null ? null : d.blackPos; }
    @Nullable public BlockPos getWhitePos(UUID id) { var d = warpArrays.get(id); return d == null ? null : d.whitePos; }
    public boolean isCharged(UUID id) { var d = warpArrays.get(id); return d != null && d.returnWarpCharged; }

    public void setCharged(UUID id, boolean value) {
        WarpRegistryEntry d = warpArrays.get(id);
        if (d != null) { d.returnWarpCharged = value; markDirty(); }
    }

    public void removePair(UUID id){
        if (warpArrays.remove(id) != null) markDirty();
    }


    public static class WarpRegistryEntry {
        BlockPos blackPos;
        BlockPos whitePos;
        boolean returnWarpCharged;

        protected WarpRegistryEntry(BlockPos blackPos){
            this.returnWarpCharged = false;
            this.blackPos = blackPos;
        }
        public boolean isReturnWarpCharged(){
            return returnWarpCharged;
        }
        public BlockPos getWhitePos(){
            return whitePos;
        }
        public BlockPos getBlackPos(){
            return blackPos;
        }
    }

    public static WarpLinkState get(ServerWorld world) {

        return world.getPersistentStateManager().getOrCreate(
                WarpLinkState::fromNbt,
                WarpLinkState::new,
                "warp_array_pairs"
        );
    }


    private static WarpLinkState fromNbt(NbtCompound nbt) {
        WarpLinkState s = new WarpLinkState();
        for (NbtElement el : nbt.getList("links", NbtElement.COMPOUND_TYPE)) {
            NbtCompound c = (NbtCompound) el;
            if (!c.contains("blackPos")) continue;
            WarpRegistryEntry entry = new WarpRegistryEntry(BlockPos.fromLong(c.getLong("blackPos")));
            if (c.contains("whitePos")) entry.whitePos = BlockPos.fromLong(c.getLong("whitePos"));
            if (c.contains("charged")){
                entry.returnWarpCharged = c.getBoolean("charged");
            }
            s.warpArrays.put(c.getUuid("id"), entry);
        }
        return s;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList list = new NbtList();
        for (var e : warpArrays.entrySet()) {
            NbtCompound c = new NbtCompound();
            c.putUuid("id", e.getKey());
            if (e.getValue().blackPos != null) c.putLong("blackPos", e.getValue().blackPos.asLong());
            if (e.getValue().whitePos != null) c.putLong("whitePos", e.getValue().whitePos.asLong());
            c.putBoolean("charged", e.getValue().returnWarpCharged);
            list.add(c);
        }
        nbt.put("links", list);
        return nbt;
    }
    public Map<UUID, WarpRegistryEntry> getWarpArrays() {
        return Collections.unmodifiableMap(this.warpArrays);
    }
}
