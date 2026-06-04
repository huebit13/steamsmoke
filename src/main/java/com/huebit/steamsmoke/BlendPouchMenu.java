package com.huebit.steamsmoke;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BlendPouchMenu extends AbstractContainerMenu {

    private static final int TOTAL_SLOTS = BlendPouchItem.TOTAL_SLOTS;
    private static final int MIXTURE_SLOTS = BlendPouchItem.MIXTURE_SLOTS;

    final SimpleContainer container;
    private final Player player;
    private final InteractionHand hand;
    private boolean suppressSave = false;

    public BlendPouchMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, buf.readEnum(InteractionHand.class));
    }

    public BlendPouchMenu(int id, Inventory playerInv, InteractionHand hand) {
        super(ModMenuTypes.BLEND_POUCH.get(), id);
        this.hand = hand;
        this.player = playerInv.player;
        this.container = new SimpleContainer(TOTAL_SLOTS);

        if (!player.level().isClientSide()) {
            ItemStack pouch = player.getItemInHand(hand);
            NonNullList<ItemStack> items = BlendPouchItem.getContents(pouch);
            for (int i = 0; i < TOTAL_SLOTS; i++) container.setItem(i, items.get(i));
        }

        // Mixture slots (0-17): 2 rows × 9
        for (int i = 0; i < MIXTURE_SLOTS; i++) {
            int row = i / 9, col = i % 9;
            addSlot(new Slot(container, i, 8 + col * 18, 24 + row * 18) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.is(ModItems.MIXTURE.get());
                }
            });
        }

        // Ingredient slots (18-53): 4 rows × 9
        for (int i = 0; i < BlendPouchItem.INGREDIENT_SLOTS; i++) {
            int row = i / 9, col = i % 9;
            addSlot(new Slot(container, MIXTURE_SLOTS + i, 8 + col * 18, 72 + row * 18) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return BlendPouchItem.isIngredient(stack);
                }
            });
        }

        // Player inventory (slots 54-80)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 176 + row * 18));
            }
        }
        // Hotbar (slots 81-89)
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInv, col, 8 + col * 18, 234));
        }

        container.addListener(c -> {
            if (!player.level().isClientSide()) saveToItem();
        });
    }

    void collectFromInventory() {
        suppressSave = true;
        try {
            net.minecraft.world.entity.player.Inventory inv = player.getInventory();
            for (int invSlot = 0; invSlot < 36; invSlot++) {
                ItemStack stack = inv.getItem(invSlot);
                if (stack.isEmpty() || stack.is(ModItems.BLEND_POUCH.get())) continue;

                if (stack.is(ModItems.MIXTURE.get())) {
                    tryMoveToSection(stack, 0, MIXTURE_SLOTS);
                } else if (BlendPouchItem.isIngredient(stack)) {
                    tryMoveToSection(stack, MIXTURE_SLOTS, TOTAL_SLOTS);
                }

                if (stack.isEmpty()) inv.setItem(invSlot, ItemStack.EMPTY);
            }
        } finally {
            suppressSave = false;
            saveToItem();
        }
        broadcastChanges();
    }

    private void tryMoveToSection(ItemStack stack, int start, int end) {
        // Pass 1: merge with existing stacks
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
        // Pass 2: fill empty slots
        for (int i = start; i < end && !stack.isEmpty(); i++) {
            if (container.getItem(i).isEmpty()) {
                container.setItem(i, stack.copy());
                stack.setCount(0);
            }
        }
    }

    private void saveToItem() {
        if (suppressSave) return;
        ItemStack pouch = player.getItemInHand(hand);
        if (!(pouch.getItem() instanceof BlendPouchItem)) return;

        NonNullList<ItemStack> items = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);
        for (int i = 0; i < TOTAL_SLOTS; i++) items.set(i, container.getItem(i));
        BlendPouchItem.setContents(pouch, items);
        player.getInventory().setChanged();
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        if (!player.level().isClientSide()) saveToItem();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return player.getItemInHand(hand).getItem() instanceof BlendPouchItem;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack slotStack = slot.getItem();
        ItemStack result = slotStack.copy();

        if (index < TOTAL_SLOTS) {
            // Pouch → player inventory
            if (!moveItemStackTo(slotStack, TOTAL_SLOTS, TOTAL_SLOTS + 36, true))
                return ItemStack.EMPTY;
        } else {
            // Player inventory → pouch
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
