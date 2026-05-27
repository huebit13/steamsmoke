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

    // ── Измельчённые ─────────────────────────────────────────────────────────
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

    // ── Замес ────────────────────────────────────────────────────────────────
    public static final DeferredItem<MixtureItem> MIXTURE = ITEMS.register(
            "mixture", () -> new MixtureItem(new Item.Properties().stacksTo(16)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}