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
        // ── Сырые с сушкой → wet_ground_* ────────────────────────────────────
        // Сырые варианты дают "влажный" перемол, хуже по качеству
        // Табак нельзя молоть сырым — только через сушку
        register(Items.APPLE,                    new ItemStack(ModItems.WET_GROUND_APPLE.get()));
        register(Items.BROWN_MUSHROOM,           new ItemStack(ModItems.WET_GROUND_BROWN_MUSHROOM.get()));
        register(Items.RED_MUSHROOM,             new ItemStack(ModItems.WET_GROUND_RED_MUSHROOM.get()));
        register(Items.KELP,                     new ItemStack(ModItems.WET_GROUND_KELP.get()));
        register(Items.MELON_SLICE,              new ItemStack(ModItems.WET_GROUND_MELON.get()));
        register(Items.CACTUS,                   new ItemStack(ModItems.WET_GROUND_CACTUS.get()));
        register(Items.NETHER_WART,              new ItemStack(ModItems.WET_GROUND_NETHER_WART.get()));
        register(Items.CRIMSON_FUNGUS,           new ItemStack(ModItems.WET_GROUND_CRIMSON_FUNGUS.get()));
        register(Items.WARPED_FUNGUS,            new ItemStack(ModItems.WET_GROUND_WARPED_FUNGUS.get()));
        register(Items.CHORUS_FRUIT,             new ItemStack(ModItems.WET_GROUND_CHORUS_FRUIT.get()));
        register(Items.GLOW_BERRIES,             new ItemStack(ModItems.WET_GROUND_GLOW_BERRIES.get()));
        register(Items.PHANTOM_MEMBRANE,         new ItemStack(ModItems.WET_GROUND_PHANTOM_MEMBRANE.get()));
        register(Items.INK_SAC,                  new ItemStack(ModItems.WET_GROUND_INK_SAC.get()));
        register(Items.SLIME_BALL,               new ItemStack(ModItems.WET_GROUND_SLIMEBALL.get()));
        register(Items.SEA_PICKLE,               new ItemStack(ModItems.WET_GROUND_SEA_PICKLE.get()));
        register(Items.MAGMA_CREAM,              new ItemStack(ModItems.WET_GROUND_MAGMA_CREAM.get()));
        register(Items.SPIDER_EYE,               new ItemStack(ModItems.WET_GROUND_SPIDER_EYE.get()));
        register(Items.FERMENTED_SPIDER_EYE,     new ItemStack(ModItems.WET_GROUND_FERMENTED_SPIDER_EYE.get()));
        register(Items.BAMBOO,                   new ItemStack(ModItems.WET_GROUND_BAMBOO.get()));

        // ── Высушенные → dried_ground_* ──────────────────────────────────────
        // Высушенные дают качественный перемол для кальяна
        register(ModItems.DRIED_TOBACCO_LEAF.get(),        new ItemStack(ModItems.DRIED_GROUND_TOBACCO.get()));
        register(ModItems.DRIED_APPLE.get(),                new ItemStack(ModItems.DRIED_GROUND_APPLE.get()));
        register(ModItems.DRIED_BROWN_MUSHROOM.get(),       new ItemStack(ModItems.DRIED_GROUND_BROWN_MUSHROOM.get()));
        register(ModItems.DRIED_RED_MUSHROOM.get(),         new ItemStack(ModItems.DRIED_GROUND_RED_MUSHROOM.get()));
        register(ModItems.DRIED_KELP_HERB.get(),            new ItemStack(ModItems.DRIED_GROUND_KELP.get()));
        register(Items.DRIED_KELP,                          new ItemStack(ModItems.DRIED_GROUND_KELP.get()));
        register(ModItems.DRIED_MELON.get(),                new ItemStack(ModItems.DRIED_GROUND_MELON.get()));
        register(ModItems.DRIED_CACTUS.get(),               new ItemStack(ModItems.DRIED_GROUND_CACTUS.get()));
        register(ModItems.DRIED_NETHER_WART.get(),          new ItemStack(ModItems.DRIED_GROUND_NETHER_WART.get()));
        register(ModItems.DRIED_CRIMSON_FUNGUS.get(),       new ItemStack(ModItems.DRIED_GROUND_CRIMSON_FUNGUS.get()));
        register(ModItems.DRIED_WARPED_FUNGUS.get(),        new ItemStack(ModItems.DRIED_GROUND_WARPED_FUNGUS.get()));
        register(ModItems.DRIED_CHORUS_FRUIT.get(),         new ItemStack(ModItems.DRIED_GROUND_CHORUS_FRUIT.get()));
        register(ModItems.DRIED_GLOW_BERRIES.get(),         new ItemStack(ModItems.DRIED_GROUND_GLOW_BERRIES.get()));
        register(ModItems.DRIED_PHANTOM_MEMBRANE.get(),     new ItemStack(ModItems.DRIED_GROUND_PHANTOM_MEMBRANE.get()));
        register(ModItems.DRIED_RABBIT_HIDE.get(),          new ItemStack(ModItems.DRIED_GROUND_RABBIT_HIDE.get()));
        register(ModItems.DRIED_INK_SAC.get(),              new ItemStack(ModItems.DRIED_GROUND_INK_SAC.get()));
        register(ModItems.DRIED_SLIMEBALL.get(),            new ItemStack(ModItems.DRIED_GROUND_SLIMEBALL.get()));
        register(ModItems.DRIED_SEA_PICKLE.get(),           new ItemStack(ModItems.DRIED_GROUND_SEA_PICKLE.get()));
        register(ModItems.DRIED_MAGMA_CREAM.get(),          new ItemStack(ModItems.DRIED_GROUND_MAGMA_CREAM.get()));
        register(ModItems.DRIED_SPIDER_EYE.get(),           new ItemStack(ModItems.DRIED_GROUND_SPIDER_EYE.get()));
        register(ModItems.DRIED_FERMENTED_SPIDER_EYE.get(), new ItemStack(ModItems.DRIED_GROUND_FERMENTED_SPIDER_EYE.get()));
        register(ModItems.DRIED_BAMBOO.get(),               new ItemStack(ModItems.DRIED_GROUND_BAMBOO.get()));

        // ── Без сушки (только один вариант) ──────────────────────────────────
        register(Items.SUGAR,                   new ItemStack(ModItems.GROUND_SUGAR.get()));
        register(Items.SWEET_BERRIES,           new ItemStack(ModItems.GROUND_SWEET_BERRIES.get()));
        register(Items.HONEYCOMB,               new ItemStack(ModItems.GROUND_HONEYCOMB.get()));
        register(Items.DANDELION,               new ItemStack(ModItems.GROUND_DANDELION.get()));
        register(Items.POPPY,                   new ItemStack(ModItems.GROUND_POPPY.get()));
        register(Items.LILY_OF_THE_VALLEY,      new ItemStack(ModItems.GROUND_LILY_OF_THE_VALLEY.get()));
        register(Items.TORCHFLOWER,             new ItemStack(ModItems.GROUND_TORCHFLOWER.get()));
        register(Items.BLAZE_POWDER,            new ItemStack(ModItems.GROUND_BLAZE_POWDER.get()));
        register(Items.GHAST_TEAR,              new ItemStack(ModItems.GROUND_GHAST_TEAR.get()));
        register(Items.WITHER_ROSE,             new ItemStack(ModItems.GROUND_WITHER_ROSE.get()));
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
        register(Items.CARROT,                  new ItemStack(ModItems.GROUND_CARROT.get()));
        register(Items.BEETROOT,                new ItemStack(ModItems.GROUND_BEETROOT.get()));
        register(Items.BAKED_POTATO,             new ItemStack(ModItems.GROUND_POTATO.get()));
        register(Items.COCOA_BEANS,             new ItemStack(ModItems.GROUND_COCOA_BEANS.get()));
        register(Items.SPORE_BLOSSOM,           new ItemStack(ModItems.GROUND_SPORE_BLOSSOM.get()));
        register(Items.PRISMARINE_SHARD,        new ItemStack(ModItems.GROUND_PRISMARINE_SHARD.get()));
        register(Items.ENDER_PEARL,             new ItemStack(ModItems.GROUND_ENDER_PEARL.get()));
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
