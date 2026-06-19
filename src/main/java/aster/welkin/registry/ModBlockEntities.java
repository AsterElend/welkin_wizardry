package aster.welkin.registry;

import aster.welkin.Welkin;
import aster.welkin.block.entity.*;
import aster.welkin.block.transducer.FluidTransducerEntity;
import aster.welkin.block.transducer.ItemTransducerEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

import static aster.welkin.Welkin.id;

public class ModBlockEntities {
    public static final BlockEntityType<NodeBlockEntity> NODE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Welkin.MOD_ID, "node_block_entity"),
                    FabricBlockEntityTypeBuilder.create(NodeBlockEntity::new,
                            ModBlocks.NODE).build(null));

    public static final BlockEntityType<PedestalBlockEntity> PEDESTAL_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Welkin.MOD_ID, "pylon_block_entity"),
                    FabricBlockEntityTypeBuilder.create(PedestalBlockEntity::new,
                            ModBlocks.PEDESTAL).build(null));

    public static final BlockEntityType<VoidBrazierBlockEntity> VOID_BRAZIER_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Welkin.MOD_ID, "brazier_block_entity"),
                    FabricBlockEntityTypeBuilder.create(VoidBrazierBlockEntity::new,
                            ModBlocks.VOID_BRAZIER).build(null));

    public static final BlockEntityType<CondenseBE> CONDENSER =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id("condenser"),
                    FabricBlockEntityTypeBuilder.create(CondenseBE::new,
                            ModBlocks.CONDENSER).build(null));


    public static final BlockEntityType<ItemTransducerEntity> ITEM_TRANSDUCER =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id("item_transducer"),
                    FabricBlockEntityTypeBuilder.create(ItemTransducerEntity::new, ModBlocks.ITEM_TRANSDUCER).build(null));

    public static final BlockEntityType<FluidTransducerEntity> FLUID_TRANSDUCER =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id("fluid_transducer"),
                    FabricBlockEntityTypeBuilder.create(FluidTransducerEntity::new, ModBlocks.FLUID_TRANSDUCER).build(null));

    public static final BlockEntityType<ExtractorBlockEntity> ATTRIBUTE_EXTRACTOR =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id("attribute_extractor"),
                    FabricBlockEntityTypeBuilder.create(ExtractorBlockEntity::new, ModBlocks.ATTRIBUTE_EXTRACTOR).build(null));
   public static final BlockEntityType<ExtractorBlockEntity> ALCHEMICAL_RECYCLER_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id("alchemical_recycler"),
                    FabricBlockEntityTypeBuilder.create(ExtractorBlockEntity::new, ModBlocks.ALCHEMICAL_RECYCLER).build(null));
    public static final BlockEntityType<AntigravityPylonBlockEntity> ANTIGRAVITY_PYLON_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id("antigravity_pylon"),
                    FabricBlockEntityTypeBuilder.create(AntigravityPylonBlockEntity::new, ModBlocks.ANTIGRAVITY_PYLON).build(null));

    public static final BlockEntityType<TeapotBlockEntity> TEAPOT_ENTITY = register("teapot", TeapotBlockEntity::new, ModBlocks.TEAPOT);


//  public static final BlockEntityType<TankFrameEntity> TANK_FRAME =
 //           Registry.register(Registries.BLOCK_ENTITY_TYPE, id("tankframe"),
 //                   FabricBlockEntityTypeBuilder.create(TankFrameEntity::new,
 //                           ModBlocks.TANK_FRAME).build(null));

    public static <T extends BlockEntity> BlockEntityType<T> register(String id, FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Welkin.id(id), FabricBlockEntityTypeBuilder.create(factory, blocks).build());
    }


    public static void registerBlockEntities(){
        Welkin.LOGGER.info(("registering blockentites for WW"));
    }






}
