package com.huebit.steamsmoke;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

/**
 * Хранит рецепты измельчения: какой предмет во что превращается в ступке.
 * Легко расширять — просто добавь строку в static-блок.
 */
public class GrindingRecipes {

    // key = исходный предмет, value = результат измельчения
    private static final Map<Item, ItemStack> RECIPES = new HashMap<>();

    static {
        // Базовые рецепты
        register(ModItems.TOBACCO_LEAF.get(),   new ItemStack(ModItems.GROUND_TOBACCO.get()));
        register(Items.APPLE,                   new ItemStack(ModItems.GROUND_APPLE.get()));
        register(Items.SUGAR,                   new ItemStack(ModItems.GROUND_SUGAR.get()));
        register(Items.SWEET_BERRIES,           new ItemStack(ModItems.GROUND_SWEET_BERRIES.get()));
        register(Items.HONEYCOMB,               new ItemStack(ModItems.GROUND_HONEYCOMB.get()));
    }

    public static void register(Item input, ItemStack output) {
        RECIPES.put(input, output);
    }

    /**
     * Возвращает результат измельчения или ItemStack.EMPTY если рецепта нет.
     */
    public static ItemStack getResult(ItemStack input) {
        ItemStack result = RECIPES.get(input.getItem());
        return result != null ? result.copy() : ItemStack.EMPTY;
    }

    public static boolean canGrind(ItemStack stack) {
        return RECIPES.containsKey(stack.getItem());
    }

    /**
     * Проверяет, является ли предмет измельчённым (т.е. может участвовать в замесе).
     */
    public static boolean isGround(ItemStack stack) {
        Item item = stack.getItem();
        return item == ModItems.GROUND_TOBACCO.get()
                || item == ModItems.GROUND_APPLE.get()
                || item == ModItems.GROUND_SUGAR.get()
                || item == ModItems.GROUND_SWEET_BERRIES.get()
                || item == ModItems.GROUND_HONEYCOMB.get();
    }
}