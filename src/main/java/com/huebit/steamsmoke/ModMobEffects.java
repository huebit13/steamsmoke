package com.huebit.steamsmoke;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMobEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, SteamSmoke.MODID);

    /** При получении удара в ближнем бою — атакующий получает Яд II на 3 сек. */
    public static final DeferredHolder<MobEffect, MobEffect> SPINE_AURA =
            MOB_EFFECTS.register("spine_aura",
                    () -> new MobEffect(MobEffectCategory.BENEFICIAL, 0x4CAF50) {});

    /** При ходьбе оставляешь горящие блоки под ногами. */
    public static final DeferredHolder<MobEffect, MobEffect> FIRE_TRAIL =
            MOB_EFFECTS.register("fire_trail",
                    () -> new MobEffect(MobEffectCategory.BENEFICIAL, 0xFF6B35) {});

    /** Мобы в радиусе 20 блоков подсвечиваются сквозь стены. */
    public static final DeferredHolder<MobEffect, MobEffect> SOUL_SIGHT =
            MOB_EFFECTS.register("soul_sight",
                    () -> new MobEffect(MobEffectCategory.BENEFICIAL, 0xAA77FF) {});

    /** Пиглины не атакуют, пока эффект активен. */
    public static final DeferredHolder<MobEffect, MobEffect> GOLD_PACT =
            MOB_EFFECTS.register("gold_pact",
                    () -> new MobEffect(MobEffectCategory.BENEFICIAL, 0xFFD700) {});

    /** Отражает 25% входящего урона ближнего боя обратно атакующему. */
    public static final DeferredHolder<MobEffect, MobEffect> IRON_SKIN =
            MOB_EFFECTS.register("iron_skin",
                    () -> new MobEffect(MobEffectCategory.BENEFICIAL, 0xB0C4DE) {});

    public static void register(IEventBus bus) {
        MOB_EFFECTS.register(bus);
    }
}
