package aster.welkin.mixin;

import aster.welkin.api.EnchantableBoatEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity implements EnchantableBoatEntity {


    @Shadow
    private boolean pressingForward;

    @Shadow
    private boolean pressingBack;



    @Unique
    private static final double SWIFT_SNEAK_BOOST_PER_LEVEL = 0.06D; // tune to taste
    @Unique
    private static final double DEPTH_STRIDER_BOOST_PER_LEVEL = 0.2D;

    @Unique
    private static final TrackedData<NbtCompound> BOAT_ENCHANTMENTS =
            DataTracker.registerData(BoatEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void welkin$initTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(BOAT_ENCHANTMENTS, new NbtCompound());
    }


    @Override
    public Map<Enchantment, Integer> welkin$getBoatEnchantments() {
        Map<Enchantment, Integer> map = new LinkedHashMap<>();
        NbtCompound nbt = this.dataTracker.get(BOAT_ENCHANTMENTS);
        for (String key : nbt.getKeys()) {
            Identifier id = Identifier.tryParse(key);
            if (id == null) continue;
            Enchantment enchantment = Registries.ENCHANTMENT.get(id);
            if (enchantment == null) continue;
            map.put(enchantment, nbt.getInt(key));
        }
        return map;
    }

    @Override
    public void welkin$setBoatEnchantments(Map<Enchantment, Integer> enchantments) {
        NbtCompound nbt = new NbtCompound();
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Identifier id = Registries.ENCHANTMENT.getId(entry.getKey());
            if (id == null) continue;
            nbt.putInt(id.toString(), entry.getValue());
        }
        this.dataTracker.set(BOAT_ENCHANTMENTS, nbt);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void welkin$write(NbtCompound nbt, CallbackInfo ci) {
        nbt.put(NBT_KEY, this.dataTracker.get(BOAT_ENCHANTMENTS).copy());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void welkin$read(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(NBT_KEY, NbtElement.COMPOUND_TYPE)) {
            this.dataTracker.set(BOAT_ENCHANTMENTS, nbt.getCompound(NBT_KEY));
        }
    }
    
    @Inject(method = "tick", at = @At("TAIL"))
    private void welkin$applyMovementEnchants(CallbackInfo ci) {
        int swiftSneak = welkin$getLevelOfBoatEnchant(Enchantments.SWIFT_SNEAK);
        int depthStrider = welkin$getLevelOfBoatEnchant(Enchantments.DEPTH_STRIDER);

        if (swiftSneak <= 0 && depthStrider <= 0) return;
        if (!this.pressingForward || this.pressingBack) return;

        boolean inWater = this.isTouchingWater();
        Vec3d vel = this.getVelocity();
        Vec3d horizontal = new Vec3d(vel.x, 0, vel.z);

        if (horizontal.lengthSquared() < 1.0E-7) return;

        double boostPerLevel = inWater ? DEPTH_STRIDER_BOOST_PER_LEVEL : SWIFT_SNEAK_BOOST_PER_LEVEL;
        int level = inWater ? depthStrider : swiftSneak;
        if (level <= 0) return;

        Vec3d nudge = horizontal.normalize().multiply(boostPerLevel * level);
        this.setVelocity(vel.x + nudge.x, vel.y, vel.z + nudge.z);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void welkin$applyFrostWalker(CallbackInfo ci) {
        if (this.getWorld().isClient) return; // block edits are server-authoritative

        int level = welkin$getLevelOfBoatEnchant(Enchantments.FROST_WALKER);
        if (level <= 0) return;


        // throttle — freezing a big area every single tick is wasteful
        if (this.age % 3 != 0) return;

        welkin$freezeWater((Entity) (Object) this, this.getWorld(), this.getBlockPos(), level);
    }


    @Unique
      void welkin$freezeWater(Entity entity, World world, BlockPos blockPos, int level) {
        if (entity.isOnGround()) {
            BlockState blockState = Blocks.FROSTED_ICE.getDefaultState();
            int i = Math.min(16, 2 + level);
            BlockPos.Mutable mutable = new BlockPos.Mutable();

            for(BlockPos blockPos2 : BlockPos.iterate(blockPos.add(-i, -1, -i), blockPos.add(i, -1, i))) {
                if (blockPos2.isWithinDistance(entity.getPos(), (double)i)) {
                    mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
                    BlockState blockState2 = world.getBlockState(mutable);
                    if (blockState2.isAir()) {
                        BlockState blockState3 = world.getBlockState(blockPos2);
                        if (blockState3 == FrostedIceBlock.getMeltedState() && blockState.canPlaceAt(world, blockPos2) && world.canPlace(blockState, blockPos2, ShapeContext.absent())) {
                            world.setBlockState(blockPos2, blockState);
                            world.scheduleBlockTick(blockPos2, Blocks.FROSTED_ICE, MathHelper.nextInt(world.getRandom(), 60, 120));
                        }
                    }
                }
            }

        }
    }

    private BoatEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    private static final String NBT_KEY = "enchantments";






    @Redirect(
            method = "dropItems",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/BoatEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;")
    )
    private ItemEntity welkin$dropEnchanted(BoatEntity instance, ItemConvertible itemConvertible) {
        ItemStack stack = new ItemStack(itemConvertible);
        Map<Enchantment, Integer> enchantments = welkin$getBoatEnchantments();
        if (!enchantments.isEmpty()) {
            EnchantmentHelper.set(enchantments, stack);
        }
        return this.dropStack(stack);
    }
}