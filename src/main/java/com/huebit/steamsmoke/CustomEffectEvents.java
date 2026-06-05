package com.huebit.steamsmoke;

import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;

@SuppressWarnings({"unused", "resource"})
@EventBusSubscriber(modid = SteamSmoke.MODID)
public class CustomEffectEvents {

    // ── Spine Aura: при ударе в ближнем бою — яд атакующему ─────────────────
    @SubscribeEvent
    public static void onSpineAura(LivingDamageEvent.Pre event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!player.hasEffect(ModMobEffects.SPINE_AURA)) return;
        if (!(event.getSource().getDirectEntity() instanceof LivingEntity attacker)) return;
        if (attacker.distanceToSqr(player) > 16) return;
        if (event.getSource().is(DamageTypes.THORNS)) return;
        attacker.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 1));
    }

    // ── Iron Skin: отражает 25% урона назад ──────────────────────────────────
    @SubscribeEvent
    public static void onIronSkin(LivingDamageEvent.Pre event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!player.hasEffect(ModMobEffects.IRON_SKIN)) return;
        if (!(event.getSource().getDirectEntity() instanceof LivingEntity attacker)) return;
        if (event.getSource().is(DamageTypes.THORNS)) return;
        float reflected = event.getNewDamage() * 0.25f;
        if (reflected > 0) {
            attacker.hurt(player.damageSources().thorns(player), reflected);
        }
    }

    // ── Fire Trail: горящие следы при ходьбе ──────────────────────────────────
    @SubscribeEvent
    public static void onFireTrail(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;
        if (!player.hasEffect(ModMobEffects.FIRE_TRAIL)) return;
        if (!player.onGround()) return;
        if (player.tickCount % 5 != 0) return;

        BlockPos feet = player.blockPosition();
        if (player.level().isEmptyBlock(feet)) {
            player.level().setBlock(feet, Blocks.FIRE.defaultBlockState(), 3);
        }
    }

    // ── Soul Sight: мобы в радиусе получают Glowing ───────────────────────────
    @SubscribeEvent
    public static void onSoulSight(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;
        if (!player.hasEffect(ModMobEffects.SOUL_SIGHT)) return;
        if (player.tickCount % 20 != 0) return;

        List<Mob> nearby = player.level().getEntitiesOfClass(
                Mob.class,
                player.getBoundingBox().inflate(20),
                mob -> true
        );
        for (Mob mob : nearby) {
            mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, 40, 0, false, false));
        }
    }

    // ── Gold Pact: пиглины не выбирают игрока как цель ───────────────────────
    @SubscribeEvent
    public static void onGoldPact(LivingChangeTargetEvent event) {
        if (!(event.getNewAboutToBeSetTarget() instanceof Player player)) return;
        if (!player.hasEffect(ModMobEffects.GOLD_PACT)) return;
        LivingEntity mob = event.getEntity();
        if (mob instanceof Piglin || mob instanceof PiglinBrute || mob instanceof ZombifiedPiglin) {
            event.setCanceled(true);
        }
    }
}
