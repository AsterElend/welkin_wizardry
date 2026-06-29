package aster.welkin.block;

import aster.welkin.packet.ServerCutsceneManager;
import aster.welkin.registry.ModItems;
import aster.welkin.registry.WelkinTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class HeartExtractorBlock extends Block {
    public HeartExtractorBlock(Settings settings) {
        super(settings);
    }
    private UUID MODIFIED_HEARTS = UUID.fromString("db5d53dd-2eb6-4df2-88f1-f9e875154509");


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
       EntityAttributeInstance instance =  player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
       if (instance == null || world.isClient) return ActionResult.PASS;
       EntityAttributeModifier modifier = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).getModifier(MODIFIED_HEARTS);

        if (stack.isIn(WelkinTags.TAKE_HEART)){
           double value = 0;
           if (modifier != null){
               value += modifier.getValue();
               instance.removeModifier(MODIFIED_HEARTS);
           }

           instance.addPersistentModifier(new EntityAttributeModifier(MODIFIED_HEARTS, "modified_hearts", 2+value, EntityAttributeModifier.Operation.ADDITION));
           stack.decrement(1);

            if (player instanceof ServerPlayerEntity servPlay){
                ServerCutsceneManager.startSequence(servPlay, pos.up().up());
            }

           return ActionResult.SUCCESS;
        } else if (stack.isEmpty()){
            double value = 0;
            if (modifier != null){
                value += modifier.getValue();
                instance.removeModifier(MODIFIED_HEARTS);
            }
            instance.addPersistentModifier(new EntityAttributeModifier(MODIFIED_HEARTS, "modified_hearts", value-2, EntityAttributeModifier.Operation.ADDITION));
            if (!player.getWorld().isClient && player instanceof ServerPlayerEntity) {
                // Create an ItemStack (e.g., giving 1 Apple)
                ItemStack stackToGive = new ItemStack(ModItems.EXTRACTED_HEART, 1);

                // Try to insert the item into the player's inventory
                boolean success = player.getInventory().insertStack(stackToGive);

                if (!success) {
                    // Drop the item on the ground if the inventory is full
                    player.dropItem(stackToGive, false);
                }
            }
            if (player instanceof ServerPlayerEntity servPlay){
                ServerCutsceneManager.startSequence(servPlay, pos.up().up());
            }

            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }


}
