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

public class HookahBlockEntity extends BlockEntity {

    private HookahFluidType fluidType = HookahFluidType.EMPTY;

    // Слот для mixture (один)
    private ItemStack mixtureStack = ItemStack.EMPTY;
    // Сколько раз уже использована текущая mixture
    private int useCount = 0;

    public HookahBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HOOKAH_BE.get(), pos, state);
    }

    // ── Fluid ─────────────────────────────────────────────────────────────

    public HookahFluidType getFluidType() { return fluidType; }

    public void setFluidType(HookahFluidType type) {
        this.fluidType = type;
        syncToClients();
    }

    // ── Mixture ───────────────────────────────────────────────────────────

    public ItemStack getMixtureStack() { return mixtureStack; }

    public boolean hasMixture() { return !mixtureStack.isEmpty(); }

    public void setMixture(ItemStack stack) {
        this.mixtureStack = stack.copyWithCount(1);
        this.useCount = 0;
        syncToClients();
    }

    public void removeMixture() {
        this.mixtureStack = ItemStack.EMPTY;
        this.useCount = 0;
        syncToClients();
    }

    /**
     * Записывает одно использование. Возвращает true если mixture ещё жива,
     * false если сгорела (useCount достиг MAX_USES).
     */
    public boolean consumeUse() {
        if (mixtureStack.isEmpty()) return false;

        int damage = mixtureStack.getDamageValue() + 1;
        if (damage >= mixtureStack.getMaxDamage()) {
            mixtureStack = ItemStack.EMPTY;
            syncToClients();
            return false;
        }
        mixtureStack.setDamageValue(damage);
        syncToClients();
        return true;
    }

    public int getUseCount() { return useCount; }

    // ── Sync ──────────────────────────────────────────────────────────────

    public void syncToClients() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    // ── NBT ───────────────────────────────────────────────────────────────

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString("FluidType", fluidType.name());
        if (!mixtureStack.isEmpty()) {
            CompoundTag mixtureTag = new CompoundTag();
            mixtureStack.save(registries, mixtureTag);
            tag.put("Mixture", mixtureTag);
        }
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

    // ── Packets ───────────────────────────────────────────────────────────

    @Override
    public @NotNull ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putString("FluidType", fluidType.name());
        if (!mixtureStack.isEmpty()) {
            CompoundTag mixtureTag = new CompoundTag();
            mixtureStack.save(registries, mixtureTag);
            tag.put("Mixture", mixtureTag);
        }
        return tag;
    }
}