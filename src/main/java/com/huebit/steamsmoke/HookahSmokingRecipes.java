package com.huebit.steamsmoke;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.ArrayList;
import java.util.List;

public class HookahSmokingRecipes {

    public static final int MAX_USES = 3;

    // Тиры длительности (тики; 20 = 1 секунда)
    private static final int D_BRIEF    =  600;  // 0:30 — побочные эффекты, вспышки
    private static final int D_POWERFUL = 1200;  // 1:00 — редкие/сильные
    private static final int D_STANDARD = 2400;  // 2:00 — скорость, регенерация, сила
    private static final int D_LONG     = 3600;  // 3:00 — утилита среднего уровня
    private static final int D_UTILITY  = 6000;  // 5:00 — ночное зрение, дыхание в воде

    // ── Публичный API ────────────────────────────────────────────────────────

    public static List<MobEffectInstance> getEffects(List<String> ingredients, HookahFluidType fluid) {
        List<MobEffectInstance> effects = new ArrayList<>();

        for (String ingredient : ingredients) {
            effects.addAll(getIngredientEffects(ingredient, fluid));
        }

        if (fluid == HookahFluidType.LAVA) {
            effects.add(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, D_STANDARD, 0));
        }

        effects.addAll(getSynergyEffects(ingredients, fluid));

        return effects;
    }

    // ── Эффекты ингредиентов ─────────────────────────────────────────────────

    private static List<MobEffectInstance> getIngredientEffects(String ingredient, HookahFluidType fluid) {
        int a = amp(fluid);
        return switch (ingredient) {

            // ── Restorative ──────────────────────────────────────────────────
            case "dried_ground_apple"               -> one(MobEffects.REGENERATION,      dur(D_STANDARD, fluid), a);
            case "dried_ground_melon"               -> two(
                    new MobEffectInstance(MobEffects.REGENERATION, dur(D_STANDARD, fluid), a),
                    new MobEffectInstance(MobEffects.ABSORPTION,   dur(D_STANDARD, fluid), 0));
            case "ground_golden_carrot"             -> two(
                    new MobEffectInstance(MobEffects.NIGHT_VISION, dur(D_UTILITY,  fluid), 0),
                    new MobEffectInstance(MobEffects.SATURATION,   dur(D_STANDARD, fluid), 0));
            case "ground_ghast_tear"                -> one(MobEffects.REGENERATION,      dur(D_POWERFUL, fluid), a + 2);
            case "dried_ground_leaves"              -> one(MobEffects.SATURATION,        dur(D_STANDARD, fluid), 0);
            case "ground_moss"                      -> one(MobEffects.REGENERATION,      dur(D_STANDARD, fluid), 0);
            case "ground_dandelion"                 -> one(MobEffects.REGENERATION,      dur(D_STANDARD, fluid), 0);
            case "dried_ground_glow_berries"        -> one(MobEffects.NIGHT_VISION,      dur(D_UTILITY,  fluid), 0);

            // ── Stimulant ────────────────────────────────────────────────────
            case "dried_ground_tobacco"             -> one(MobEffects.MOVEMENT_SPEED,    dur(D_STANDARD, fluid), a);
            case "ground_sugar"                     -> one(MobEffects.DIG_SPEED,         dur(D_STANDARD, fluid), a);
            case "dried_ground_bamboo"              -> one(MobEffects.DIG_SPEED,         dur(D_STANDARD, fluid), a + 1);
            case "ground_redstone"                  -> two(
                    new MobEffectInstance(MobEffects.MOVEMENT_SPEED, dur(D_STANDARD, fluid), a),
                    new MobEffectInstance(MobEffects.DIG_SPEED,      dur(D_STANDARD, fluid), a));
            case "ground_blaze_powder"              -> two(
                    new MobEffectInstance(MobEffects.DAMAGE_BOOST,    dur(D_POWERFUL, fluid), a),
                    new MobEffectInstance(MobEffects.FIRE_RESISTANCE, dur(D_POWERFUL, fluid), 0));

            // ── Nature ───────────────────────────────────────────────────────
            case "dried_ground_kelp"                -> two(
                    new MobEffectInstance(MobEffects.WATER_BREATHING, dur(D_UTILITY, fluid), 0),
                    new MobEffectInstance(MobEffects.DOLPHINS_GRACE,  dur(D_UTILITY, fluid), 0));
            case "dried_ground_nether_wart"         -> one(MobEffects.DAMAGE_BOOST,      dur(D_STANDARD, fluid), a);
            case "dried_ground_brown_mushroom"      -> two(
                    new MobEffectInstance(MobEffects.SATURATION, dur(D_STANDARD, fluid), 0),
                    new MobEffectInstance(MobEffects.ABSORPTION, dur(D_POWERFUL, fluid), 0));
            case "dried_ground_red_mushroom"        -> one(MobEffects.JUMP,              dur(D_STANDARD, fluid), a + 1);
            case "dried_ground_cactus"              -> one(MobEffects.DAMAGE_RESISTANCE, dur(D_STANDARD, fluid), a);
            case "ground_torchflower"               -> one(MobEffects.FIRE_RESISTANCE,   dur(D_LONG,     fluid), 0);
            case "ground_allium"                    -> one(MobEffects.DAMAGE_RESISTANCE, dur(D_POWERFUL, fluid), 0);

            // ── Mystic ───────────────────────────────────────────────────────
            case "dried_ground_chorus_fruit"        -> two(
                    new MobEffectInstance(MobEffects.SLOW_FALLING, dur(D_STANDARD, fluid), 0),
                    new MobEffectInstance(MobEffects.LEVITATION,   D_BRIEF / 4,             0));
            case "dried_ground_phantom_membrane"    -> two(
                    new MobEffectInstance(MobEffects.NIGHT_VISION, dur(D_UTILITY,  fluid), a),
                    new MobEffectInstance(MobEffects.SLOW_FALLING, dur(D_UTILITY,  fluid), 0));
            case "ground_ender_pearl"               -> two(
                    new MobEffectInstance(MobEffects.MOVEMENT_SPEED, dur(D_BRIEF, fluid), a + 2),
                    new MobEffectInstance(MobEffects.DIG_SPEED,      dur(D_BRIEF, fluid), a));
            case "ground_echo_shard"                -> two(
                    new MobEffectInstance(MobEffects.DARKNESS,    D_BRIEF / 2,             0),
                    new MobEffectInstance(MobEffects.NIGHT_VISION, dur(D_UTILITY, fluid),  0));
            case "ground_amethyst"                  -> two(
                    new MobEffectInstance(MobEffects.GLOWING,           dur(D_STANDARD, fluid), 0),
                    new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, dur(D_POWERFUL, fluid), 0));

            // ── Цветы ────────────────────────────────────────────────────────
            case "ground_honeycomb"                 -> one(MobEffects.SATURATION,        dur(D_STANDARD, fluid), 0);
            case "ground_sweet_berries"             -> one(MobEffects.NIGHT_VISION,      dur(D_LONG,     fluid), 0);
            case "ground_poppy"                     -> one(MobEffects.NIGHT_VISION,      dur(D_POWERFUL, fluid), 0);
            case "ground_lily_of_the_valley"        -> one(MobEffects.POISON,            dur(D_BRIEF,    fluid), 0);
            case "ground_spore_blossom"             -> two(
                    new MobEffectInstance(MobEffects.SATURATION,        dur(D_POWERFUL, fluid), 0),
                    new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, dur(D_POWERFUL, fluid), 0));
            case "ground_cornflower"                -> one(MobEffects.DIG_SPEED,         dur(D_POWERFUL, fluid), 0);
            case "ground_pink_petals"               -> one(MobEffects.REGENERATION,      dur(D_POWERFUL, fluid), 0);
            case "ground_sunflower"                 -> one(MobEffects.MOVEMENT_SPEED,    dur(D_POWERFUL, fluid), 0);
            case "ground_oxeye_daisy"               -> one(MobEffects.DAMAGE_RESISTANCE, dur(D_BRIEF,    fluid), 0);
            case "ground_peony"                     -> one(MobEffects.SATURATION,        dur(D_POWERFUL, fluid), 0);
            case "ground_lilac"                     -> one(MobEffects.NIGHT_VISION,      dur(D_POWERFUL, fluid), 0);
            case "ground_rose_bush"                 -> one(MobEffects.DAMAGE_BOOST,      dur(D_BRIEF,    fluid), 0);
            case "ground_azure_bluet"               -> one(MobEffects.ABSORPTION,        dur(D_BRIEF,    fluid), 0);
            case "ground_pink_tulip"                -> one(MobEffects.REGENERATION,      dur(D_BRIEF,    fluid), 0);
            case "ground_red_tulip"                 -> one(MobEffects.DAMAGE_BOOST,      dur(D_BRIEF,    fluid), 0);
            case "ground_orange_tulip"              -> one(MobEffects.MOVEMENT_SPEED,    dur(D_BRIEF,    fluid), 0);
            case "ground_white_tulip"               -> one(MobEffects.DAMAGE_RESISTANCE, dur(D_BRIEF,    fluid), 0);

            // ── Dangerous ────────────────────────────────────────────────────
            case "dried_ground_spider_eye"          -> two(
                    new MobEffectInstance(MobEffects.DAMAGE_BOOST, dur(D_STANDARD, fluid), a + 1),
                    new MobEffectInstance(MobEffects.POISON,       dur(D_BRIEF,    fluid), 0));
            case "dried_ground_pufferfish"          -> two(
                    new MobEffectInstance(MobEffects.DAMAGE_BOOST, dur(D_STANDARD, fluid), a + 1),
                    new MobEffectInstance(MobEffects.CONFUSION,    dur(D_BRIEF,    fluid), 0));
            case "dried_ground_magma_cream"         -> two(
                    new MobEffectInstance(MobEffects.FIRE_RESISTANCE, dur(D_STANDARD, fluid), a + 1),
                    new MobEffectInstance(MobEffects.DIG_SLOWDOWN,    dur(D_BRIEF,    fluid), 0));
            case "dried_ground_fermented_spider_eye"-> one(MobEffects.INVISIBILITY,     dur(D_STANDARD, fluid), 0);
            case "ground_wither_rose"               -> two(
                    new MobEffectInstance(MobEffects.DAMAGE_BOOST, dur(D_POWERFUL, fluid), a + 2),
                    new MobEffectInstance(MobEffects.WITHER,       dur(D_BRIEF,    fluid), 0));
            case "ground_nether_star"               -> two(
                    new MobEffectInstance(MobEffects.REGENERATION, dur(D_POWERFUL, fluid), a + 3),
                    new MobEffectInstance(MobEffects.BLINDNESS,    dur(D_BRIEF,    fluid), a + 1));

            // ── Прочее ───────────────────────────────────────────────────────
            case "dried_ground_crimson_fungus"      -> one(MobEffects.DAMAGE_BOOST,     dur(D_STANDARD, fluid), a);
            case "dried_ground_warped_fungus"       -> two(
                    new MobEffectInstance(MobEffects.MOVEMENT_SPEED, dur(D_STANDARD, fluid), a),
                    new MobEffectInstance(MobEffects.JUMP,           dur(D_STANDARD, fluid), 0));
            case "dried_ground_sea_pickle"          -> two(
                    new MobEffectInstance(MobEffects.NIGHT_VISION, dur(D_LONG,     fluid), 0),
                    new MobEffectInstance(MobEffects.GLOWING,      dur(D_STANDARD, fluid), 0));
            case "dried_ground_rabbit_hide"         -> two(
                    new MobEffectInstance(MobEffects.MOVEMENT_SPEED, dur(D_STANDARD, fluid), 0),
                    new MobEffectInstance(MobEffects.JUMP,           dur(D_STANDARD, fluid), 0));
            case "dried_ground_ink_sac"             -> two(
                    new MobEffectInstance(MobEffects.BLINDNESS,    D_BRIEF / 3,             0),
                    new MobEffectInstance(MobEffects.INVISIBILITY, dur(D_STANDARD, fluid),  0));
            case "dried_ground_slimeball"           -> two(
                    new MobEffectInstance(MobEffects.JUMP,              dur(D_STANDARD, fluid), a + 1),
                    new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, dur(D_POWERFUL, fluid), 0));
            case "ground_prismarine_shard"          -> one(MobEffects.WATER_BREATHING,  dur(D_LONG,     fluid), 0);
            case "ground_pumpkin"                   -> one(MobEffects.NIGHT_VISION,     dur(D_STANDARD, fluid), 0);
            case "ground_weeping_vines"             -> two(
                    new MobEffectInstance(MobEffects.WEAKNESS,          dur(D_POWERFUL, fluid), 0),
                    new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, dur(D_POWERFUL, fluid), 0));
            case "ground_coal"                      -> one(MobEffects.FIRE_RESISTANCE,  dur(D_STANDARD, fluid), 0);
            case "ground_carrot"                    -> one(MobEffects.NIGHT_VISION,     dur(D_POWERFUL, fluid), 0);
            case "ground_beetroot"                  -> one(MobEffects.REGENERATION,     dur(D_BRIEF,    fluid), 0);
            case "ground_potato"                    -> one(MobEffects.SATURATION,       dur(D_BRIEF,    fluid), 0);
            case "ground_cocoa_beans"               -> two(
                    new MobEffectInstance(MobEffects.DIG_SPEED,      dur(D_BRIEF, fluid), 0),
                    new MobEffectInstance(MobEffects.MOVEMENT_SPEED, dur(D_BRIEF, fluid), 0));

            // ── Сырые перемолотые ────────────────────────────────────────────
            case "wet_ground_apple"                 -> one(MobEffects.REGENERATION,     dur(D_POWERFUL, fluid), 0);

            default -> List.of();
        };
    }

    // ── Синергии ─────────────────────────────────────────────────────────────

    private static List<MobEffectInstance> getSynergyEffects(List<String> ingredients, HookahFluidType fluid) {
        List<MobEffectInstance> bonuses = new ArrayList<>();

        // Табак + яблоко: классический бленд — бонусный Absorption
        if (has(ingredients, "dried_ground_tobacco", "dried_ground_apple")) {
            bonuses.add(new MobEffectInstance(MobEffects.ABSORPTION, dur(D_STANDARD, fluid), 0));
        }

        // Бурый + красный гриб: полная микология — Jump Boost III
        if (has(ingredients, "dried_ground_brown_mushroom", "dried_ground_red_mushroom")) {
            bonuses.add(new MobEffectInstance(MobEffects.JUMP, dur(D_STANDARD, fluid), 2));
        }

        return bonuses;
    }

    // ── Хелперы ──────────────────────────────────────────────────────────────

    /** Молоко увеличивает длительность ×1.5; лава и вода — без изменений. */
    private static int dur(int base, HookahFluidType fluid) {
        return fluid == HookahFluidType.MILK ? base * 3 / 2 : base;
    }

    /** Лава даёт +1 уровень ко всем эффектам ингредиента. */
    private static int amp(HookahFluidType fluid) {
        return fluid == HookahFluidType.LAVA ? 1 : 0;
    }

    private static boolean has(List<String> ingredients, String... required) {
        for (String r : required) {
            if (!ingredients.contains(r)) return false;
        }
        return true;
    }

    private static List<MobEffectInstance> one(Holder<MobEffect> effect, int duration, int amplifier) {
        return List.of(new MobEffectInstance(effect, duration, amplifier));
    }

    private static List<MobEffectInstance> two(MobEffectInstance a, MobEffectInstance b) {
        return List.of(a, b);
    }
}
