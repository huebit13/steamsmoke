package com.huebit.steamsmoke;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffectInstance;
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

    public static int getFlecksVariant(ItemStack stack) {
        List<String> ingredients = getIngredients(stack);
        if (ingredients.isEmpty()) return 0;
        List<String> sorted = new ArrayList<>(ingredients);
        Collections.sort(sorted);
        int hash = sorted.hashCode();
        return (Math.abs(hash) % FLECKS_VARIANTS) + 1;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
                                @NotNull List<Component> tip, @NotNull TooltipFlag flag) {
        List<String> ingredients = getIngredients(stack);

        if (ingredients.isEmpty()) {
            tip.add(Component.translatable("item.steamsmoke.mixture.empty")
                    .withStyle(ChatFormatting.GRAY));
            return;
        }

        // ── Шапка ────────────────────────────────────────────────────────────
        tip.add(SteamSmokeTooltips.thickSep());

        // ── Состав (ингредиенты + их эффекты) ────────────────────────────────
        tip.add(SteamSmokeTooltips.header("◉ Состав"));
        for (String ing : ingredients) {
            tip.add(Component.literal("  ◦ ")
                    .withStyle(ChatFormatting.DARK_GRAY)
                    .append(Component.translatable("item.steamsmoke." + ing)
                            .withStyle(ChatFormatting.YELLOW)));

            List<MobEffectInstance> ingEffects = HookahSmokingRecipes.getBaseEffects(ing);
            if (!ingEffects.isEmpty()) {
                if (SmokingDiscoveries.isDiscovered(ing)) {
                    for (MobEffectInstance eff : ingEffects) {
                        tip.add(SteamSmokeTooltips.effectLineNested(eff));
                    }
                } else {
                    tip.add(Component.literal("    ◆ ???")
                            .withStyle(ChatFormatting.DARK_GRAY));
                }
            }
        }

        // ── Синергии ──────────────────────────────────────────────────────────
        List<HookahSmokingRecipes.SynergyInfo> synergies =
                HookahSmokingRecipes.getActiveSynergies(ingredients, HookahFluidType.WATER);
        if (!synergies.isEmpty()) {
            tip.add(Component.empty());
            for (HookahSmokingRecipes.SynergyInfo syn : synergies) {
                tip.add(Component.literal("✦ ")
                        .withStyle(ChatFormatting.LIGHT_PURPLE)
                        .append(Component.literal(syn.name())
                                .withStyle(ChatFormatting.DARK_PURPLE)));

                boolean allKnown = syn.required().stream().allMatch(SmokingDiscoveries::isDiscovered);
                if (allKnown) {
                    for (MobEffectInstance eff : syn.bonus()) {
                        tip.add(SteamSmokeTooltips.effectLineNested(eff, true));
                    }
                } else {
                    tip.add(Component.literal("    ◆ ???")
                            .withStyle(ChatFormatting.DARK_GRAY));
                }
            }
        }

        // ── Использования ─────────────────────────────────────────────────────
        tip.add(Component.empty());
        int remaining = stack.getMaxDamage() - stack.getDamageValue();
        int max = stack.getMaxDamage();
        MutableComponent usesLine = Component.literal("  ").withStyle(ChatFormatting.GRAY);
        for (int i = 0; i < max; i++) {
            usesLine.append(Component.literal("◆")
                    .withStyle(i < remaining ? ChatFormatting.GREEN : ChatFormatting.DARK_GRAY));
        }
        usesLine.append(Component.literal("  " + remaining + "/" + max + " использования")
                .withStyle(ChatFormatting.DARK_GRAY));
        tip.add(usesLine);

        // ── Подсказка про жидкость ────────────────────────────────────────────
        tip.add(Component.literal("  Жидкость изменяет силу и длительность")
                .withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY).withItalic(true)));

        // ── Подвал ────────────────────────────────────────────────────────────
        tip.add(SteamSmokeTooltips.thickSep());
    }
}
