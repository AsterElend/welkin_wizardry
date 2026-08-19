package aster.welkin.item.baton;

import aster.welkin.api.WelkinUtil;
import aster.welkin.registry.WelkinBlocks;
import aster.welkin.registry.WelkinItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class InscriberBatonItem extends Item {

    public InscriberBatonItem(Settings settings) {
        super(settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient) return TypedActionResult.pass(stack);

        if (!user.isSneaking()){
            int currentMode = getMode(stack);
            int nextMode = (currentMode + 1) % 2;
            setMode(stack, nextMode);
            return TypedActionResult.success(stack);
        } else {
            return TypedActionResult.pass(stack);
        }

    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx){
        World world = ctx.getWorld();
        if (world.isClient || !ctx.getPlayer().isSneaking()) return ActionResult.PASS;
        ItemStack stack = ctx.getStack();
        int currentMode = getMode(stack);
        switch(currentMode){
            case 0 ->{
                WelkinUtil.placeBlockWithoutItem(ctx, WelkinBlocks.TENPO_SIGIL);
            }
            case 1 ->{
               WelkinUtil.placeBlockWithoutItem(ctx, WelkinBlocks.SULI_SIGIL);
            }
        }
        return ActionResult.SUCCESS;
    }

    public int getMode(ItemStack stack) {
        if (stack.hasNbt() && stack.getOrCreateNbt().contains("WandMode")) {
            return stack.getNbt().getInt("WandMode");
        }
        return 0; // Default mode
    }

    public void setMode(ItemStack stack, int mode) {
        stack.getOrCreateNbt().putInt("WandMode", mode);
    }

    @Override
    public Text getName(ItemStack stack) {
        if (stack.isOf(WelkinItems.INSCRIBER_BATON)){
            int mode = getMode(stack);
            Text modeText =
            switch (mode){
                case 0 ->  Text.translatable("welkin.sigil.tenpo");
                case 1 ->  Text.translatable("welkin.sigil.suli");
                default -> Text.translatable("welkin.sigil.error.how?");
            };

            return Text.translatable(this.getTranslationKey()).append(" (").append(modeText.getWithStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)).get(0))
                    .append(")");
        }
        else return super.getName(stack);
    }


}
