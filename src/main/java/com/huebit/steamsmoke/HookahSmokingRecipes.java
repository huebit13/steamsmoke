package com.huebit.steamsmoke;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.ArrayList;
import java.util.List;

public class HookahSmokingRecipes {

    public static final int BASE_DURATION_TICKS = 200; // 10 секунд
    public static final int MAX_USES = 3;

    /**
     * Возвращает список эффектов на основе ингредиентов mixture и жидкости в кальяне.
     */
    public static List<MobEffectInstance> getEffects(List<String> ingredients, HookahFluidType fluid) {
        List<MobEffectInstance> effects = new ArrayList<>();

        int duration = getBaseDuration(fluid);

        for (String ingredient : ingredients) {
            MobEffectInstance effect = getIngredientEffect(ingredient, duration, fluid);
            if (effect != null) effects.add(effect);
        }

        // Лава даёт Fire Resistance всегда
        if (fluid == HookahFluidType.LAVA) {
            effects.add(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, duration, 0));
        }

        return effects;
    }

    private static int getBaseDuration(HookahFluidType fluid) {
        return switch (fluid) {
            case MILK  -> BASE_DURATION_TICKS * 2; // 20 сек
            case LAVA  -> BASE_DURATION_TICKS;     // 10 сек, но эффект усилен
            default    -> BASE_DURATION_TICKS;     // вода и пусто — 10 сек
        };
    }

    private static MobEffectInstance getIngredientEffect(String ingredient, int duration, HookahFluidType fluid) {
        // Лава усиливает эффект на 1 уровень
        int amplifier = (fluid == HookahFluidType.LAVA) ? 1 : 0;

        return switch (ingredient) {
            case "ground_tobacco"       -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED,    duration, amplifier);
            case "ground_apple"         -> new MobEffectInstance(MobEffects.REGENERATION,      duration, amplifier);
            case "ground_honeycomb"     -> new MobEffectInstance(MobEffects.SATURATION,        duration, amplifier);
            case "ground_sugar"         -> new MobEffectInstance(MobEffects.DIG_SPEED,         duration, amplifier);
            case "ground_sweet_berries" -> new MobEffectInstance(MobEffects.NIGHT_VISION,      duration, amplifier);
            default -> null;
        };
    }
}