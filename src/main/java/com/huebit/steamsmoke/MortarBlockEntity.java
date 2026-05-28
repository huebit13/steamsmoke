package com.huebit.steamsmoke;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MortarBlockEntity extends BlockEntity {

    public static final int MAX_SLOTS = 4;

    private final NonNullList<ItemStack> items = NonNullList.withSize(MAX_SLOTS, ItemStack.EMPTY);

    public MortarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MORTAR_BE.get(), pos, state);
    }

    public int getItemCount() {
        int count = 0;
        for (ItemStack s : items) if (!s.isEmpty()) count++;
        return count;
    }

    public boolean isFull() {
        return getItemCount() >= MAX_SLOTS;
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public boolean addItem(ItemStack stack) {
        if (isFull()) return false;
        for (int i = 0; i < MAX_SLOTS; i++) {
            if (items.get(i).isEmpty()) {
                items.set(i, stack.copyWithCount(1));
                sync();
                return true;
            }
        }
        return false;
    }

    public List<ItemStack> removeAll() {
        List<ItemStack> result = new ArrayList<>();
        for (int i = 0; i < MAX_SLOTS; i++) {
            if (!items.get(i).isEmpty()) {
                result.add(items.get(i).copy());
                items.set(i, ItemStack.EMPTY);
            }
        }
        sync();
        return result;
    }

    public void replaceWith(ItemStack stack) {
        for (int i = 0; i < MAX_SLOTS; i++) items.set(i, ItemStack.EMPTY);
        items.set(0, stack);
        sync();
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public List<ItemStack> getNonEmptyItems() {
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack s : items) if (!s.isEmpty()) result.add(s);
        return result;
    }

    public void sync() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        // Сначала очищаем все слоты, потом загружаем из тега
        for (int i = 0; i < MAX_SLOTS; i++) items.set(i, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);
    }

    @Override
    public @NotNull ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        ContainerHelper.saveAllItems(tag, items, registries);
        return tag;
    }
}