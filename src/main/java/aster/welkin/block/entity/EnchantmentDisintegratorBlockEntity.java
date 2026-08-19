package aster.welkin.block.entity;

import aster.welkin.api.IHasLensInfo;
import aster.welkin.api.LinkablePedestal;
import aster.welkin.api.WelkinUtil;
import aster.welkin.registry.WelkinBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class EnchantmentDisintegratorBlockEntity extends LinkablePedestal implements IHasLensInfo {
  private int cooldown;
    private final int BASE_COOLDOWN = 100;
    public EnchantmentDisintegratorBlockEntity(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.ENCHANTMENT_DISINTEGRATOR, pos, state);
        this.cooldown = BASE_COOLDOWN;
    }
    public void tick(BlockPos pos, World world){
        if (world.isReceivingRedstonePower(pos)) return;
        if (this.logic.isEmpty()) return;
        ItemStack stack = this.logic.getStack();
        if (!stack.hasEnchantments()) return;
        if (cooldown >= 0){
            cooldown--;
            return;
        }
        if (!(world.getBlockEntity(linkPos) instanceof ThunderheadBlockEntity thunder)) {
            cooldown = BASE_COOLDOWN;
            return;
        };
        Random random = world.getRandom();
        Enchantment chosen = pickOriginalEnchantment(random, stack);
        int level = EnchantmentHelper.getLevel(chosen, stack);

        int aetherCost = (int) ((Math.pow(level, level)) * -100);
        if (!thunder.hasSufficientAether(aetherCost)) {
            cooldown = BASE_COOLDOWN;
            return;
        };

        swapSelectedEnchantment(random, stack, chosen, level);
        WelkinUtil.yellAtEverything(this);
        thunder.acceptAether(aetherCost);

        cooldown = BASE_COOLDOWN;



    }
    @Override
    public int getMaxCountPerStack(){
        return 1;
    }

    public static Enchantment pickOriginalEnchantment(Random random, ItemStack stack) {
        Map<Enchantment, Integer> currentEnchants = EnchantmentHelper.get(stack);
        if (currentEnchants.isEmpty()) {
            return null;
        }
        return currentEnchants.keySet().stream().toList().get(random.nextInt(currentEnchants.size()));
    }

    public static void swapSelectedEnchantment(Random random, ItemStack stack, Enchantment originalEnchant, int originalLevel) {
        Map<Enchantment, Integer> currentEnchants = EnchantmentHelper.get(stack);
        // Remove the chosen original enchantment from the pool before testing conflicts
        currentEnchants.remove(originalEnchant);
        loop(currentEnchants, stack, random, originalLevel);
        loop(currentEnchants, stack, random, originalLevel);
        // Save modifications back to ItemStack NBT
        EnchantmentHelper.set(currentEnchants, stack);
    }

    private static void loop(Map<Enchantment, Integer> currentEnchants, ItemStack stack, Random random, int originalLevel) {
        List<Enchantment> possibleReplacements = Registries.ENCHANTMENT.stream()
                .filter(enchantment -> enchantment.isAcceptableItem(stack))
                .filter(enchantment -> !currentEnchants.containsKey(enchantment))
                .filter(enchantment -> isCompatibleWithRemaining(enchantment, currentEnchants))
                .toList();

        if (possibleReplacements.isEmpty()) {
            return; // No valid alternative available; exit without modifying stack
        }

        Enchantment newEnchant = possibleReplacements.get(random.nextInt(possibleReplacements.size()));
        int newLevel = originalLevel - 1;

        if (newLevel > 0) {
            // Cap at the new enchantment's maximum native level limit
            newLevel = Math.min(newLevel, newEnchant.getMaxLevel());
            currentEnchants.put(newEnchant, newLevel);
        }
    }

    private static boolean isCompatibleWithRemaining(Enchantment target, Map<Enchantment, Integer> remainingEnchants) {
        for (Enchantment activeEnchant : remainingEnchants.keySet()) {
            if (!target.canCombine(activeEnchant)) {
                return false; // Found a conflict (e.g., Silk Touch vs Fortune)
            }
        }
        return true;
    }
    @Override
    public void applyLensOverlay(List<Pair<ItemStack, StringVisitable>> lines,
                                 BlockState state, BlockPos pos,
                                 PlayerEntity observer, World world, Direction hitFace){
        var enchantments = EnchantmentHelper.get(this.logic.getStack());
        if (enchantments.isEmpty()) return;
        lines.add(new Pair<>(new ItemStack(Items.ENCHANTED_BOOK), Text.empty()));
        for (var entry : enchantments.entrySet()) {
            Text name = entry.getKey().getName(entry.getValue());
            lines.add(new Pair<>(ItemStack.EMPTY, name));
        }

    };

    @Override
    public void writeAdditionalData(NbtCompound nbt){
        nbt.putInt("cooldown", cooldown);
    }

    @Override
    public void readAdditionalData(NbtCompound nbt){
        cooldown = nbt.getInt("cooldown");
    }



}
