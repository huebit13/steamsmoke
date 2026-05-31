package com.huebit.steamsmoke;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SteamSmoke.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HookahBlockEntity>> HOOKAH_BE =
            BLOCK_ENTITIES.register("hookah_be", () ->
                    BlockEntityType.Builder.of(HookahBlockEntity::new, SteamSmoke.HOOKAH.get()).build(null));

    // ИСПРАВЛЕНО: Добавлен тип BlockEntity для ступки
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MortarBlockEntity>> MORTAR_BE =
            BLOCK_ENTITIES.register("mortar_be", () ->
                    BlockEntityType.Builder.of(MortarBlockEntity::new, SteamSmoke.MORTAR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DryingRackBlockEntity>> DRYING_RACK_BE =
            BLOCK_ENTITIES.register("drying_rack_be", () ->
                    BlockEntityType.Builder.of(DryingRackBlockEntity::new, SteamSmoke.DRYING_RACK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}