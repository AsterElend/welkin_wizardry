package aster.welkin.client;

import aster.welkin.api.AetherHolder;
import aster.welkin.api.EnchantableBoatEntity;
import aster.welkin.api.IHasLensInfo;
import aster.welkin.api.Linkable;
import aster.welkin.block.WarpArrayBlock;
import aster.welkin.block.entity.WarpArrayBlockEntity;
import aster.welkin.registry.WelkinItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;

public class OculatorLensOverlays {
    public static void addOculatorLensStuff(){
        OculatorInfoRegistry.addPredicateDisplayer(
                (state, pos, observer, world, direction) -> state.getBlock() instanceof IHasLensInfo,
                (lines, state, pos, observer, world, direction) -> {
                    if (world.getBlockEntity(pos) instanceof IHasLensInfo hasLensInfo) {
                        hasLensInfo.applyLensOverlay(lines, state, pos, observer, world, direction);
                    } else if ((world.getBlockEntity(pos) instanceof AetherHolder holder)) {
                        lines.add(new Pair<>(new ItemStack(WelkinItems.CHARGESTONE),
                                Text.translatable("welkin.scry.thunderhead").append(String.valueOf(holder.getAether()))));
                    }
                }
        );



        OculatorInfoRegistry.addPredicateDisplayer((state, pos, observer, world, hitFace) ->
                (state.getBlock() instanceof WarpArrayBlock.Controller),
                ((lines, state, pos, observer, world, hitFace) -> {

                    if (world.getBlockEntity(pos) instanceof WarpArrayBlockEntity warpArray){
                        //I'll work out how to get a persistentState into the client later
                      //  WarpLinkState link = WarpLinkState.get((ServerWorld) world);
                        if (warpArray.getCore() != WarpArrayBlockEntity.WARP_CORE.NONE){
                              switch (warpArray.getCore()){
                                case WHITE_HOLE -> {

                                    lines.add(new Pair<>(new ItemStack(WelkinItems.WHITE_HOLE_CORE), Text.empty()));

                                }
                                case BLACK_HOLE -> {

                                        lines.add(new Pair<>(new ItemStack(WelkinItems.BLACK_HOLE_CORE), Text.empty()));


                                }

                            };


                        }

                    }
                })
);

        OculatorInfoRegistry.addPredicateDisplayer((state, pos, observer, world, hitFace) ->
                        world.getBlockEntity(pos) instanceof Linkable,
                ((lines, state, pos, observer, world, hitFace) -> {
                    Linkable entity = (Linkable) world.getBlockEntity(pos);
                    //the entity has to exist, we just checked with a predicate, calm down java
                    if (entity.getLinkPos() == null) {
                        lines.add(new Pair<>(new ItemStack(Items.SLIME_BALL),
                                Text.translatable("welkin.scry.linkable.idle").setStyle(Style.EMPTY.withColor(Formatting.BLUE))));
                    } else {
                        lines.add(new Pair<>(new ItemStack(Items.SPECTRAL_ARROW),
                                Text.translatable("welkin.scry.linkable.linked").append(" ").append(entity.getLinkPos().toShortString())
                        ));
                        lines.add(new Pair<>(ItemStack.EMPTY,
                                Text.translatable("welkin.scry.linkable.side1").append(entity.getLinkSide().getName())
                                        .append(Text.translatable("welkin.scry.linkable.side2"))));
                    }
                }));




        OculatorInfoRegistry.addEntityPredicateDisplayer(
                (entity, observer, world) -> entity instanceof EnchantableBoatEntity,
                (lines, entity, observer, world) -> {
                    var enchantable = (EnchantableBoatEntity) entity;
                    var enchantments = enchantable.welkin$getBoatEnchantments();
                    if (enchantments.isEmpty()) return;

                    for (var entry : enchantments.entrySet()) {
                        Text name = entry.getKey().getName(entry.getValue());
                        lines.add(new Pair<>(ItemStack.EMPTY, name));
                    }
                }
        );


    }
}
