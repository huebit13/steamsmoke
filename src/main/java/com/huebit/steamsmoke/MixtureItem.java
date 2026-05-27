package com.huebit.steamsmoke;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;

public class MixtureItem extends Item {

    public static final String INGREDIENTS_TAG = "MixtureIngredients";

    public MixtureItem(Properties properties) {
        super(properties);
    }

    /**
     * Создаёт стак замеса с заданным списком ингредиентов.
     * Каждый ингредиент — строка-идентификатор (например "ground_tobacco").
     */
    public static ItemStack createMixture(List<String> ingredients) {
        ItemStack stack = new ItemStack(ModItems.MIXTURE.get());
        ListTag list = new ListTag();
        for (String ing : ingredients) {
            list.add(StringTag.valueOf(ing));
        }
        stack.getOrCreateTag().put(INGREDIENTS_TAG, list);
        return stack;
    }

    /**
     * Возвращает список ингредиентов из NBT.
     */
    public static List<String> getIngredients(ItemStack stack) {
        List<String> result = new ArrayList<>();
        if (!stack.hasTag()) return result;
        ListTag list = stack.getOrCreateTag().getList(INGREDIENTS_TAG, Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) {
            result.add(list.getString(i));
        }
        return result;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
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