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

    // ── Измельчённые — базовые ───────────────────────────────────────────────
    public static final DeferredItem<Item> GROUND_TOBACCO = ITEMS.registerSimpleItem(
            "ground_tobacco", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> GROUND_APPLE = ITEMS.registerSimpleItem(
            "ground_apple", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> GROUND_SUGAR = ITEMS.registerSimpleItem(
            "ground_sugar", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> GROUND_SWEET_BERRIES = ITEMS.registerSimpleItem(
            "ground_sweet_berries", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> GROUND_HONEYCOMB = ITEMS.registerSimpleItem(
            "ground_honeycomb", new Item.Properties().stacksTo(64));

    // ── Измельчённые — ранняя игра ───────────────────────────────────────────
    public static final DeferredItem<Item> GROUND_DANDELION = ITEMS.registerSimpleItem(
            "ground_dandelion", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> GROUND_POPPY = ITEMS.registerSimpleItem(
            "ground_poppy", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> GROUND_BROWN_MUSHROOM = ITEMS.registerSimpleItem(
            "ground_brown_mushroom", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> GROUND_RED_MUSHROOM = ITEMS.registerSimpleItem(
            "ground_red_mushroom", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> GROUND_KELP = ITEMS.registerSimpleItem(
            "ground_kelp", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> GROUND_MELON = ITEMS.registerSimpleItem(
            "ground_melon", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> GROUND_CACTUS = ITEMS.registerSimpleItem(
            "ground_cactus", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> GROUND_LILY_OF_THE_VALLEY = ITEMS.registerSimpleItem(
            "ground_lily_of_the_valley", new Item.Properties().stacksTo(64));

    // ── Измельчённые — средняя игра (Незер / особые) ─────────────────────────
    public static final DeferredItem<Item> GROUND_NETHER_WART = ITEMS.registerSimpleItem(
            "ground_nether_wart", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> GROUND_CRIMSON_FUNGUS = ITEMS.registerSimpleItem(
            "ground_crimson_fungus", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> GROUND_WARPED_FUNGUS = ITEMS.registerSimpleItem(
            "ground_warped_fungus", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> GROUND_CHORUS_FRUIT = ITEMS.registerSimpleItem(
            "ground_chorus_fruit", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> GROUND_GLOW_BERRIES = ITEMS.registerSimpleItem(
            "ground_glow_berries", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> GROUND_TORCHFLOWER = ITEMS.registerSimpleItem(
            "ground_torchflower", new Item.Properties().stacksTo(64));

    // ── Измельчённые — поздняя игра / редкие ─────────────────────────────────
    public static final DeferredItem<Item> GROUND_BLAZE_POWDER = ITEMS.registerSimpleItem(
            "ground_blaze_powder", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> GROUND_GHAST_TEAR = ITEMS.registerSimpleItem(
            "ground_ghast_tear", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> GROUND_PHANTOM_MEMBRANE = ITEMS.registerSimpleItem(
            "ground_phantom_membrane", new Item.Properties().stacksTo(64));

    public static final DeferredItem<Item> GROUND_WITHER_ROSE = ITEMS.registerSimpleItem(
            "ground_wither_rose", new Item.Properties().stacksTo(64));

    // ── Измельчённые — цветы ─────────────────────────────────────────────────
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

    // ── Измельчённые — еда и урожай ───────────────────────────────────────────
    public static final DeferredItem<Item> GROUND_CARROT = ITEMS.registerSimpleItem(
            "ground_carrot", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_BEETROOT = ITEMS.registerSimpleItem(
            "ground_beetroot", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_POTATO = ITEMS.registerSimpleItem(
            "ground_potato", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_COCOA_BEANS = ITEMS.registerSimpleItem(
            "ground_cocoa_beans", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_DRIED_KELP = ITEMS.registerSimpleItem(
            "ground_dried_kelp", new Item.Properties().stacksTo(64));

    // ── Измельчённые — надземный мир / разное ────────────────────────────────
    public static final DeferredItem<Item> GROUND_RABBIT_FOOT = ITEMS.registerSimpleItem(
            "ground_rabbit_foot", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_SLIMEBALL = ITEMS.registerSimpleItem(
            "ground_slimeball", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_SPORE_BLOSSOM = ITEMS.registerSimpleItem(
            "ground_spore_blossom", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_INK_SAC = ITEMS.registerSimpleItem(
            "ground_ink_sac", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_SEA_PICKLE = ITEMS.registerSimpleItem(
            "ground_sea_pickle", new Item.Properties().stacksTo(64));

    // ── Измельчённые — средняя игра / вода / Незер ───────────────────────────
    public static final DeferredItem<Item> GROUND_MAGMA_CREAM = ITEMS.registerSimpleItem(
            "ground_magma_cream", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_FERMENTED_SPIDER_EYE = ITEMS.registerSimpleItem(
            "ground_fermented_spider_eye", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_PRISMARINE_SHARD = ITEMS.registerSimpleItem(
            "ground_prismarine_shard", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_SPIDER_EYE = ITEMS.registerSimpleItem(
            "ground_spider_eye", new Item.Properties().stacksTo(64));

    // ── Измельчённые — поздняя игра / Край ───────────────────────────────────
    public static final DeferredItem<Item> GROUND_ENDER_PEARL = ITEMS.registerSimpleItem(
            "ground_ender_pearl", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_DRAGON_BREATH = ITEMS.registerSimpleItem(
            "ground_dragon_breath", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_BAMBOO = ITEMS.registerSimpleItem(
            "ground_bamboo", new Item.Properties().stacksTo(64));
    public static final DeferredItem<Item> GROUND_PINK_PETALS = ITEMS.registerSimpleItem(
            "ground_pink_petals", new Item.Properties().stacksTo(64));

    // ── Замес ────────────────────────────────────────────────────────────────
    public static final DeferredItem<MixtureItem> MIXTURE = ITEMS.register(
            "mixture", () -> new MixtureItem(new Item.Properties().stacksTo(1).durability(3)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}