package aster.welkin.api;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;
import java.awt.Color;

public class RainbowColorProvider implements BlockColorProvider {
    @Override
    public int getColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
        if (world == null || pos == null) {
            return 0xFFFFFF;
        }

        // 1. Calculate hue based on positions (scaled so 32 blocks repeats the loop)
        float hue = (pos.getX() + pos.getY() + pos.getZ()) / 32.0f;
        hue = hue - (float) Math.floor(hue); // Clamp between 0.0 and 1.0

        // 2. Pure mathematical HSB to RGB conversion (S=1.0, V=1.0)
        float h = hue * 6.0f;
        int i = (int) h;
        float f = h - i;

        int q = (int) ((1.0f - f) * 255.0f);
        int t = (int) (f * 255.0f);

        int r = 0, g = 0, b = 0;

        switch (i) {
            case 0 -> { r = 255; g = t;   b = 0;   }
            case 1 -> { r = q;   g = 255; b = 0;   }
            case 2 -> { r = 0;   g = 255; b = t;   }
            case 3 -> { r = 0;   g = q;   b = 255; }
            case 4 -> { r = t;   g = 0;   b = 255; }
            case 5 -> { r = 255; g = 0;   b = q;   }
        }

        // 3. Pack R, G, and B into a single 24-bit integer
        return (r << 16) | (g << 8) | b;
    }
}
