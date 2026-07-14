package aster.welkin.item.baton;

/*
public class AbscondBatonItem extends Item {

    private static final String STORED_BLOCK = "StoredBlock";
    private static final String STORED_STATE = "StoredState";
    private static final String STORED_BE = "StoredBE";
    private static final String FORCE_TIMER = "ForceTimer";

    // 20 ticks = 1 second
    public static final int FORCE_DRAIN_INTERVAL = 1;

    public AbscondBatonItem(Settings settings) {
        super(settings);
    }

    // ---------------------------
    //    PICKUP / PLACE LOGIC
    // ---------------------------

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        World world = ctx.getWorld();
        if (world.isClient) return ActionResult.SUCCESS;

        PlayerEntity player = ctx.getPlayer();
        if (player == null) return ActionResult.PASS;

        ItemStack stack = ctx.getStack();
        BlockPos pos = ctx.getBlockPos();
        Direction linkSide = ctx.getSide();

        if (!isCarrying(stack)) {
            return pickUpBlock(world, pos, player, stack);
        } else {
            return placeStoredBlock(world, pos, linkSide, player, stack);
        }
    }

    private ActionResult pickUpBlock(World world, BlockPos pos, PlayerEntity player, ItemStack stack) {
        BlockState state = world.getBlockState(pos);
        if (state.isAir()) return ActionResult.FAIL;

        // Allow-list check (welkin:abscondable)
        if (!state.isIn(WelkinTags.ABSCONDABLE)) {
            player.sendMessage(Text.literal("This block cannot be moved.").formatted(Formatting.RED), true);
            return ActionResult.FAIL;
        }

        NbtCompound nbt = stack.getOrCreateNbt();

        // Save Block ID
        Identifier blockId = Registries.BLOCK.getId(state.getBlock());
        nbt.putString(STORED_BLOCK, blockId.toString());

        // Save full BlockState (with properties)
        nbt.put(STORED_STATE, NbtHelper.fromBlockState(state));

        // Save BlockEntity NBT (correct 1.20.1 API)
        BlockEntity be = world.getBlockEntity(pos);
        if (be != null) {
            be.readNbt(nbt.getCompound(STORED_BE));
            be.markDirty();
        }

        // Remove block
        world.removeBlock(pos, false);
        player.sendMessage(Text.literal("Picked up block.").formatted(Formatting.YELLOW), true);

        return ActionResult.SUCCESS;
    }
    // ✨ Public so the tick handler can auto-place on force depletion
    public ActionResult placeStoredBlock(World world, BlockPos pos, Direction linkSide, PlayerEntity player, ItemStack stack) {
        if (!isCarrying(stack)) return ActionResult.FAIL;

        NbtCompound nbt = stack.getOrCreateNbt();
        BlockPos placePos = pos.offset(linkSide);

        // Must be replaceable
        if (!world.getBlockState(placePos).isReplaceable()) {
            return ActionResult.FAIL;
        }

        // Load block ID
        Identifier blockId = new Identifier(nbt.getString(STORED_BLOCK));
        Block block = Registries.BLOCK.get(blockId);

        RegistryEntryLookup<Block> lookup = world.getRegistryManager().getWrapperOrThrow(RegistryKeys.BLOCK);

        // Load stored BlockState
        BlockState restoredState = NbtHelper.toBlockState(lookup, nbt.getCompound(STORED_STATE));




        // Place block
        world.setBlockState(placePos, restoredState);

        // Restore BlockEntity data
        if (nbt.contains(STORED_BE)) {
            BlockEntity be = world.getBlockEntity(placePos);
            if (be != null) {
                be.readNbt(nbt.getCompound(STORED_BE));
                be.markDirty();
            }
        }

        clearStored(stack);
        player.sendMessage(Text.literal("Placed block.").formatted(Formatting.GREEN), true);

        return ActionResult.SUCCESS;
    }



    public static void tickCarriedBlock(PlayerEntity player) {
        ItemStack stack = player.getMainHandStack();
        if (!(stack.getItem() instanceof AbscondBatonItem) || !isCarrying(stack)) return;

        NbtCompound nbt = stack.getOrCreateNbt();

        int timer = nbt.getInt(FORCE_TIMER) + 1;
        if (timer < FORCE_DRAIN_INTERVAL) {
            nbt.putInt(FORCE_TIMER, timer);
            return;
        }


        // Reset 1-second timer
        nbt.putInt(FORCE_TIMER, 0);

        // Drain force

        SkyForceComponent force = WelkinEntityCC.FORCE.get(player);

        // Try consuming 1 force
        if (!force.consume(10)) {
            // Out of force → auto-place block at player's feet
            BlockPos pos = player.getBlockPos().down();
            ((AbscondBatonItem) stack.getItem()).placeStoredBlock(
                    player.getWorld(), pos, Direction.UP, player, stack
            );

            player.sendMessage(
                    Text.literal("You ran out of force! Dropping the block.")
                            .formatted(Formatting.RED),
                    true
            );
        }
    }

    // ------------------------------------------------------------
    //  Utility
    // ------------------------------------------------------------

    public static boolean isCarrying(ItemStack stack) {
        return stack.hasNbt() && stack.getNbt().contains(STORED_BLOCK);
    }

    private static void clearStored(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.remove(STORED_BLOCK);
        nbt.remove(STORED_STATE);
        nbt.remove(STORED_BE);
        nbt.remove(FORCE_TIMER);
    }

    // ------------------------------------------------------------
    //  Tooltip
    // ------------------------------------------------------------

    @Override
    public void appendTooltip(ItemStack stack, World world, java.util.List<Text> tooltip, TooltipContext context) {
        if (isCarrying(stack)) {
            tooltip.add(Text.literal("Carrying: " + stack.getNbt().getString(STORED_BLOCK))
                    .formatted(Formatting.YELLOW));
        } else {
            tooltip.add(Text.literal("Empty").formatted(Formatting.GRAY));
        }
    }
}


 */
