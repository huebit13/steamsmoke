package com.huebit.steamsmoke;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = SteamSmoke.MODID, value = Dist.CLIENT)
public class SteamSmokeTooltips {

    // Palette of signature colors for ingredient linking
    private static final int[] PALETTE = {
        0xFFA552, 0x52C5FF, 0xFF7EB3, 0x7BFF72,
        0xBD79FF, 0xFFD95C, 0x52FFDB, 0xFF6B6B
    };

    static TextColor getIngredientColor(String ingredient) {
        return TextColor.fromRgb(PALETTE[Math.abs(ingredient.hashCode()) % PALETTE.length]);
    }

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        var key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (!SteamSmoke.MODID.equals(key.getNamespace())) return;

        String id = key.getPath();
        HookahSmokingRecipes.IngredientEffects base = HookahSmokingRecipes.getBaseEffects(id);
        if (base.isEmpty()) return;

        List<Component> tip = event.getToolTip();
        tip.add(thinSep());
        tip.add(header("◈ Эффект при курении"));

        if (SmokingDiscoveries.isDiscovered(id)) {
            for (MobEffectInstance eff : base.positive()) {
                tip.add(effectLine(eff));
            }
            for (MobEffectInstance eff : base.negative()) {
                tip.add(negativeEffectLine(eff));
            }
        } else {
            tip.add(unknownLine(null));
            tip.add(Component.literal("  Закурите в кальяне, чтобы узнать")
                    .withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY).withItalic(true)));
        }

        tip.add(thinSep());
    }

    // ── Shared helpers ───────────────────────────────────────────────────────

    static Component thinSep() {
        return Component.literal("──────────────────").withStyle(ChatFormatting.DARK_GRAY);
    }

    static Component thickSep() {
        return Component.literal("══════════════════").withStyle(ChatFormatting.DARK_GRAY);
    }

    static Component header(String text) {
        return Component.literal(text).withStyle(ChatFormatting.GOLD);
    }

    // Standard effect line (individual ingredient tooltips)
    static Component effectLine(MobEffectInstance eff) {
        return effectLine(eff, false);
    }

    static Component effectLine(MobEffectInstance eff, boolean isSynergy) {
        TextColor color = TextColor.fromRgb(eff.getEffect().value().getColor());
        MutableComponent line = Component.literal("  ◆ ")
                .withStyle(ChatFormatting.DARK_GRAY)
                .append(buildEffectName(eff).withStyle(Style.EMPTY.withColor(color)))
                .append(durationBar(eff.getDuration()))
                .append(Component.literal(formatDuration(eff.getDuration())).withStyle(ChatFormatting.GRAY));
        if (isSynergy) line.append(Component.literal("  ✦").withStyle(ChatFormatting.LIGHT_PURPLE));
        return line;
    }

    // Mixture effect line: signature-colored bullet links to its ingredient
    static Component effectLineColored(MobEffectInstance eff, boolean isSynergy, TextColor sigColor) {
        TextColor potionColor = TextColor.fromRgb(eff.getEffect().value().getColor());
        MutableComponent line = Component.literal("  ◆ ")
                .withStyle(Style.EMPTY.withColor(sigColor))
                .append(buildEffectName(eff).withStyle(Style.EMPTY.withColor(potionColor)))
                .append(durationBar(eff.getDuration()))
                .append(Component.literal(formatDuration(eff.getDuration())).withStyle(ChatFormatting.GRAY));
        if (isSynergy) line.append(Component.literal("  ✦").withStyle(ChatFormatting.LIGHT_PURPLE));
        return line;
    }

    // Negative effect line (red bullet)
    static Component negativeEffectLine(MobEffectInstance eff) {
        TextColor color = TextColor.fromRgb(eff.getEffect().value().getColor());
        return Component.literal("  ◆ ")
                .withStyle(ChatFormatting.DARK_RED)
                .append(buildEffectName(eff).withStyle(Style.EMPTY.withColor(color)))
                .append(durationBar(eff.getDuration()))
                .append(Component.literal(formatDuration(eff.getDuration())).withStyle(ChatFormatting.GRAY));
    }

    static Component negativeEffectLineColored(MobEffectInstance eff, boolean isSynergy, TextColor sigColor) {
        TextColor potionColor = TextColor.fromRgb(eff.getEffect().value().getColor());
        MutableComponent line = Component.literal("  ◆ ")
                .withStyle(ChatFormatting.DARK_RED)
                .append(buildEffectName(eff).withStyle(Style.EMPTY.withColor(potionColor)))
                .append(durationBar(eff.getDuration()))
                .append(Component.literal(formatDuration(eff.getDuration())).withStyle(ChatFormatting.GRAY));
        if (isSynergy) line.append(Component.literal("  ✦").withStyle(ChatFormatting.LIGHT_PURPLE));
        return line;
    }

    // Unknown/undiscovered effect line
    static Component unknownLine(TextColor sigColor) {
        Style bulletStyle = sigColor != null
                ? Style.EMPTY.withColor(sigColor)
                : Style.EMPTY.withColor(ChatFormatting.DARK_GRAY);
        return Component.literal("  ◆ ")
                .withStyle(bulletStyle)
                .append(Component.literal("???????   ·   ?:??")
                        .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x6B4C8F)).withItalic(true)));
    }

    // Duration bar: ▰▰▰▰▰▱▱▱ scaled to D_UTILITY (5 min max)
    static Component durationBar(int ticks) {
        int filled = Math.max(1, Math.min(8, Math.round(8f * ticks / 6000f)));
        String bar = "▰".repeat(filled) + "▱".repeat(8 - filled);
        return Component.literal("  " + bar + "  ").withStyle(ChatFormatting.DARK_GRAY);
    }

    private static MutableComponent buildEffectName(MobEffectInstance eff) {
        MutableComponent name = Component.translatable(eff.getEffect().value().getDescriptionId());
        if (eff.getAmplifier() > 0) {
            name = Component.translatable("potion.withAmplifier", name,
                    Component.translatable("potion.potency." + eff.getAmplifier()));
        }
        return name;
    }

    static String formatDuration(int ticks) {
        int sec = ticks / 20;
        return (sec / 60) + ":" + String.format("%02d", sec % 60);
    }
}
