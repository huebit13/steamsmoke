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
                    BlockEntityType.Builder.of(HookahBlockEntity::new, SteamSmoke.HOOKAH.get()).build(null)); //[cite: 4, 5]

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus); //[cite: 4]
    }
}