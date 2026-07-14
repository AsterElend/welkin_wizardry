package aster.welkin.client;

import aster.welkin.api.EnchantableBoatEntity;
import aster.welkin.api.IHasLensInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

public class OculatorLensOverlays {
    public static void addOculatorLensStuff(){
        OculatorInfoRegistry.addPredicateDisplayer(
                (state, pos, observer, world, direction) -> state.getBlock() instanceof IHasLensInfo,
                (lines, state, pos, observer, world, direction) -> {
                    if (world.getBlockEntity(pos) instanceof IHasLensInfo hasLensInfo) {
                        hasLensInfo.applyLensOverlay(lines, state, pos, observer, world, direction);
                    }
                }
        );

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
