package aster.welkin.client;

import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;

public class SongweaverRenderLayers extends RenderLayer{

    public static final RenderLayer WARD_LAYER = RenderLayer.of(
            "ward_layer",
            VertexFormats.POSITION_COLOR,
            VertexFormat.DrawMode.QUADS,
            256,
            true,
            true,
            MultiPhaseParameters.builder()
                    .program(RenderPhase.COLOR_PROGRAM)
                    .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                    .writeMaskState(RenderPhase.ALL_MASK)
                    .cull(RenderPhase.DISABLE_CULLING)
                    .build(true)
    );



    public SongweaverRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }
}