package com.huebit.steamsmoke;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PortableDiffuserMenu extends AbstractContainerMenu {

    static final int MIXTURE_SLOT_X = 80;
    static final int MIXTURE_SLOT_Y = 28;

    final SimpleContainer container;
    private final Player player;
    private final InteractionHand hand;

    /** Client constructor — reads hand from network buffer; slots synced by server. */
    public PortableDiffuserMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv, buf.readEnum(InteractionHand.class));
    }

    /** Server constructor. */
    public PortableDiffuserMenu(int id, Inventory playerInv, InteractionHand hand) {
        super(ModMenuTypes.PORTABLE_DIFFUSER.get(), id);
        this.hand = hand;
        this.player = playerInv.player;
        this.container = new SimpleContainer(1);

        if (!player.level().isClientSide()) {
            ItemStack diffuser = player.getItemInHand(hand);
            ItemStack mixture = PortableDiffuserItem.getMixture(diffuser);
            container.setItem(0, mixture);
        }

        addSlot(new Slot(container, 0, MIXTURE_SLOT_X, MIXTURE_SLOT_Y) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ModItems.MIXTURE.get());
            }
        });

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }

        container.addListener(c -> {
            if (!player.level().isClientSide()) saveToItem();
        });
    }

    private void saveToItem() {
        ItemStack diffuser = player.getItemInHand(hand);
        if (!(diffuser.getItem() instanceof PortableDiffuserItem)) return;

        ItemStack mixture = container.getItem(0);

        int currentVariant = PortableDiffuserItem.getFluidVariant(diffuser);
        if (mixture.isEmpty() && currentVariant != 0) {
            PortableDiffuserItem.setFluidVariant(diffuser, 0);
            // Звук извлечения смеси из колбы
            player.level().playSound(null, player.blockPosition(),
                    SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.6f, 1.2f);
        } else if (!mixture.isEmpty() && currentVariant == 0) {
            int newVariant = 1 + player.level().random.nextInt(3); // 1=gold 2=mint 3=purple
            PortableDiffuserItem.setFluidVariant(diffuser, newVariant);
            // Звук загрузки смеси — низкая нота, как у кальяна
            player.level().playSound(null, player.blockPosition(),
                    SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.8f, 0.6f);
        }

        PortableDiffuserItem.setMixture(diffuser, mixture); // no registry needed
        player.getInventory().setChanged();
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        if (!player.level().isClientSide()) saveToItem();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return player.getItemInHand(hand).getItem() instanceof PortableDiffuserItem;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack slotStack = slot.getItem();
        ItemStack result = slotStack.copy();

        if (index == 0) {
            if (!moveItemStackTo(slotStack, 1, 37, true)) return ItemStack.EMPTY;
        } else {
            if (!slotStack.is(ModItems.MIXTURE.get())) return ItemStack.EMPTY;
            if (!moveItemStackTo(slotStack, 0, 1, false)) return ItemStack.EMPTY;
        }

        if (slotStack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        return result;
    }
}
