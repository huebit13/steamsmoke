package com.huebit.steamsmoke;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GrindingRecipes {

    private static final Map<Item, ItemStack> RECIPES = new HashMap<>();
    private static final Set<Item> GROUND_ITEMS = new HashSet<>();

    static {
        // ── Базовые ──────────────────────────────────────────────────────────
        register(ModItems.TOBACCO_LEAF.get(),   new ItemStack(ModItems.GROUND_TOBACCO.get()));
        register(Items.APPLE,                   new ItemStack(ModItems.GROUND_APPLE.get()));
        register(Items.SUGAR,                   new ItemStack(ModItems.GROUND_SUGAR.get()));
        register(Items.SWEET_BERRIES,           new ItemStack(ModItems.GROUND_SWEET_BERRIES.get()));
        register(Items.HONEYCOMB,               new ItemStack(ModItems.GROUND_HONEYCOMB.get()));

        // ── Ранняя игра ───────────────────────────────────────────────────────
        register(Items.DANDELION,               new ItemStack(ModItems.GROUND_DANDELION.get()));
        register(Items.POPPY,                   new ItemStack(ModItems.GROUND_POPPY.get()));
        register(Items.BROWN_MUSHROOM,          new ItemStack(ModItems.GROUND_BROWN_MUSHROOM.get()));
        register(Items.RED_MUSHROOM,            new ItemStack(ModItems.GROUND_RED_MUSHROOM.get()));
        register(Items.KELP,                    new ItemStack(ModItems.GROUND_KELP.get()));
        register(Items.MELON_SLICE,             new ItemStack(ModItems.GROUND_MELON.get()));
        register(Items.CACTUS,                  new ItemStack(ModItems.GROUND_CACTUS.get()));
        register(Items.LILY_OF_THE_VALLEY,      new ItemStack(ModItems.GROUND_LILY_OF_THE_VALLEY.get()));

        // ── Средняя игра (Незер / особые) ────────────────────────────────────
        register(Items.NETHER_WART,             new ItemStack(ModItems.GROUND_NETHER_WART.get()));
        register(Items.CRIMSON_FUNGUS,          new ItemStack(ModItems.GROUND_CRIMSON_FUNGUS.get()));
        register(Items.WARPED_FUNGUS,           new ItemStack(ModItems.GROUND_WARPED_FUNGUS.get()));
        register(Items.CHORUS_FRUIT,            new ItemStack(ModItems.GROUND_CHORUS_FRUIT.get()));
        register(Items.GLOW_BERRIES,            new ItemStack(ModItems.GROUND_GLOW_BERRIES.get()));
        register(Items.TORCHFLOWER,             new ItemStack(ModItems.GROUND_TORCHFLOWER.get()));

        // ── Поздняя игра / редкие ─────────────────────────────────────────────
        register(Items.BLAZE_POWDER,            new ItemStack(ModItems.GROUND_BLAZE_POWDER.get()));
        register(Items.GHAST_TEAR,              new ItemStack(ModItems.GROUND_GHAST_TEAR.get()));
        register(Items.PHANTOM_MEMBRANE,        new ItemStack(ModItems.GROUND_PHANTOM_MEMBRANE.get()));
        register(Items.WITHER_ROSE,             new ItemStack(ModItems.GROUND_WITHER_ROSE.get()));

        // ── Цветы ────────────────────────────────────────────────────────────
        register(Items.ALLIUM,                  new ItemStack(ModItems.GROUND_ALLIUM.get()));
        register(Items.SUNFLOWER,               new ItemStack(ModItems.GROUND_SUNFLOWER.get()));
        register(Items.CORNFLOWER,              new ItemStack(ModItems.GROUND_CORNFLOWER.get()));
        register(Items.AZURE_BLUET,             new ItemStack(ModItems.GROUND_AZURE_BLUET.get()));
        register(Items.OXEYE_DAISY,             new ItemStack(ModItems.GROUND_OXEYE_DAISY.get()));
        register(Items.RED_TULIP,               new ItemStack(ModItems.GROUND_RED_TULIP.get()));
        register(Items.ORANGE_TULIP,            new ItemStack(ModItems.GROUND_ORANGE_TULIP.get()));
        register(Items.WHITE_TULIP,             new ItemStack(ModItems.GROUND_WHITE_TULIP.get()));
        register(Items.PINK_TULIP,              new ItemStack(ModItems.GROUND_PINK_TULIP.get()));
        register(Items.LILAC,                   new ItemStack(ModItems.GROUND_LILAC.get()));
        register(Items.PEONY,                   new ItemStack(ModItems.GROUND_PEONY.get()));
        register(Items.ROSE_BUSH,               new ItemStack(ModItems.GROUND_ROSE_BUSH.get()));

        // ── Еда и урожай ─────────────────────────────────────────────────────
        register(Items.CARROT,                  new ItemStack(ModItems.GROUND_CARROT.get()));
        register(Items.BEETROOT,                new ItemStack(ModItems.GROUND_BEETROOT.get()));
        register(Items.POTATO,                  new ItemStack(ModItems.GROUND_POTATO.get()));
        register(Items.COCOA_BEANS,             new ItemStack(ModItems.GROUND_COCOA_BEANS.get()));
        register(Items.DRIED_KELP,              new ItemStack(ModItems.GROUND_DRIED_KELP.get()));

        // ── Надземный мир / разное ────────────────────────────────────────────
        register(Items.RABBIT_FOOT,             new ItemStack(ModItems.GROUND_RABBIT_FOOT.get()));
        register(Items.SLIME_BALL,              new ItemStack(ModItems.GROUND_SLIMEBALL.get()));
        register(Items.SPORE_BLOSSOM,           new ItemStack(ModItems.GROUND_SPORE_BLOSSOM.get()));
        register(Items.INK_SAC,                 new ItemStack(ModItems.GROUND_INK_SAC.get()));
        register(Items.SEA_PICKLE,              new ItemStack(ModItems.GROUND_SEA_PICKLE.get()));

        // ── Средняя игра / вода / Незер ──────────────────────────────────────
        register(Items.MAGMA_CREAM,             new ItemStack(ModItems.GROUND_MAGMA_CREAM.get()));
        register(Items.FERMENTED_SPIDER_EYE,    new ItemStack(ModItems.GROUND_FERMENTED_SPIDER_EYE.get()));
        register(Items.PRISMARINE_SHARD,        new ItemStack(ModItems.GROUND_PRISMARINE_SHARD.get()));
        register(Items.SPIDER_EYE,              new ItemStack(ModItems.GROUND_SPIDER_EYE.get()));

        // ── Поздняя игра / Край ───────────────────────────────────────────────
        register(Items.ENDER_PEARL,             new ItemStack(ModItems.GROUND_ENDER_PEARL.get()));
        register(Items.DRAGON_BREATH,           new ItemStack(ModItems.GROUND_DRAGON_BREATH.get()));
        register(Items.BAMBOO,                  new ItemStack(ModItems.GROUND_BAMBOO.get()));
        register(Items.PINK_PETALS,             new ItemStack(ModItems.GROUND_PINK_PETALS.get()));
    }

    public static void register(Item input, ItemStack output) {
        RECIPES.put(input, output);
        GROUND_ITEMS.add(output.getItem());
    }

    public static ItemStack getResult(ItemStack input) {
        ItemStack result = RECIPES.get(input.getItem());
        return result != null ? result.copy() : ItemStack.EMPTY;
    }

    public static boolean canGrind(ItemStack stack) {
        return RECIPES.containsKey(stack.getItem());
    }

    public static boolean isGround(ItemStack stack) {
        return GROUND_ITEMS.contains(stack.getItem());
    }
}
