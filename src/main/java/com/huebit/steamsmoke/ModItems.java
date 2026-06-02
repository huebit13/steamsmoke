package com.huebit.steamsmoke;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SteamSmoke.MODID);

    // ── Базовые ──────────────────────────────────────────────────────────────
    public static final DeferredItem<Item> TOBACCO_LEAF = ITEMS.registerSimpleItem(
            "tobacco_leaf", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> PESTLE = ITEMS.registerSimpleItem(
            "pestle", new Item.Properties().stacksTo(1).durability(64));

    // ── Высушенные (результат стойки сушки) ──────────────────────────────────
    public static final DeferredItem<Item> DRIED_TOBACCO_LEAF = ITEMS.registerSimpleItem(
            "dried_tobacco_leaf", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_APPLE = ITEMS.registerSimpleItem(
            "dried_apple", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_BROWN_MUSHROOM = ITEMS.registerSimpleItem(
            "dried_brown_mushroom", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_RED_MUSHROOM = ITEMS.registerSimpleItem(
            "dried_red_mushroom", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_KELP_HERB = ITEMS.registerSimpleItem(
            "dried_kelp_herb", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_MELON = ITEMS.registerSimpleItem(
            "dried_melon", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_CACTUS = ITEMS.registerSimpleItem(
            "dried_cactus", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_NETHER_WART = ITEMS.registerSimpleItem(
            "dried_nether_wart", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_CRIMSON_FUNGUS = ITEMS.registerSimpleItem(
            "dried_crimson_fungus", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_WARPED_FUNGUS = ITEMS.registerSimpleItem(
            "dried_warped_fungus", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_CHORUS_FRUIT = ITEMS.registerSimpleItem(
            "dried_chorus_fruit", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GLOW_BERRIES = ITEMS.registerSimpleItem(
            "dried_glow_berries", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_PHANTOM_MEMBRANE = ITEMS.registerSimpleItem(
            "dried_phantom_membrane", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_RABBIT_HIDE = ITEMS.registerSimpleItem(
            "dried_rabbit_hide", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_INK_SAC = ITEMS.registerSimpleItem(
            "dried_ink_sac", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_SLIMEBALL = ITEMS.registerSimpleItem(
            "dried_slimeball", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_SEA_PICKLE = ITEMS.registerSimpleItem(
            "dried_sea_pickle", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_MAGMA_CREAM = ITEMS.registerSimpleItem(
            "dried_magma_cream", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_SPIDER_EYE = ITEMS.registerSimpleItem(
            "dried_spider_eye", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_FERMENTED_SPIDER_EYE = ITEMS.registerSimpleItem(
            "dried_fermented_spider_eye", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_BAMBOO = ITEMS.registerSimpleItem(
            "dried_bamboo", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_PUFFERFISH = ITEMS.registerSimpleItem(
            "dried_pufferfish", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_LEAVES = ITEMS.registerSimpleItem(
            "dried_leaves", new Item.Properties().stacksTo(64));

    // ── Сырые перемолотые (без сушки; табак — только через сушку) ────────────
    public static final DeferredItem<Item> WET_GROUND_APPLE = ITEMS.registerSimpleItem(
            "wet_ground_apple", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> WET_GROUND_BROWN_MUSHROOM = ITEMS.registerSimpleItem(
            "wet_ground_brown_mushroom", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> WET_GROUND_RED_MUSHROOM = ITEMS.registerSimpleItem(
            "wet_ground_red_mushroom", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> WET_GROUND_KELP = ITEMS.registerSimpleItem(
            "wet_ground_kelp", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> WET_GROUND_MELON = ITEMS.registerSimpleItem(
            "wet_ground_melon", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> WET_GROUND_CACTUS = ITEMS.registerSimpleItem(
            "wet_ground_cactus", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> WET_GROUND_NETHER_WART = ITEMS.registerSimpleItem(
            "wet_ground_nether_wart", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> WET_GROUND_CRIMSON_FUNGUS = ITEMS.registerSimpleItem(
            "wet_ground_crimson_fungus", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> WET_GROUND_WARPED_FUNGUS = ITEMS.registerSimpleItem(
            "wet_ground_warped_fungus", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> WET_GROUND_CHORUS_FRUIT = ITEMS.registerSimpleItem(
            "wet_ground_chorus_fruit", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> WET_GROUND_GLOW_BERRIES = ITEMS.registerSimpleItem(
            "wet_ground_glow_berries", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> WET_GROUND_PHANTOM_MEMBRANE = ITEMS.registerSimpleItem(
            "wet_ground_phantom_membrane", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> WET_GROUND_INK_SAC = ITEMS.registerSimpleItem(
            "wet_ground_ink_sac", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> WET_GROUND_SLIMEBALL = ITEMS.registerSimpleItem(
            "wet_ground_slimeball", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> WET_GROUND_SEA_PICKLE = ITEMS.registerSimpleItem(
            "wet_ground_sea_pickle", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> WET_GROUND_MAGMA_CREAM = ITEMS.registerSimpleItem(
            "wet_ground_magma_cream", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> WET_GROUND_SPIDER_EYE = ITEMS.registerSimpleItem(
            "wet_ground_spider_eye", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> WET_GROUND_FERMENTED_SPIDER_EYE = ITEMS.registerSimpleItem(
            "wet_ground_fermented_spider_eye", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> WET_GROUND_BAMBOO = ITEMS.registerSimpleItem(
            "wet_ground_bamboo", new Item.Properties().stacksTo(64));

    // ── Сушёные перемолотые (сушка + перемолка) ──────────────────────────────
    public static final DeferredItem<Item> DRIED_GROUND_TOBACCO = ITEMS.registerSimpleItem(
            "dried_ground_tobacco", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_APPLE = ITEMS.registerSimpleItem(
            "dried_ground_apple", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_BROWN_MUSHROOM = ITEMS.registerSimpleItem(
            "dried_ground_brown_mushroom", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_RED_MUSHROOM = ITEMS.registerSimpleItem(
            "dried_ground_red_mushroom", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_KELP = ITEMS.registerSimpleItem(
            "dried_ground_kelp", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_MELON = ITEMS.registerSimpleItem(
            "dried_ground_melon", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_CACTUS = ITEMS.registerSimpleItem(
            "dried_ground_cactus", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_NETHER_WART = ITEMS.registerSimpleItem(
            "dried_ground_nether_wart", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_CRIMSON_FUNGUS = ITEMS.registerSimpleItem(
            "dried_ground_crimson_fungus", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_WARPED_FUNGUS = ITEMS.registerSimpleItem(
            "dried_ground_warped_fungus", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_CHORUS_FRUIT = ITEMS.registerSimpleItem(
            "dried_ground_chorus_fruit", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_GLOW_BERRIES = ITEMS.registerSimpleItem(
            "dried_ground_glow_berries", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_PHANTOM_MEMBRANE = ITEMS.registerSimpleItem(
            "dried_ground_phantom_membrane", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_RABBIT_HIDE = ITEMS.registerSimpleItem(
            "dried_ground_rabbit_hide", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_INK_SAC = ITEMS.registerSimpleItem(
            "dried_ground_ink_sac", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_SLIMEBALL = ITEMS.registerSimpleItem(
            "dried_ground_slimeball", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_SEA_PICKLE = ITEMS.registerSimpleItem(
            "dried_ground_sea_pickle", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_MAGMA_CREAM = ITEMS.registerSimpleItem(
            "dried_ground_magma_cream", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_SPIDER_EYE = ITEMS.registerSimpleItem(
            "dried_ground_spider_eye", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_FERMENTED_SPIDER_EYE = ITEMS.registerSimpleItem(
            "dried_ground_fermented_spider_eye", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_BAMBOO = ITEMS.registerSimpleItem(
            "dried_ground_bamboo", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_PUFFERFISH = ITEMS.registerSimpleItem(
            "dried_ground_pufferfish", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> DRIED_GROUND_LEAVES = ITEMS.registerSimpleItem(
            "dried_ground_leaves", new Item.Properties().stacksTo(64));

    // ── Обычные перемолотые (предметы без сушки) ──────────────────────────────
    public static final DeferredItem<Item> GROUND_SUGAR = ITEMS.registerSimpleItem(
            "ground_sugar", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_SWEET_BERRIES = ITEMS.registerSimpleItem(
            "ground_sweet_berries", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_HONEYCOMB = ITEMS.registerSimpleItem(
            "ground_honeycomb", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_DANDELION = ITEMS.registerSimpleItem(
            "ground_dandelion", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_POPPY = ITEMS.registerSimpleItem(
            "ground_poppy", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_LILY_OF_THE_VALLEY = ITEMS.registerSimpleItem(
            "ground_lily_of_the_valley", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_TORCHFLOWER = ITEMS.registerSimpleItem(
            "ground_torchflower", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_BLAZE_POWDER = ITEMS.registerSimpleItem(
            "ground_blaze_powder", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_GHAST_TEAR = ITEMS.registerSimpleItem(
            "ground_ghast_tear", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_WITHER_ROSE = ITEMS.registerSimpleItem(
            "ground_wither_rose", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_ALLIUM = ITEMS.registerSimpleItem(
            "ground_allium", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_SUNFLOWER = ITEMS.registerSimpleItem(
            "ground_sunflower", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_CORNFLOWER = ITEMS.registerSimpleItem(
            "ground_cornflower", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_AZURE_BLUET = ITEMS.registerSimpleItem(
            "ground_azure_bluet", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_OXEYE_DAISY = ITEMS.registerSimpleItem(
            "ground_oxeye_daisy", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_RED_TULIP = ITEMS.registerSimpleItem(
            "ground_red_tulip", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_ORANGE_TULIP = ITEMS.registerSimpleItem(
            "ground_orange_tulip", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_WHITE_TULIP = ITEMS.registerSimpleItem(
            "ground_white_tulip", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_PINK_TULIP = ITEMS.registerSimpleItem(
            "ground_pink_tulip", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_LILAC = ITEMS.registerSimpleItem(
            "ground_lilac", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_PEONY = ITEMS.registerSimpleItem(
            "ground_peony", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_ROSE_BUSH = ITEMS.registerSimpleItem(
            "ground_rose_bush", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_CARROT = ITEMS.registerSimpleItem(
            "ground_carrot", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_BEETROOT = ITEMS.registerSimpleItem(
            "ground_beetroot", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_POTATO = ITEMS.registerSimpleItem(
            "ground_potato", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_COCOA_BEANS = ITEMS.registerSimpleItem(
            "ground_cocoa_beans", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_SPORE_BLOSSOM = ITEMS.registerSimpleItem(
            "ground_spore_blossom", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_PRISMARINE_SHARD = ITEMS.registerSimpleItem(
            "ground_prismarine_shard", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_ENDER_PEARL = ITEMS.registerSimpleItem(
            "ground_ender_pearl", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_PINK_PETALS = ITEMS.registerSimpleItem(
            "ground_pink_petals", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_GUNPOWDER = ITEMS.registerSimpleItem(
            "ground_gunpowder", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_REDSTONE = ITEMS.registerSimpleItem(
            "ground_redstone", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_MOSS = ITEMS.registerSimpleItem(
            "ground_moss", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_DRAGON_EGG = ITEMS.registerSimpleItem(
            "ground_dragon_egg", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_AMETHYST = ITEMS.registerSimpleItem(
            "ground_amethyst", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_WEEPING_VINES = ITEMS.registerSimpleItem(
            "ground_weeping_vines", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_PUMPKIN = ITEMS.registerSimpleItem(
            "ground_pumpkin", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_COAL = ITEMS.registerSimpleItem(
            "ground_coal", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_FIRE_CHARGE = ITEMS.registerSimpleItem(
            "ground_fire_charge", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_BONE_MEAL = ITEMS.registerSimpleItem(
            "ground_bone_meal", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_WIND_CHARGE = ITEMS.registerSimpleItem(
            "ground_wind_charge", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_TOTEM_OF_UNDYING = ITEMS.registerSimpleItem(
            "ground_totem_of_undying", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_TNT = ITEMS.registerSimpleItem(
            "ground_tnt", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_END_CRYSTAL = ITEMS.registerSimpleItem(
            "ground_end_crystal", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_IRON_NUGGET = ITEMS.registerSimpleItem(
            "ground_iron_nugget", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_GOLD_NUGGET = ITEMS.registerSimpleItem(
            "ground_gold_nugget", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_NETHERITE = ITEMS.registerSimpleItem(
            "ground_netherite", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_NETHER_STAR = ITEMS.registerSimpleItem(
            "ground_nether_star", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_ECHO_SHARD = ITEMS.registerSimpleItem(
            "ground_echo_shard", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_GOLDEN_CARROT = ITEMS.registerSimpleItem(
            "ground_golden_carrot", new Item.Properties().stacksTo(64));

    // ── Замес ────────────────────────────────────────────────────────────────
    public static final DeferredItem<MixtureItem> MIXTURE = ITEMS.register(
            "mixture", () -> new MixtureItem(new Item.Properties().stacksTo(1).durability(3)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
