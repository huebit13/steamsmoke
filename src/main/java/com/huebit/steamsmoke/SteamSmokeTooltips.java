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

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        var key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (!SteamSmoke.MODID.equals(key.getNamespace())) return;

        String id = key.getPath();
        List<MobEffectInstance> base = HookahSmokingRecipes.getBaseEffects(id);
        if (base.isEmpty()) return;

        List<Component> tip = event.getToolTip();
        tip.add(thinSep());
        tip.add(header("◈ Эффект при курении"));

        if (SmokingDiscoveries.isDiscovered(id)) {
            for (MobEffectInstance eff : base) {
                tip.add(effectLine(eff));
            }
        } else {
            tip.add(Component.literal("  ◆ ???")
                    .withStyle(ChatFormatting.DARK_GRAY));
            tip.add(Component.literal("  Закурите в кальяне, чтобы узнать")
                    .withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY).withItalic(true)));
        }

        tip.add(thinSep());
    }

    // ── Shared helpers (used by MixtureItem too) ─────────────────────────────

    static Component thinSep() {
        return Component.literal("──────────────────").withStyle(ChatFormatting.DARK_GRAY);
    }

    static Component thickSep() {
        return Component.literal("══════════════════").withStyle(ChatFormatting.DARK_GRAY);
    }

    static Component header(String text) {
        return Component.literal(text).withStyle(ChatFormatting.GOLD);
    }

    static Component effectLine(MobEffectInstance eff) {
        return effectLine(eff, false);
    }

    static Component effectLine(MobEffectInstance eff, boolean isSynergy) {
        TextColor color = TextColor.fromRgb(eff.getEffect().value().getColor());
        String duration = formatDuration(eff.getDuration());

        MutableComponent name = Component.translatable(eff.getEffect().value().getDescriptionId());
        if (eff.getAmplifier() > 0) {
            name = Component.translatable("potion.withAmplifier", name,
                    Component.translatable("potion.potency." + eff.getAmplifier()));
        }

        MutableComponent line = Component.literal("  ◆ ")
                .withStyle(ChatFormatting.DARK_GRAY)
                .append(name.withStyle(Style.EMPTY.withColor(color)))
                .append(Component.literal("   · " + duration).withStyle(ChatFormatting.GRAY));

        if (isSynergy) {
            line.append(Component.literal("  ✦").withStyle(ChatFormatting.LIGHT_PURPLE));
        }

        return line;
    }

    static String formatDuration(int ticks) {
        int sec = ticks / 20;
        return (sec / 60) + ":" + String.format("%02d", sec % 60);
    }
}
