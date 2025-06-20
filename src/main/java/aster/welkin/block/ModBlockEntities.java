package aster.welkin.block;

import aster.welkin.Welkin;

import aster.welkin.block.fancy.focus.FocusBlockEntity;
import aster.welkin.block.fancy.node.NodeBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<NodeBlockEntity> NODE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Welkin.MOD_ID, "node_block_entity"),
                    FabricBlockEntityTypeBuilder.create(NodeBlockEntity::new,
                            ModBlocks.NODE).build(null));

    public static final BlockEntityType<FocusBlockEntity> FOCUS =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Welkin.MOD_ID, "focus_block_entity"),
                    FabricBlockEntityTypeBuilder.create(FocusBlockEntity::new,
                            ModBlocks.FOCUS).build(null));

    public static void registerBlockEntities(){
        Welkin.LOGGER.info(("registering blockentites for WW"));
    }
}
