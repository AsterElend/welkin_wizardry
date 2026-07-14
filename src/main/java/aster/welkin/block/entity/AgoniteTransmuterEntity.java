package aster.welkin.block.entity;

import aster.welkin.api.PedestalLikeBlockEntity;
import aster.welkin.recipes.AgoniteTransmutationRecipe;
import aster.welkin.registry.WelkinBlockEntities;
import aster.welkin.registry.WelkinRecipes;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class AgoniteTransmuterEntity extends PedestalLikeBlockEntity {
    public float stockpiledPain = 0;


    public void addToStockpile(float toStockpile){
        stockpiledPain += toStockpile;
    }

    public AgoniteTransmuterEntity(BlockPos pos, BlockState state) {
        super(WelkinBlockEntities.AGONITE_TRANSMUTER, pos, state);
    }

    public void runRecipe(){
        SimpleInventory inv = new SimpleInventory(getStack());
        Optional<AgoniteTransmutationRecipe> maybeGet = this.getWorld().getServer().getRecipeManager().getFirstMatch(
                WelkinRecipes.AGONY_TYPE, inv, this.getWorld()
        );
        if (maybeGet.isEmpty()) return;
        AgoniteTransmutationRecipe recipe = maybeGet.get();
        int toTransmuteCount = getCount();
        float required = recipe.getPain() * toTransmuteCount;
        if (required < stockpiledPain) return;
        setStack(new ItemStack(recipe.getBaseOutput().getItem(), toTransmuteCount));
        stockpiledPain -= required;


    }

    @Override
    public void readAdditionalData(NbtCompound nbt){
        stockpiledPain = nbt.getFloat("pain");
    }

    @Override
    public void storeAdditionalData(NbtCompound nbt){
        nbt.putFloat("pain", stockpiledPain);
    }
}
