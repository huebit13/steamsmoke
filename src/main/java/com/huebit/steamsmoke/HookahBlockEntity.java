package com.huebit.steamsmoke;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class HookahBlockEntity extends BlockEntity {

    private HookahFluidType fluidType = HookahFluidType.EMPTY;

    public HookahBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HOOKAH_BE.get(), pos, state);
    }

    public HookahFluidType getFluidType() {
        return fluidType;
    }

    public void setFluidType(HookahFluidType type) {
        this.fluidType = type;
        syncToClients();
    }

    /**
     * Вызывать после любого изменения — синхронизация с клиентом.
     */
    public void syncToClients() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    // -------------------------------------------------------------------------
    // Сериализация
    // -------------------------------------------------------------------------

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString("FluidType", fluidType.name());
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("FluidType")) {
            try {
                fluidType = HookahFluidType.valueOf(tag.getString("FluidType"));
            } catch (IllegalArgumentException e) {
                fluidType = HookahFluidType.EMPTY;
            }
        }
    }

    // -------------------------------------------------------------------------
    // Синхронизация клиент ↔ сервер
    // -------------------------------------------------------------------------

    @Override
    public @NotNull ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putString("FluidType", fluidType.name());
        return tag;
    }
}