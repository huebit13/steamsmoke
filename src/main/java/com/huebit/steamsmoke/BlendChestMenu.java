package com.huebit.steamsmoke;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlendChestMenu extends AbstractContainerMenu {

    private static final int TOTAL_SLOTS      = BlendChestItem.TOTAL_SLOTS;      // 54
    private static final int MIXTURE_SLOTS    = BlendChestItem.MIXTURE_SLOTS;    // 18
    private static final int INGREDIENT_SLOTS = BlendChestItem.INGREDIENT_SLOTS; // 36

    final SimpleContainer container;
    private final Player player;
    @Nullable private final InteractionHand hand;
    @Nullable private final BlendChestBlockEntity blockEntity;
    private boolean suppressSave = false;

    // Network constructor used by client side
    public BlendChestMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        super(ModMenuTypes.BLEND_CHEST.get(), id);
        this.player = playerInv.player;
        this.hand = null;
        this.blockEntity = null;
        this.container = new SimpleContainer(TOTAL_SLOTS);

        boolean isBlock = buf.readBoolean();
        if (isBlock) buf.readBlockPos();
        else buf.readEnum(InteractionHand.class);

        addAllSlots(playerInv);
    }

    private BlendChestMenu(int id, Inventory playerInv, SimpleContainer container,
            @Nullable InteractionHand hand, @Nullable BlendChestBlockEntity blockEntity) {
        super(ModMenuTypes.BLEND_CHEST.get(), id);
        this.player = playerInv.player;
        this.hand = hand;
        this.blockEntity = blockEntity;
        this.container = container;

        addAllSlots(playerInv);

        if (!player.level().isClientSide()) {
            container.addListener(c -> saveToSource());
        }
    }

    public static BlendChestMenu fromHand(int id, Inventory playerInv, InteractionHand hand) {
        SimpleContainer container = new SimpleContainer(TOTAL_SLOTS);
        ItemStack stack = playerInv.player.getItemInHand(hand);
        NonNullList<ItemStack> items = BlendChestItem.getContents(stack);
        for (int i = 0; i < TOTAL_SLOTS; i++) container.setItem(i, items.get(i));
        return new BlendChestMenu(id, playerInv, container, hand, null);
    }

    public static BlendChestMenu fromBlock(int id, Inventory playerInv, BlendChestBlockEntity be) {
        // Share the block entity's container directly so changes persist to the BE automatically
        return new BlendChestMenu(id, playerInv, be.container, null, be);
    }

    private void addAllSlots(Inventory playerInv) {
        // Mixture slots (0-17): 2 rows × 9, y = 24 + row*18
        for (int i = 0; i < MIXTURE_SLOTS; i++) {
            int row = i / 9, col = i % 9;
            addSlot(new Slot(container, i, 8 + col * 18, 24 + row * 18) {
                @Override public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.is(ModItems.MIXTURE.get());
                }
            });
        }

        // Ingredient slots (18-53): 4 rows × 9, y = 72 + row*18
        for (int i = 0; i < INGREDIENT_SLOTS; i++) {
            int row = i / 9, col = i % 9;
            addSlot(new Slot(container, MIXTURE_SLOTS + i, 8 + col * 18, 72 + row * 18) {
                @Override public boolean mayPlace(@NotNull ItemStack stack) {
                    return BlendPouchItem.isIngredient(stack);
                }
            });
        }

        // Player inventory (slots 54-80), y = 176 + row*18
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 176 + row * 18));
            }
        }
        // Hotbar (slots 81-89), y=234
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInv, col, 8 + col * 18, 234));
        }
    }

    void collectFromInventory() {
        suppressSave = true;
        try {
            net.minecraft.world.entity.player.Inventory inv = player.getInventory();
            for (int invSlot = 0; invSlot < 36; invSlot++) {
                ItemStack stack = inv.getItem(invSlot);
                if (stack.isEmpty() || stack.is(ModItems.BLEND_CHEST_ITEM.get())) continue;

                if (stack.is(ModItems.MIXTURE.get())) {
                    tryMoveToSection(stack, 0, MIXTURE_SLOTS);
                } else if (BlendPouchItem.isIngredient(stack)) {
                    tryMoveToSection(stack, MIXTURE_SLOTS, TOTAL_SLOTS);
                }

                if (stack.isEmpty()) inv.setItem(invSlot, ItemStack.EMPTY);
            }
        } finally {
            suppressSave = false;
            saveToSource();
        }
        broadcastChanges();
    }

    private void tryMoveToSection(ItemStack stack, int start, int end) {
        for (int i = start; i < end && !stack.isEmpty(); i++) {
            ItemStack existing = container.getItem(i);
            if (!existing.isEmpty() && ItemStack.isSameItemSameComponents(existing, stack)) {
                int space = existing.getMaxStackSize() - existing.getCount();
                int toMove = Math.min(space, stack.getCount());
                if (toMove > 0) {
                    existing.grow(toMove);
                    stack.shrink(toMove);
                    container.setItem(i, existing);
                }
            }
        }
        for (int i = start; i < end && !stack.isEmpty(); i++) {
            if (container.getItem(i).isEmpty()) {
                container.setItem(i, stack.copy());
                stack.setCount(0);
            }
        }
    }

    private void saveToSource() {
        if (suppressSave || player.level().isClientSide()) return;
        if (hand != null) {
            // Item mode: write back to DataComponent
            ItemStack stack = player.getItemInHand(hand);
            if (!(stack.getItem() instanceof BlendChestItem)) return;
            NonNullList<ItemStack> items = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);
            for (int i = 0; i < TOTAL_SLOTS; i++) items.set(i, container.getItem(i));
            BlendChestItem.setContents(stack, items);
            player.getInventory().setChanged();
        }
        // Block mode: changes go directly to be.container which calls be.setChanged() via listener
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        saveToSource();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (hand != null) {
            return player.getItemInHand(hand).getItem() instanceof BlendChestItem;
        }
        if (blockEntity != null) {
            BlockPos pos = blockEntity.getBlockPos();
            Level level = blockEntity.getLevel();
            return level != null
                && !blockEntity.isRemoved()
                && level.getBlockState(pos).is(SteamSmoke.BLEND_CHEST_BLOCK.get())
                && player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 64.0;
        }
        return true; // client side dummy
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack slotStack = slot.getItem();
        ItemStack result = slotStack.copy();

        if (index < TOTAL_SLOTS) {
            if (!moveItemStackTo(slotStack, TOTAL_SLOTS, TOTAL_SLOTS + 36, true))
                return ItemStack.EMPTY;
        } else {
            if (slotStack.is(ModItems.MIXTURE.get())) {
                if (!moveItemStackTo(slotStack, 0, MIXTURE_SLOTS, false))
                    return ItemStack.EMPTY;
            } else if (BlendPouchItem.isIngredient(slotStack)) {
                if (!moveItemStackTo(slotStack, MIXTURE_SLOTS, TOTAL_SLOTS, false))
                    return ItemStack.EMPTY;
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (slotStack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        return result;
    }
}
