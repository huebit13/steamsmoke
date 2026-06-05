package com.huebit.steamsmoke;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.ArrayList;
import java.util.List;

public class HookahSmokingRecipes {

    public static final int MAX_USES = 3;

    // ── Публичный тип эффектов ────────────────────────────────────────────────

    public record IngredientEffects(
            List<MobEffectInstance> positive,
            List<MobEffectInstance> negative,
            List<MobEffectInstance> crash   // зарезервировано, пока пусто
    ) {
        public static final IngredientEffects EMPTY =
                new IngredientEffects(List.of(), List.of(), List.of());

        public boolean isEmpty() {
            return positive.isEmpty() && negative.isEmpty();
        }

        /** Все эффекты: позитивные + негативные (crash не применяется) */
        public List<MobEffectInstance> all() {
            if (negative.isEmpty()) return positive;
            if (positive.isEmpty()) return negative;
            List<MobEffectInstance> result = new ArrayList<>(positive);
            result.addAll(negative);
            return result;
        }
    }

    public record SynergyInfo(String name, List<String> required, List<MobEffectInstance> bonus) {}

    // ── Тиры длительности (тики; 20 = 1 секунда) ─────────────────────────────
    private static final int D_BRIEF    =  600;  // 0:30
    private static final int D_POWERFUL = 1200;  // 1:00
    private static final int D_STANDARD = 2400;  // 2:00
    private static final int D_LONG     = 3600;  // 3:00
    private static final int D_UTILITY  = 6000;  // 5:00

    // ── Публичный API ─────────────────────────────────────────────────────────

    public static IngredientEffects getBaseEffects(String ingredient) {
        return getIngredientEffects(ingredient, HookahFluidType.WATER);
    }

    public static List<SynergyInfo> getActiveSynergies(List<String> ingredients, HookahFluidType fluid) {
        List<SynergyInfo> result = new ArrayList<>();

        if (has(ingredients, "dried_ground_tobacco", "dried_ground_apple")) {
            result.add(new SynergyInfo("Классический бленд",
                    List.of("dried_ground_tobacco", "dried_ground_apple"),
                    List.of(inst(MobEffects.ABSORPTION, dur(D_STANDARD, fluid), 0))));
        }
        if (has(ingredients, "dried_ground_brown_mushroom", "dried_ground_red_mushroom")) {
            result.add(new SynergyInfo("Полная микология",
                    List.of("dried_ground_brown_mushroom", "dried_ground_red_mushroom"),
                    List.of(inst(MobEffects.JUMP, dur(D_STANDARD, fluid), 2))));
        }

        return result;
    }

    public static IngredientEffects getEffects(List<String> ingredients, HookahFluidType fluid) {
        List<MobEffectInstance> pos = new ArrayList<>();
        List<MobEffectInstance> neg = new ArrayList<>();

        for (String ingredient : ingredients) {
            IngredientEffects ie = getIngredientEffects(ingredient, fluid);
            pos.addAll(ie.positive());
            neg.addAll(ie.negative());
        }

        if (fluid == HookahFluidType.LAVA) {
            pos.add(inst(MobEffects.FIRE_RESISTANCE, D_STANDARD, 0));
        }

        for (SynergyInfo syn : getActiveSynergies(ingredients, fluid)) {
            pos.addAll(syn.bonus());
        }

        return new IngredientEffects(pos, neg, List.of());
    }

    // ── Эффекты ингредиентов ──────────────────────────────────────────────────

    private static IngredientEffects getIngredientEffects(String ingredient, HookahFluidType fluid) {
        int a = amp(fluid);
        return switch (ingredient) {

            // ── Dried Ground ─────────────────────────────────────────────────

            case "dried_ground_tobacco" -> pos(
                    inst(MobEffects.MOVEMENT_SPEED,  dur(D_STANDARD, fluid), a + 1),
                    inst(MobEffects.NIGHT_VISION,    dur(D_BRIEF,    fluid), 0)
            );
            case "dried_ground_apple" -> pos(
                    inst(MobEffects.REGENERATION,    dur(D_STANDARD, fluid), a),
                    inst(MobEffects.SATURATION,      dur(D_STANDARD, fluid), 0)
            );
            case "dried_ground_brown_mushroom" -> pos(
                    inst(MobEffects.SATURATION,      dur(D_STANDARD, fluid), 0),
                    inst(MobEffects.SLOW_FALLING,    dur(D_STANDARD, fluid), 0)
            );
            case "dried_ground_red_mushroom" -> pn(
                    list(inst(MobEffects.JUMP,       dur(D_STANDARD, fluid), a + 2)),
                    list(inst(MobEffects.CONFUSION,  dur(D_BRIEF,    fluid), 0))
            );
            case "dried_ground_kelp" -> pos(
                    inst(MobEffects.WATER_BREATHING, dur(D_UTILITY,  fluid), 0),
                    inst(MobEffects.DOLPHINS_GRACE,  dur(D_UTILITY,  fluid), 0),
                    inst(MobEffects.NIGHT_VISION,    dur(D_UTILITY,  fluid), 0)
            );
            case "dried_ground_melon" -> pos(
                    inst(MobEffects.REGENERATION,    dur(D_STANDARD, fluid), a),
                    inst(MobEffects.ABSORPTION,      dur(D_STANDARD, fluid), 0)
            );
            case "dried_ground_cactus" -> pos(
                    inst(MobEffects.DAMAGE_RESISTANCE, dur(D_STANDARD, fluid), a),
                    inst(ModMobEffects.SPINE_AURA,     dur(D_STANDARD, fluid), 0)
            );
            case "dried_ground_nether_wart" -> pos(
                    inst(MobEffects.DAMAGE_BOOST,    dur(D_STANDARD, fluid), a),
                    inst(MobEffects.FIRE_RESISTANCE, dur(D_STANDARD, fluid), 0)
            );
            case "dried_ground_crimson_fungus" -> pos(
                    inst(MobEffects.DAMAGE_BOOST,    dur(D_STANDARD, fluid), a + 1),
                    inst(MobEffects.FIRE_RESISTANCE, dur(D_BRIEF,    fluid), 0)
            );
            case "dried_ground_warped_fungus" -> pos(
                    inst(MobEffects.MOVEMENT_SPEED,  dur(D_STANDARD, fluid), a + 1),
                    inst(MobEffects.JUMP,            dur(D_STANDARD, fluid), 0)
            );
            case "dried_ground_chorus_fruit" -> pos(
                    inst(MobEffects.SLOW_FALLING,    dur(D_STANDARD, fluid), 0),
                    inst(MobEffects.LEVITATION,      7,                       0),
                    inst(MobEffects.MOVEMENT_SPEED,  dur(D_STANDARD, fluid), a)
            );
            case "dried_ground_glow_berries" -> pos(
                    inst(MobEffects.NIGHT_VISION,    dur(D_UTILITY,  fluid), 0),
                    inst(MobEffects.GLOWING,         dur(D_STANDARD, fluid), 0)
            );
            case "dried_ground_phantom_membrane" -> pos(
                    inst(MobEffects.NIGHT_VISION,    dur(D_UTILITY,  fluid), 0),
                    inst(MobEffects.SLOW_FALLING,    dur(D_UTILITY,  fluid), 0)
            );
            case "dried_ground_rabbit_hide" -> pos(
                    inst(MobEffects.MOVEMENT_SPEED,  dur(D_STANDARD, fluid), a),
                    inst(MobEffects.JUMP,            dur(D_STANDARD, fluid), 1)
            );
            case "dried_ground_ink_sac" -> pn(
                    list(inst(MobEffects.INVISIBILITY, dur(D_STANDARD, fluid), 0)),
                    list(inst(MobEffects.BLINDNESS,    200,                     0))
            );
            case "dried_ground_slimeball" -> pn(
                    list(inst(MobEffects.JUMP,              dur(D_STANDARD, fluid), a + 1)),
                    list(inst(MobEffects.MOVEMENT_SLOWDOWN, dur(D_POWERFUL, fluid), 0))
            );
            case "dried_ground_sea_pickle" -> pos(
                    inst(MobEffects.NIGHT_VISION,    dur(D_UTILITY,  fluid), 0),
                    inst(MobEffects.GLOWING,         dur(D_STANDARD, fluid), 0),
                    inst(MobEffects.WATER_BREATHING, dur(D_LONG,     fluid), 0)
            );
            case "dried_ground_magma_cream" -> pos(
                    inst(MobEffects.FIRE_RESISTANCE, dur(D_STANDARD, fluid), 0),
                    inst(MobEffects.DAMAGE_BOOST,    dur(D_BRIEF,    fluid), a)
            );
            case "dried_ground_spider_eye" -> pn(
                    list(inst(MobEffects.DAMAGE_BOOST, dur(D_STANDARD, fluid), a + 1)),
                    list(inst(MobEffects.POISON,       dur(D_BRIEF,    fluid), 0))
            );
            case "dried_ground_fermented_spider_eye" -> pos(
                    inst(MobEffects.INVISIBILITY,    dur(D_STANDARD, fluid), 0)
            );
            case "dried_ground_bamboo" -> pos(
                    inst(MobEffects.DIG_SPEED,       dur(D_STANDARD, fluid), a + 1)
            );
            case "dried_ground_pufferfish" -> pn(
                    list(inst(MobEffects.DAMAGE_BOOST, dur(D_STANDARD, fluid), a + 1)),
                    list(inst(MobEffects.CONFUSION,    dur(D_BRIEF,    fluid), 0))
            );
            case "dried_ground_leaves" -> pos(
                    inst(MobEffects.SATURATION,      dur(D_STANDARD, fluid), 0),
                    inst(MobEffects.REGENERATION,    dur(D_UTILITY,  fluid), 0)
            );

            // ── Wet Ground ───────────────────────────────────────────────────

            case "wet_ground_apple" -> pos(
                    inst(MobEffects.REGENERATION,    dur(D_POWERFUL, fluid), 0)
            );
            case "wet_ground_brown_mushroom" -> pn(
                    list(inst(MobEffects.SATURATION, dur(D_BRIEF,    fluid), 0)),
                    list(inst(MobEffects.CONFUSION,  dur(D_BRIEF,    fluid), 0))
            );
            case "wet_ground_red_mushroom" -> neg(
                    inst(MobEffects.POISON,          dur(D_STANDARD, fluid), 0),
                    inst(MobEffects.CONFUSION,       dur(D_POWERFUL, fluid), 0)
            );
            case "wet_ground_kelp" -> pos(
                    inst(MobEffects.WATER_BREATHING, dur(D_POWERFUL, fluid), 0)
            );
            case "wet_ground_melon" -> pn(
                    list(inst(MobEffects.SATURATION,         dur(D_POWERFUL, fluid), 0)),
                    list(inst(MobEffects.MOVEMENT_SLOWDOWN,  dur(D_BRIEF,    fluid), 0))
            );
            case "wet_ground_cactus" -> pn(
                    list(inst(MobEffects.DAMAGE_RESISTANCE,  dur(D_POWERFUL, fluid), 0)),
                    list(inst(MobEffects.POISON,             dur(D_BRIEF,    fluid), 0))
            );
            case "wet_ground_nether_wart" -> pn(
                    list(inst(MobEffects.DAMAGE_BOOST,       dur(D_BRIEF, fluid), 0)),
                    list(inst(MobEffects.WEAKNESS,           dur(D_BRIEF, fluid), 0))
            );
            case "wet_ground_crimson_fungus" -> pn(
                    list(inst(MobEffects.FIRE_RESISTANCE,    dur(D_BRIEF,    fluid), 0)),
                    list(inst(MobEffects.CONFUSION,          dur(D_BRIEF,    fluid), 0))
            );
            case "wet_ground_warped_fungus" -> pn(
                    list(inst(MobEffects.MOVEMENT_SPEED,     dur(D_POWERFUL, fluid), 0)),
                    list(inst(MobEffects.LEVITATION,         dur(D_BRIEF,    fluid), 0))
            );
            case "wet_ground_chorus_fruit" -> pn(
                    list(inst(MobEffects.SLOW_FALLING,       dur(D_BRIEF,    fluid), 0)),
                    list(inst(MobEffects.CONFUSION,          dur(D_POWERFUL, fluid), 0))
            );
            case "wet_ground_glow_berries" -> pos(
                    inst(MobEffects.NIGHT_VISION,    dur(D_POWERFUL, fluid), 0)
            );
            case "wet_ground_phantom_membrane" -> pn(
                    list(inst(MobEffects.NIGHT_VISION,       dur(D_STANDARD, fluid), 0)),
                    list(inst(MobEffects.WEAKNESS,           dur(D_BRIEF,    fluid), 0))
            );
            case "wet_ground_ink_sac" -> pn(
                    list(inst(MobEffects.INVISIBILITY,       dur(D_BRIEF,    fluid), 0)),
                    list(inst(MobEffects.BLINDNESS,          dur(D_BRIEF,    fluid), 0))
            );
            case "wet_ground_slimeball" -> pn(
                    list(inst(MobEffects.JUMP,               dur(D_POWERFUL, fluid), 0)),
                    list(inst(MobEffects.MOVEMENT_SLOWDOWN,  dur(D_POWERFUL, fluid), 0))
            );
            case "wet_ground_sea_pickle" -> pos(
                    inst(MobEffects.NIGHT_VISION,    dur(D_POWERFUL, fluid), 0)
            );
            case "wet_ground_magma_cream" -> pn(
                    list(inst(MobEffects.FIRE_RESISTANCE,    dur(D_BRIEF,    fluid), 0)),
                    list(inst(MobEffects.MOVEMENT_SLOWDOWN,  dur(D_POWERFUL, fluid), 0))
            );
            case "wet_ground_spider_eye" -> pn(
                    list(inst(MobEffects.DAMAGE_BOOST,       dur(D_BRIEF,    fluid), 0)),
                    list(inst(MobEffects.POISON,             dur(D_STANDARD, fluid), 0))
            );
            case "wet_ground_fermented_spider_eye" -> pn(
                    list(inst(MobEffects.INVISIBILITY,       dur(D_BRIEF,    fluid), 0)),
                    list(inst(MobEffects.WEAKNESS,           dur(D_POWERFUL, fluid), 0))
            );
            case "wet_ground_bamboo" -> pos(
                    inst(MobEffects.DIG_SPEED,       dur(D_BRIEF,    fluid), 0)
            );

            // ── Ground ───────────────────────────────────────────────────────

            case "ground_sugar" -> pos(
                    inst(MobEffects.DIG_SPEED,       dur(D_STANDARD, fluid), a + 1),
                    inst(MobEffects.MOVEMENT_SPEED,  dur(D_STANDARD, fluid), a)
            );
            case "ground_sweet_berries" -> pos(
                    inst(MobEffects.NIGHT_VISION,    dur(D_LONG,     fluid), 0),
                    inst(MobEffects.REGENERATION,    dur(D_BRIEF,    fluid), 0)
            );
            case "ground_honeycomb" -> pos(
                    inst(MobEffects.DAMAGE_RESISTANCE, dur(D_STANDARD, fluid), a),
                    inst(MobEffects.SATURATION,        dur(D_STANDARD, fluid), 0)
            );
            case "ground_dandelion" -> pos(
                    inst(MobEffects.ABSORPTION,      dur(D_POWERFUL, fluid), 0),
                    inst(MobEffects.SATURATION,      dur(D_BRIEF,    fluid), 0)
            );
            case "ground_poppy" -> pn(
                    list(inst(MobEffects.NIGHT_VISION,      dur(D_STANDARD, fluid), 0)),
                    list(inst(MobEffects.MOVEMENT_SLOWDOWN, dur(D_POWERFUL, fluid), 0))
            );
            case "ground_lily_of_the_valley" -> pn(
                    list(inst(MobEffects.DAMAGE_RESISTANCE, dur(D_STANDARD, fluid), a)),
                    list(inst(MobEffects.POISON,            dur(D_BRIEF,    fluid), 0))
            );
            case "ground_torchflower" -> pos(
                    inst(MobEffects.FIRE_RESISTANCE, dur(D_LONG,     fluid), 0),
                    inst(MobEffects.GLOWING,         dur(D_STANDARD, fluid), 0)
            );
            case "ground_blaze_powder" -> pos(
                    inst(MobEffects.DAMAGE_BOOST,    dur(D_POWERFUL, fluid), a),
                    inst(MobEffects.FIRE_RESISTANCE, dur(D_POWERFUL, fluid), 0)
            );
            case "ground_ghast_tear" -> pos(
                    inst(MobEffects.REGENERATION,    dur(D_POWERFUL, fluid), a + 2)
            );
            case "ground_wither_rose" -> pn(
                    list(inst(MobEffects.DAMAGE_BOOST, dur(D_POWERFUL, fluid), a + 2)),
                    list(inst(MobEffects.WITHER,       dur(D_BRIEF,    fluid), 0))
            );
            case "ground_allium" -> pos(
                    inst(MobEffects.DAMAGE_RESISTANCE, dur(D_STANDARD, fluid), a),
                    inst(MobEffects.DAMAGE_BOOST,      dur(D_POWERFUL, fluid), 0)
            );
            case "ground_sunflower" -> pos(
                    inst(MobEffects.MOVEMENT_SPEED,  dur(D_STANDARD, fluid), a),
                    inst(MobEffects.JUMP,            dur(D_POWERFUL, fluid), 0)
            );
            case "ground_cornflower" -> pos(
                    inst(MobEffects.DIG_SPEED,       dur(D_STANDARD, fluid), a),
                    inst(MobEffects.NIGHT_VISION,    dur(D_POWERFUL, fluid), 0)
            );
            case "ground_azure_bluet" -> pos(
                    inst(MobEffects.REGENERATION,    dur(D_STANDARD, fluid), a),
                    inst(MobEffects.ABSORPTION,      dur(D_BRIEF,    fluid), 0)
            );
            case "ground_oxeye_daisy" -> pos(
                    inst(MobEffects.DAMAGE_RESISTANCE, dur(D_STANDARD, fluid), a),
                    inst(MobEffects.SATURATION,        dur(D_BRIEF,    fluid), 0)
            );
            case "ground_red_tulip" -> pos(
                    inst(MobEffects.DAMAGE_BOOST,    dur(D_STANDARD, fluid), a),
                    inst(MobEffects.REGENERATION,    dur(D_BRIEF,    fluid), 0)
            );
            case "ground_orange_tulip" -> pos(
                    inst(MobEffects.MOVEMENT_SPEED,  dur(D_STANDARD, fluid), a),
                    inst(MobEffects.FIRE_RESISTANCE, dur(D_POWERFUL, fluid), 0)
            );
            case "ground_white_tulip" -> pos(
                    inst(MobEffects.DAMAGE_RESISTANCE, dur(D_STANDARD, fluid), a),
                    inst(MobEffects.ABSORPTION,        dur(D_BRIEF,    fluid), 0)
            );
            case "ground_pink_tulip" -> pos(
                    inst(MobEffects.REGENERATION,    dur(D_STANDARD, fluid), a),
                    inst(MobEffects.SATURATION,      dur(D_BRIEF,    fluid), 0)
            );
            case "ground_lilac" -> pos(
                    inst(MobEffects.NIGHT_VISION,    dur(D_STANDARD, fluid), 0),
                    inst(MobEffects.SATURATION,      dur(D_BRIEF,    fluid), 0)
            );
            case "ground_peony" -> pos(
                    inst(MobEffects.SATURATION,      dur(D_STANDARD, fluid), 0),
                    inst(MobEffects.ABSORPTION,      dur(D_BRIEF,    fluid), 0)
            );
            case "ground_rose_bush" -> pos(
                    inst(MobEffects.DAMAGE_BOOST,    dur(D_STANDARD, fluid), a),
                    inst(MobEffects.ABSORPTION,      dur(D_BRIEF,    fluid), 0)
            );
            case "ground_carrot" -> pos(
                    inst(MobEffects.NIGHT_VISION,    dur(D_STANDARD, fluid), 0),
                    inst(MobEffects.JUMP,            dur(D_POWERFUL, fluid), 0)
            );
            case "ground_beetroot" -> pos(
                    inst(MobEffects.REGENERATION,    dur(D_STANDARD, fluid), a),
                    inst(MobEffects.DAMAGE_BOOST,    dur(D_BRIEF,    fluid), 0)
            );
            case "ground_potato" -> pn(
                    list(
                            inst(MobEffects.SATURATION,        dur(D_UTILITY,  fluid), 0),
                            inst(MobEffects.DAMAGE_RESISTANCE, dur(D_STANDARD, fluid), a)
                    ),
                    list(inst(MobEffects.MOVEMENT_SLOWDOWN,    dur(D_STANDARD, fluid), 0))
            );
            case "ground_cocoa_beans" -> pos(
                    inst(MobEffects.DIG_SPEED,       dur(D_POWERFUL, fluid), a),
                    inst(MobEffects.MOVEMENT_SPEED,  dur(D_STANDARD, fluid), a)
            );
            case "ground_spore_blossom" -> pn(
                    list(
                            inst(MobEffects.SATURATION,   dur(D_POWERFUL, fluid), 0),
                            inst(MobEffects.REGENERATION, dur(D_POWERFUL, fluid), a)
                    ),
                    list(inst(MobEffects.MOVEMENT_SLOWDOWN, dur(D_POWERFUL, fluid), 0))
            );
            case "ground_prismarine_shard" -> pos(
                    inst(MobEffects.WATER_BREATHING, dur(D_LONG,     fluid), 0),
                    inst(MobEffects.NIGHT_VISION,    dur(D_STANDARD, fluid), 0)
            );
            case "ground_ender_pearl" -> pos(
                    inst(MobEffects.MOVEMENT_SPEED,  dur(D_BRIEF,    fluid), a + 2),
                    inst(MobEffects.DIG_SPEED,       dur(D_BRIEF,    fluid), a)
            );
            case "ground_pink_petals" -> pos(
                    inst(MobEffects.REGENERATION,    dur(D_STANDARD, fluid), a)
            );
            case "ground_gunpowder" -> pn(
                    list(
                            inst(MobEffects.DAMAGE_BOOST,      dur(D_BRIEF, fluid), a + 1),
                            inst(MobEffects.DAMAGE_RESISTANCE, dur(D_BRIEF, fluid), 0)
                    ),
                    list(inst(MobEffects.CONFUSION,            dur(D_BRIEF, fluid), 0))
            );
            case "ground_redstone" -> pos(
                    inst(MobEffects.MOVEMENT_SPEED,  dur(D_STANDARD, fluid), a),
                    inst(MobEffects.DIG_SPEED,       dur(D_STANDARD, fluid), a)
            );
            case "ground_moss" -> pos(
                    inst(MobEffects.REGENERATION,    dur(D_UTILITY,  fluid), 0),
                    inst(MobEffects.WATER_BREATHING, dur(D_POWERFUL, fluid), 0)
            );
            case "ground_dragon_egg" -> pn(
                    list(inst(MobEffects.DAMAGE_BOOST, dur(D_POWERFUL, fluid), a + 2)),
                    list(
                            inst(MobEffects.LEVITATION, dur(D_BRIEF, fluid), 0),
                            inst(MobEffects.BLINDNESS,  dur(D_BRIEF, fluid), 0)
                    )
            );
            case "ground_amethyst" -> pos(
                    inst(MobEffects.GLOWING,           dur(D_STANDARD, fluid), 0),
                    inst(MobEffects.DAMAGE_RESISTANCE, dur(D_POWERFUL, fluid), a)
            );
            case "ground_weeping_vines" -> pn(
                    list(
                            inst(MobEffects.REGENERATION,      dur(D_STANDARD, fluid), a),
                            inst(MobEffects.DAMAGE_RESISTANCE, dur(D_LONG,     fluid), 0)
                    ),
                    list(inst(MobEffects.MOVEMENT_SLOWDOWN,    dur(D_STANDARD, fluid), 0))
            );
            case "ground_pumpkin" -> pos(
                    inst(MobEffects.NIGHT_VISION,    dur(D_STANDARD, fluid), 0),
                    inst(MobEffects.GLOWING,         dur(D_STANDARD, fluid), 0)
            );
            case "ground_coal" -> pos(
                    inst(MobEffects.FIRE_RESISTANCE,   dur(D_STANDARD, fluid), 0),
                    inst(MobEffects.DAMAGE_RESISTANCE, dur(D_POWERFUL, fluid), a)
            );
            case "ground_fire_charge" -> pos(
                    inst(MobEffects.DAMAGE_BOOST,    dur(D_BRIEF,    fluid), a + 2),
                    inst(MobEffects.FIRE_RESISTANCE, dur(D_POWERFUL, fluid), 0),
                    inst(ModMobEffects.FIRE_TRAIL,   dur(D_POWERFUL, fluid), 0)
            );
            case "ground_bone_meal" -> pos(
                    inst(MobEffects.REGENERATION,    dur(D_STANDARD, fluid), a),
                    inst(MobEffects.ABSORPTION,      dur(D_POWERFUL, fluid), 0)
            );
            case "ground_wind_charge" -> pos(
                    inst(MobEffects.MOVEMENT_SPEED,  dur(D_POWERFUL, fluid), a + 2),
                    inst(MobEffects.JUMP,            dur(D_POWERFUL, fluid), 1),
                    inst(MobEffects.SLOW_FALLING,    dur(D_STANDARD, fluid), 0)
            );
            case "ground_totem_of_undying" -> pos(
                    inst(MobEffects.REGENERATION,    dur(D_POWERFUL, fluid), a + 2),
                    inst(MobEffects.ABSORPTION,      dur(D_BRIEF,    fluid), 1),
                    inst(ModMobEffects.SOUL_SIGHT,   dur(D_BRIEF,    fluid), 0)
            );
            case "ground_tnt" -> pn(
                    list(
                            inst(MobEffects.DAMAGE_BOOST,     dur(D_BRIEF, fluid), a + 2),
                            inst(MobEffects.MOVEMENT_SPEED,   dur(D_BRIEF, fluid), a + 1)
                    ),
                    list(
                            inst(MobEffects.CONFUSION,        dur(D_BRIEF,    fluid), 0),
                            inst(MobEffects.WEAKNESS,         dur(D_POWERFUL, fluid), 0)
                    )
            );
            case "ground_end_crystal" -> pos(
                    inst(MobEffects.REGENERATION,    dur(D_POWERFUL, fluid), a + 3),
                    inst(MobEffects.GLOWING,         dur(D_STANDARD, fluid), 0)
            );
            case "ground_iron_nugget" -> pos(
                    inst(MobEffects.DAMAGE_RESISTANCE, dur(D_STANDARD, fluid), a),
                    inst(ModMobEffects.IRON_SKIN,      dur(D_STANDARD, fluid), 0)
            );
            case "ground_gold_nugget" -> pos(
                    inst(ModMobEffects.GOLD_PACT,    dur(D_LONG,     fluid), 0),
                    inst(MobEffects.SATURATION,      dur(D_POWERFUL, fluid), 0)
            );
            case "ground_netherite" -> pos(
                    inst(MobEffects.DAMAGE_RESISTANCE, dur(D_STANDARD, fluid), a + 1),
                    inst(MobEffects.FIRE_RESISTANCE,   dur(D_STANDARD, fluid), 0),
                    inst(MobEffects.DAMAGE_BOOST,      dur(D_STANDARD, fluid), a)
            );
            case "ground_nether_star" -> pn(
                    list(inst(MobEffects.REGENERATION, dur(D_POWERFUL, fluid), a + 3)),
                    list(inst(MobEffects.BLINDNESS,    dur(D_BRIEF,    fluid), 0))
            );
            case "ground_echo_shard" -> pn(
                    list(inst(MobEffects.NIGHT_VISION, dur(D_UTILITY,  fluid), 0)),
                    list(inst(MobEffects.DARKNESS,     dur(D_BRIEF,    fluid), 0))
            );
            case "ground_golden_carrot" -> pos(
                    inst(MobEffects.NIGHT_VISION,    dur(D_UTILITY,  fluid), 0),
                    inst(MobEffects.SATURATION,      dur(D_STANDARD, fluid), 0)
            );

            default -> IngredientEffects.EMPTY;
        };
    }

    // ── Синергии ──────────────────────────────────────────────────────────────

    private static List<MobEffectInstance> getSynergyEffects(List<String> ingredients, HookahFluidType fluid) {
        return getActiveSynergies(ingredients, fluid).stream()
                .flatMap(s -> s.bonus().stream())
                .collect(java.util.stream.Collectors.toList());
    }

    // ── Хелперы ───────────────────────────────────────────────────────────────

    private static int dur(int base, HookahFluidType fluid) {
        return fluid == HookahFluidType.MILK ? base * 3 / 2 : base;
    }

    private static int amp(HookahFluidType fluid) {
        return fluid == HookahFluidType.LAVA ? 1 : 0;
    }

    private static boolean has(List<String> ingredients, String... required) {
        for (String r : required) {
            if (!ingredients.contains(r)) return false;
        }
        return true;
    }

    private static MobEffectInstance inst(Holder<MobEffect> effect, int duration, int amplifier) {
        return new MobEffectInstance(effect, duration, amplifier);
    }

    private static IngredientEffects pos(MobEffectInstance... effects) {
        return new IngredientEffects(List.of(effects), List.of(), List.of());
    }

    private static IngredientEffects neg(MobEffectInstance... effects) {
        return new IngredientEffects(List.of(), List.of(effects), List.of());
    }

    private static IngredientEffects pn(List<MobEffectInstance> pos, List<MobEffectInstance> neg) {
        return new IngredientEffects(pos, neg, List.of());
    }

    private static List<MobEffectInstance> list(MobEffectInstance... effects) {
        return List.of(effects);
    }
}
