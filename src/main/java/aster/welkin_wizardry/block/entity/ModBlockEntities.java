package aster.welkin_wizardry.block.entity;

import aster.welkin_wizardry.WelkinWizardry;
import aster.welkin_wizardry.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<NodeBlockEntity> NODE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(WelkinWizardry.MOD_ID, "node_block_entity"),
                    FabricBlockEntityTypeBuilder.create(NodeBlockEntity::new,
                            ModBlocks.NODE).build(null));
    public static void registerBlockEntities(){
        WelkinWizardry.LOGGER.info(("registering blockentites for WW"));
    }
}
