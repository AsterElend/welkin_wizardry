package aster.welkin.block.transducer;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class WireConnectionComponent {
    private static final Set<WireConnection> CONNECTIONS = new HashSet<>();


    public record WireConnection(BlockPos a, BlockPos b) {}


    public static void addConnection(World world, BlockPos a, BlockPos b) {
        CONNECTIONS.add(new WireConnection(a, b));
    }


    public static Set<WireConnection> getConnections() {
        return CONNECTIONS;
    }
}
