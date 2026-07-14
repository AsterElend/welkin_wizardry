package aster.welkin.registry;

import aster.welkin.Welkin;
import aster.welkin.block.entity.*;
import aster.welkin.block.sigil.SigilBlock;
import aster.welkin.block.sigil.TenpoSigil;
import aster.welkin.block.transducer.FluidTransducerEntity;
import aster.welkin.block.transducer.ItemTransducerEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static aster.welkin.Welkin.id;

public class WelkinBlockEntities {
    public static final BlockEntityType<NodeBlockEntity> NODE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Welkin.MOD_ID, "node_block_entity"),
                    FabricBlockEntityTypeBuilder.create(NodeBlockEntity::new,
                            WelkinBlocks.NODE).build(null));

    public static final BlockEntityType<PylonBlockEntity> PYLON =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Welkin.MOD_ID, "pylon_block_entity"),
                    FabricBlockEntityTypeBuilder.create(PylonBlockEntity::new,
                            WelkinBlocks.PYLON).build(null));


    public static final BlockEntityType<CondenserBE> CONDENSER =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id("condenser"),
                    FabricBlockEntityTypeBuilder.create(CondenserBE::new,
                            WelkinBlocks.CONDENSER).build(null));


    public static final BlockEntityType<ItemTransducerEntity> ITEM_TRANSDUCER =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id("item_transducer"),
                    FabricBlockEntityTypeBuilder.create(ItemTransducerEntity::new, WelkinBlocks.ITEM_TRANSDUCER).build(null));

    public static final BlockEntityType<FluidTransducerEntity> FLUID_TRANSDUCER =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id("fluid_transducer"),
                    FabricBlockEntityTypeBuilder.create(FluidTransducerEntity::new, WelkinBlocks.FLUID_TRANSDUCER).build(null));


    public static final BlockEntityType<AntigravityPylonBlockEntity> ANTIGRAVITY_PYLON =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id("antigravity_pylon"),
                    FabricBlockEntityTypeBuilder.create(AntigravityPylonBlockEntity::new, WelkinBlocks.ANTIGRAVITY_PYLON).build(null));

    public static final BlockEntityType<TeapotBlockEntity> TEAPOT = register("teapot", TeapotBlockEntity::new, WelkinBlocks.TEAPOT);
    public static final BlockEntityType<AgoniteTransmuterEntity> AGONITE_TRANSMUTER = register("agonite_transmuter", AgoniteTransmuterEntity::new,
            WelkinBlocks.AGONITE_TRANSMUTER);

    public static final BlockEntityType<ChristeningAltarBlockEntity> CHRISTENING_ALTAR = register("christening_altar_entity",
            ChristeningAltarBlockEntity::new, WelkinBlocks.CHRISTENING_ALTAR);
    public static final BlockEntityType<ThunderheadBlockEntity> THUNDERHEAD = register("thunderhead",
            ThunderheadBlockEntity::new, WelkinBlocks.THUNDERHEAD);
    public static final BlockEntityType<EchoingRecieverBlockEntity> ECHOING_RECEIVER = register("echoing_receiver",
            EchoingRecieverBlockEntity::new, WelkinBlocks.ECHOING_RECEIVER);
    public static final BlockEntityType<TenpoSigil> TENPO_SIGIL = register("tenpo_sigil", TenpoSigil::new, WelkinBlocks.TENPO_SIGIL);

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
