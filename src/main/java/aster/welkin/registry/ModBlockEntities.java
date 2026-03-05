package aster.welkin.registry;

import aster.welkin.Welkin;

import aster.welkin.block.brazier.VoidBrazierBlockEntity;
import aster.welkin.block.condese.CondenseBE;
import aster.welkin.block.extractor.ExtractorBlockEntity;
import aster.welkin.block.pylon.PylonBlockEntity;
import aster.welkin.block.node.NodeBlockEntity;
//import aster.welkin.block.tank.TankControllerEntity;
//import aster.welkin.block.tank.TankFrameEntity;
import aster.welkin.block.transducer.TransducerBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static aster.welkin.Welkin.id;

public class ModBlockEntities {
    public static final BlockEntityType<NodeBlockEntity> NODE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Welkin.MOD_ID, "node_block_entity"),
                    FabricBlockEntityTypeBuilder.create(NodeBlockEntity::new,
                            ModBlocks.NODE).build(null));

    public static final BlockEntityType<PylonBlockEntity> PYLON =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Welkin.MOD_ID, "pylon_block_entity"),
                    FabricBlockEntityTypeBuilder.create(PylonBlockEntity::new,
                            ModBlocks.PYLON).build(null));

    public static final BlockEntityType<VoidBrazierBlockEntity> BRAZIER =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Welkin.MOD_ID, "brazier_block_entity"),
                    FabricBlockEntityTypeBuilder.create(VoidBrazierBlockEntity::new,
                            ModBlocks.BRAZIER).build(null));

    public static final BlockEntityType<CondenseBE> CONDENSER =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id("condense"),
                    FabricBlockEntityTypeBuilder.create(CondenseBE::new,
                            ModBlocks.CONDENSER).build(null));

    public static final BlockEntityType<TransducerBlockEntity> TRANSDUCER =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id("transducer"),
                    FabricBlockEntityTypeBuilder.create(TransducerBlockEntity::new, ModBlocks.TRANSDUCER).build(null));

    public static final BlockEntityType<ExtractorBlockEntity> EXTRACTOR =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id("extractor"),
                    FabricBlockEntityTypeBuilder.create(ExtractorBlockEntity::new, ModBlocks.EXTRACTOR).build(null));
/*
    public static final BlockEntityType<TankControllerEntity> TANK_CONTROLLER =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id("tankcontrol"),
                    FabricBlockEntityTypeBuilder.create(TankControllerEntity::new,
                            ModBlocks.TANKCONTROLLER).build(null));

  public static final BlockEntityType<TankFrameEntity> TANK_FRAME =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id("tankframe"),
                    FabricBlockEntityTypeBuilder.create(TankFrameEntity::new,
                            ModBlocks.TANK_FRAME).build(null));
*/
    public static void registerBlockEntities(){
        Welkin.LOGGER.info(("registering blockentites for WW"));
    }
}
