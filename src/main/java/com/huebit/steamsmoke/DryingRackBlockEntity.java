package com.huebit.steamsmoke;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DryingRackBlockEntity extends BlockEntity {

    public static final int MAX_SLOTS = 6;

    private final NonNullList<ItemStack> items = NonNullList.withSize(MAX_SLOTS, ItemStack.EMPTY);
    protected final int[] dryingProgress = new int[MAX_SLOTS];
    protected final int[] dryingTotal    = new int[MAX_SLOTS];

    public DryingRackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DRYING_RACK_BE.get(), pos, state);
    }

    protected DryingRackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    // ── Server tick ──────────────────────────────────────────────────────────

    public static void serverTick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull DryingRackBlockEntity entity) {
        boolean dirty = false;
        for (int i = 0; i < MAX_SLOTS; i++) {
            ItemStack stack = entity.items.get(i);
            if (stack.isEmpty()) {
                entity.dryingProgress[i] = 0;
                entity.dryingTotal[i]    = 0;
                continue;
            }
            if (entity.dryingTotal[i] == 0) {
                DryingRecipes.DryingRecipe recipe = DryingRecipes.getRecipe(stack);
                if (recipe == null) continue;
                entity.dryingTotal[i] = recipe.durationTicks();
            }
            entity.dryingProgress[i]++;
            if (entity.dryingProgress[i] >= entity.dryingTotal[i]) {
                DryingRecipes.DryingRecipe recipe = DryingRecipes.getRecipe(entity.items.get(i));
                entity.items.set(i, recipe != null ? recipe.createResult() : ItemStack.EMPTY);
                entity.dryingProgress[i] = 0;
                entity.dryingTotal[i]    = 0;
                dirty = true;
            }
        }
        if (dirty) entity.sync();
    }

    // ── Progress getters ─────────────────────────────────────────────────────

    public int getDryingProgress(int slot) { return dryingProgress[slot]; }
    public int getDryingTotal(int slot)    { return dryingTotal[slot]; }

    // ── Inventory ────────────────────────────────────────────────────────────

    public boolean isEmpty() {
        for (ItemStack s : items) if (!s.isEmpty()) return false;
        return true;
    }

    public boolean isFull() {
        for (ItemStack s : items) if (s.isEmpty()) return false;
        return true;
    }

    public boolean addItem(ItemStack stack) {
        for (int i = 0; i < MAX_SLOTS; i++) {
            if (items.get(i).isEmpty()) {
                items.set(i, stack.copyWithCount(1));
                sync();
                return true;
            }
        }
        return false;
    }

    public ItemStack takeItem() {
        for (int i = MAX_SLOTS - 1; i >= 0; i--) {
            if (!items.get(i).isEmpty()) {
                ItemStack taken = items.get(i).copy();
                items.set(i, ItemStack.EMPTY);
                dryingProgress[i] = 0;
                dryingTotal[i]    = 0;
                sync();
                return taken;
            }
        }
        return ItemStack.EMPTY;
    }

    public List<ItemStack> getNonEmptyItems() {
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack s : items) if (!s.isEmpty()) result.add(s.copy());
        return result;
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    // ── Sync & NBT ───────────────────────────────────────────────────────────

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
        tag.putIntArray("DryingProgress", dryingProgress);
        tag.putIntArray("DryingTotal",    dryingTotal);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        for (int i = 0; i < MAX_SLOTS; i++) items.set(i, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);
        int[] savedProgress = tag.getIntArray("DryingProgress");
        int[] savedTotal    = tag.getIntArray("DryingTotal");
        for (int i = 0; i < MAX_SLOTS; i++) {
            dryingProgress[i] = i < savedProgress.length ? savedProgress[i] : 0;
            dryingTotal[i]    = i < savedTotal.length    ? savedTotal[i]    : 0;
        }
    }

    @Override
    public @NotNull ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        ContainerHelper.saveAllItems(tag, items, registries);
        tag.putIntArray("DryingProgress", dryingProgress);
        tag.putIntArray("DryingTotal",    dryingTotal);
        return tag;
    }
}
