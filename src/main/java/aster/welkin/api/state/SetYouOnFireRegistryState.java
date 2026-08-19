package aster.welkin.api.state;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtLong;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.Collection;

public class SetYouOnFireRegistryState extends PersistentState {



    private final Multimap<ChunkPos, BlockPos> sigils = HashMultimap.create();

    public static SetYouOnFireRegistryState get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(
                SetYouOnFireRegistryState::fromNbt,   // read function
                SetYouOnFireRegistryState::new,       // supplier (new empty state)
                "setyouonfire_registry"               // save id
        );
    }

    public void add(BlockPos pos) {
        sigils.put(new ChunkPos(pos), pos);
        markDirty();
    }

    public void remove(BlockPos pos) {
        sigils.remove(new ChunkPos(pos), pos);
        markDirty();
    }

    public Collection<BlockPos> get(ChunkPos chunkPos) {
        return sigils.get(chunkPos);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList list = new NbtList();
        for (BlockPos pos : sigils.values()) {
            list.add(NbtLong.of(pos.asLong()));
        }
        nbt.put("positions", list);
        return nbt;
    }

    public static SetYouOnFireRegistryState fromNbt(NbtCompound nbt) {
        SetYouOnFireRegistryState state = new SetYouOnFireRegistryState();
        for (NbtElement el : nbt.getList("positions", NbtElement.LONG_TYPE)) {
            BlockPos pos = BlockPos.fromLong(((NbtLong) el).longValue());
            state.sigils.put(new ChunkPos(pos), pos);
        }
        return state;
    }

    public static void register(World world, BlockPos pos) {
        if (world.isClient) return;
        SetYouOnFireRegistryState.get((ServerWorld) world).add(pos);
    }

    public static void unregister(World world, BlockPos pos) {
        if (world.isClient) return;
        SetYouOnFireRegistryState.get((ServerWorld) world).remove(pos);
    }
}