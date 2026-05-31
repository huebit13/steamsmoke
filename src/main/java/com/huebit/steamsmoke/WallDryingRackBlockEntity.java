package com.huebit.steamsmoke;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class WallDryingRackBlockEntity extends DryingRackBlockEntity {

    public static final int MAX_SLOTS = 3;

    public WallDryingRackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WALL_DRYING_RACK_BE.get(), pos, state);
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < MAX_SLOTS; i++) if (!getItems().get(i).isEmpty()) return false;
        return true;
    }

    @Override
    public boolean isFull() {
        for (int i = 0; i < MAX_SLOTS; i++) if (getItems().get(i).isEmpty()) return false;
        return true;
    }

    @Override
    public boolean addItem(ItemStack stack) {
        for (int i = 0; i < MAX_SLOTS; i++) {
            if (getItems().get(i).isEmpty()) {
                getItems().set(i, stack.copyWithCount(1));
                sync();
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack takeItem() {
        for (int i = MAX_SLOTS - 1; i >= 0; i--) {
            if (!getItems().get(i).isEmpty()) {
                ItemStack taken = getItems().get(i).copy();
                getItems().set(i, ItemStack.EMPTY);
                dryingProgress[i] = 0;
                dryingTotal[i]    = 0;
                sync();
                return taken;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public List<ItemStack> getNonEmptyItems() {
        List<ItemStack> result = new ArrayList<>();
        for (int i = 0; i < MAX_SLOTS; i++) {
            ItemStack s = getItems().get(i);
            if (!s.isEmpty()) result.add(s.copy());
        }
        return result;
    }
}
