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
import java.util.Collections;
import java.util.List;

public class MixtureItem extends Item {

    public static final String INGREDIENTS_TAG = "MixtureIngredients";
    public static final int FLECKS_VARIANTS = 16;

    public MixtureItem(Properties properties) {
        super(properties);
    }

    public static ItemStack createMixture(List<String> ingredients) {
        ItemStack stack = new ItemStack(ModItems.MIXTURE.get());
        ListTag list = new ListTag();
        for (String ing : ingredients) list.add(StringTag.valueOf(ing));
        CompoundTag tag = new CompoundTag();
        tag.put(INGREDIENTS_TAG, list);
        CustomData.update(DataComponents.CUSTOM_DATA, stack, currentTag -> currentTag.merge(tag));
        return stack;
    }

    public static List<String> getIngredients(ItemStack stack) {
        List<String> result = new ArrayList<>();
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return result;
        CompoundTag tag = customData.copyTag();
        if (!tag.contains(INGREDIENTS_TAG, Tag.TAG_LIST)) return result;
        ListTag list = tag.getList(INGREDIENTS_TAG, Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) result.add(list.getString(i));
        return result;
    }

    /**
     * Возвращает номер flecks-варианта (1..FLECKS_VARIANTS) для данного стака,
     * или 0 если замес пустой. Хэш считается по отсортированному списку —
     * порядок добавления не влияет, результат стабилен для одинакового состава.
     */
    public static int getFlecksVariant(ItemStack stack) {
        List<String> ingredients = getIngredients(stack);
        if (ingredients.isEmpty()) return 0;
        List<String> sorted = new ArrayList<>(ingredients);
        Collections.sort(sorted);
        int hash = sorted.hashCode();
        return (Math.abs(hash) % FLECKS_VARIANTS) + 1; // 1..16
    }

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