package com.huebit.steamsmoke;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, SteamSmoke.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<PortableDiffuserMenu>> PORTABLE_DIFFUSER =
            MENU_TYPES.register("portable_diffuser",
                    () -> IMenuTypeExtension.create(PortableDiffuserMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<BlendPouchMenu>> BLEND_POUCH =
            MENU_TYPES.register("blend_pouch",
                    () -> IMenuTypeExtension.create(BlendPouchMenu::new));

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
