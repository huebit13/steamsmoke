package com.huebit.steamsmoke;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class DryingRecipes {

    public record DryingRecipe(Item output, int durationTicks) {
        public ItemStack createResult() {
            return new ItemStack(output);
        }
    }

    private static final Map<Item, DryingRecipe> RECIPES = new HashMap<>();

    static {
        // ── Табак ────────────────────────────────────────────────────────────
        add(ModItems.TOBACCO_LEAF.get(),        ModItems.DRIED_TOBACCO_LEAF.get(),          24000); // 20 мин
        // ── Базовые ──────────────────────────────────────────────────────────
        add(Items.APPLE,                         ModItems.DRIED_APPLE.get(),                 16800); // 14 мин
        add(Items.BROWN_MUSHROOM,                ModItems.DRIED_BROWN_MUSHROOM.get(),        14400); // 12 мин
        add(Items.RED_MUSHROOM,                  ModItems.DRIED_RED_MUSHROOM.get(),          14400); // 12 мин
        add(Items.KELP,                          Items.DRIED_KELP,                           12000); // 10 мин → ваниль
        add(Items.DRIED_KELP,                    ModItems.DRIED_KELP_HERB.get(),              9600); //  8 мин
        add(Items.MELON_SLICE,                   ModItems.DRIED_MELON.get(),                 19200); // 16 мин
        add(Items.CACTUS,                        ModItems.DRIED_CACTUS.get(),                21600); // 18 мин
        // ── Незер / особые ───────────────────────────────────────────────────
        add(Items.NETHER_WART,                   ModItems.DRIED_NETHER_WART.get(),           18000); // 15 мин
        add(Items.CRIMSON_FUNGUS,                ModItems.DRIED_CRIMSON_FUNGUS.get(),        15600); // 13 мин
        add(Items.WARPED_FUNGUS,                 ModItems.DRIED_WARPED_FUNGUS.get(),         15600); // 13 мин
        add(Items.CHORUS_FRUIT,                  ModItems.DRIED_CHORUS_FRUIT.get(),          24000); // 20 мин
        add(Items.GLOW_BERRIES,                  ModItems.DRIED_GLOW_BERRIES.get(),          13200); // 11 мин
        // ── Поздняя игра ─────────────────────────────────────────────────────
        add(Items.PHANTOM_MEMBRANE,              ModItems.DRIED_PHANTOM_MEMBRANE.get(),      28800); // 24 мин
        // ── Надземный мир / разное ────────────────────────────────────────────
        add(Items.RABBIT_FOOT,                   ModItems.DRIED_RABBIT_FOOT.get(),           21600); // 18 мин
        add(Items.SLIME_BALL,                    ModItems.DRIED_SLIMEBALL.get(),             26400); // 22 мин
        add(Items.SEA_PICKLE,                    ModItems.DRIED_SEA_PICKLE.get(),            12000); // 10 мин
        add(Items.MAGMA_CREAM,                   ModItems.DRIED_MAGMA_CREAM.get(),           26400); // 22 мин
        add(Items.SPIDER_EYE,                    ModItems.DRIED_SPIDER_EYE.get(),            19200); // 16 мин
        add(Items.FERMENTED_SPIDER_EYE,          ModItems.DRIED_FERMENTED_SPIDER_EYE.get(), 19200); // 16 мин
        add(Items.BAMBOO,                        ModItems.DRIED_BAMBOO.get(),                 9600); //  8 мин
    }

    private static void add(Item input, Item output, int durationTicks) {
        RECIPES.put(input, new DryingRecipe(output, durationTicks));
    }

    public static DryingRecipe getRecipe(ItemStack stack) {
        return RECIPES.get(stack.getItem());
    }

    public static boolean canDry(ItemStack stack) {
        return RECIPES.containsKey(stack.getItem());
    }
}
