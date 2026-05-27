package com.huebit.steamsmoke;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MixtureItem extends Item {

    public static final String INGREDIENTS_TAG = "MixtureIngredients";

    public MixtureItem(Properties properties) {
        super(properties);
    }

    /**
     * Создаёт стак замеса с заданным списком ингредиентов через CustomData компонент.
     */
    public static ItemStack createMixture(List<String> ingredients) {
        ItemStack stack = new ItemStack(ModItems.MIXTURE.get());

        ListTag list = new ListTag();
        for (String ing : ingredients) {
            list.add(StringTag.valueOf(ing));
        }

        CompoundTag tag = new CompoundTag();
        tag.put(INGREDIENTS_TAG, list);
        CustomData.update(DataComponents.CUSTOM_DATA, stack, currentTag -> currentTag.merge(tag));

        return stack;
    }

    /**
     * Возвращает список ингредиентов из компонента данных.
     */
    public static List<String> getIngredients(ItemStack stack) {
        List<String> result = new ArrayList<>();

        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return result;

        CompoundTag tag = customData.copyTag();
        if (!tag.contains(INGREDIENTS_TAG, Tag.TAG_LIST)) return result;

        ListTag list = tag.getList(INGREDIENTS_TAG, Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) {
            result.add(list.getString(i));
        }
        return result;
    }

    // ================= ДОБАВЛЕННЫЕ МЕТОДЫ ДЛЯ СИСТЕМЫ ТИНТОВАНИЯ =================

    /**
     * Возвращает фиксированный HEX-цвет для конкретного измельчённого ингредиента.
     */
    public static int getIngredientColor(String ingredientId) {
        return switch (ingredientId) {
            case "ground_tobacco" -> 0x4E9A24;       // Твой фирменный кактусовый зелёный
            case "ground_apple" -> 0xFF2222;         // Насыщенный яблочный красный
            case "ground_sweet_berries" -> 0x9E0031; // Бордовый для ягод
            case "ground_sugar" -> 0xFFFFFF;         // Белый для сахара
            case "ground_honeycomb" -> 0xFFB300;     // Янтарно-медовый
            default -> 0x543D2B;                     // Дефолтный табачный коричневый
        };
    }

    /**
     * Рассчитывает средний цвет бленда на основе всех замешанных ингредиентов.
     */
    public static int getBlendColor(ItemStack stack) {
        List<String> ingredients = getIngredients(stack);
        if (ingredients.isEmpty()) {
            return 0x543D2B; // Цвет по умолчанию для пустого брикета
        }

        int r = 0, g = 0, b = 0;
        for (String ing : ingredients) {
            int color = getIngredientColor(ing);
            r += (color >> 16) & 0xFF;
            g += (color >> 8) & 0xFF;
            b += color & 0xFF;
        }

        // Высчитываем среднее арифметическое RGB каналов
        r /= ingredients.size();
        g /= ingredients.size();
        b /= ingredients.size();

        return (r << 16) | (g << 8) | b;
    }
    // =============================================================================

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
                                @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        List<String> ingredients = getIngredients(stack);
        if (ingredients.isEmpty()) {
            tooltipComponents.add(Component.translatable("item.steamsmoke.mixture.empty")
                    .withStyle(ChatFormatting.GRAY));
        } else {
            tooltipComponents.add(Component.translatable("item.steamsmoke.mixture.contents")
                    .withStyle(ChatFormatting.GOLD));
            for (String ing : ingredients) {
                tooltipComponents.add(Component.literal("  • ")
                        .append(Component.translatable("item.steamsmoke." + ing))
                        .withStyle(ChatFormatting.YELLOW));
            }
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}