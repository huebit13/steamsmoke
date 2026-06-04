package com.huebit.steamsmoke;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BlendChestBlockEntity extends BlockEntity {

    public final SimpleContainer container = new SimpleContainer(BlendChestItem.TOTAL_SLOTS);

    public BlendChestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLEND_CHEST_BE.get(), pos, state);
        container.addListener(c -> setChanged());
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        NonNullList<ItemStack> items = NonNullList.withSize(BlendChestItem.TOTAL_SLOTS, ItemStack.EMPTY);
        for (int i = 0; i < BlendChestItem.TOTAL_SLOTS; i++) items.set(i, container.getItem(i));
        ContainerHelper.saveAllItems(tag, items, registries);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        NonNullList<ItemStack> items = NonNullList.withSize(BlendChestItem.TOTAL_SLOTS, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);
        for (int i = 0; i < BlendChestItem.TOTAL_SLOTS; i++) container.setItem(i, items.get(i));
    }
}
