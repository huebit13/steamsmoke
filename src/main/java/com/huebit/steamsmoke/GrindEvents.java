package com.huebit.steamsmoke;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class GrindEvents {

    @FunctionalInterface
    interface GrindEffect {
        // true = предмет уничтожен, не давать результат помола
        boolean apply(Level level, BlockPos pos, Player player);
    }

    private record EventEntry(double chance, GrindEffect effect) {}

    private static final Map<Item, EventEntry> EVENTS = new HashMap<>();

    static {
        // ── Взрывоопасные ────────────────────────────────────────────────────
        register(Items.GUNPOWDER, 0.20, (level, pos, player) -> {
            explode(level, pos, 1.0f);
            return false; // порох всё равно перемалывается
        });

        register(Items.TNT, 0.30, (level, pos, player) -> {
            explode(level, pos, 2.5f);
            return true; // взорвался — результата нет
        });

        register(Items.END_CRYSTAL, 0.30, (level, pos, player) -> {
            explode(level, pos, 4.0f);
            spawnParticles(level, pos, ParticleTypes.END_ROD, 40);
            return true;
        });

        // ── Ветряной заряд ────────────────────────────────────────────────────
        register(Items.WIND_CHARGE, 0.30, (level, pos, player) -> {
            double angle = level.random.nextDouble() * Math.PI * 2;
            player.setDeltaMovement(Math.cos(angle) * 2.0, 0.5, Math.sin(angle) * 2.0);
            syncMotion(player);
            level.playSound(null, pos, SoundEvents.WIND_CHARGE_BURST.value(), SoundSource.BLOCKS, 1f, 1f);
            spawnParticles(level, pos, ParticleTypes.GUST, 25);
            return true; // заряд улетел
        });

        // ── Огонь ─────────────────────────────────────────────────────────────
        register(Items.BLAZE_POWDER, 1.0, (level, pos, player) -> {
            player.igniteForSeconds(3);
            spawnParticles(level, pos, ParticleTypes.FLAME, 15);
            return false;
        });

        register(Items.FIRE_CHARGE, 1.0, (level, pos, player) -> {
            double dx = (level.random.nextDouble() - 0.5) * 0.8;
            double dz = (level.random.nextDouble() - 0.5) * 0.8;
            SmallFireball fireball = new SmallFireball(level,
                    pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5,
                    new Vec3(dx, 0.2, dz));
            level.addFreshEntity(fireball);
            level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1f, 1f);
            return false;
        });

        // ── Магма ─────────────────────────────────────────────────────────────
        GrindEffect magmaEffect = (level, pos, player) -> {
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, 0));
            spawnParticles(level, pos, ParticleTypes.LAVA, 10);
            return false;
        };
        register(Items.MAGMA_CREAM, 1.0, magmaEffect);
        register(ModItems.DRIED_MAGMA_CREAM.get(), 1.0, magmaEffect);

        // ── Яды / дебаффы ─────────────────────────────────────────────────────
        register(Items.WITHER_ROSE, 0.25, (level, pos, player) -> {
            player.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 0));
            spawnParticles(level, pos, ParticleTypes.SMOKE, 12);
            return false;
        });

        GrindEffect spiderPoison = (level, pos, player) -> {
            player.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 0));
            return false;
        };
        register(Items.SPIDER_EYE, 0.30, spiderPoison);
        register(Items.FERMENTED_SPIDER_EYE, 0.30, spiderPoison);
        register(ModItems.DRIED_SPIDER_EYE.get(), 0.20, spiderPoison);
        register(ModItems.DRIED_FERMENTED_SPIDER_EYE.get(), 0.20, spiderPoison);

        GrindEffect pufferfishEffect = (level, pos, player) -> {
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0));
            if (level.random.nextFloat() < 0.40f) {
                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, 0));
            }
            return false;
        };
        register(Items.PUFFERFISH, 1.0, pufferfishEffect);
        register(ModItems.DRIED_PUFFERFISH.get(), 1.0, pufferfishEffect);

        // ── Телепортация ──────────────────────────────────────────────────────
        register(Items.ENDER_PEARL, 0.15, (level, pos, player) -> {
            double angle = level.random.nextDouble() * Math.PI * 2;
            double dist = 3 + level.random.nextDouble() * 2;
            player.teleportTo(player.getX() + Math.cos(angle) * dist, player.getY(), player.getZ() + Math.sin(angle) * dist);
            level.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1f, 1f);
            spawnParticles(level, pos, ParticleTypes.PORTAL, 15);
            return false;
        });

        register(Items.CHORUS_FRUIT, 1.0, (level, pos, player) -> {
            double angle = level.random.nextDouble() * Math.PI * 2;
            double dist = 2 + level.random.nextDouble() * 3;
            player.teleportTo(player.getX() + Math.cos(angle) * dist, player.getY(), player.getZ() + Math.sin(angle) * dist);
            level.playSound(null, pos, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1f, 1f);
            return false;
        });

        // ── Особые ────────────────────────────────────────────────────────────
        register(Items.ECHO_SHARD, 1.0, (level, pos, player) -> {
            player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200, 0));
            level.playSound(null, pos, SoundEvents.SCULK_SENSOR_BREAK, SoundSource.BLOCKS, 1f, 0.5f);
            spawnParticles(level, pos, ParticleTypes.SCULK_SOUL, 8);
            return false;
        });

        register(Items.NETHER_STAR, 1.0, (level, pos, player) -> {
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 0));
            spawnParticles(level, pos, ParticleTypes.END_ROD, 30);
            level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1f, 2f);
            return false;
        });

        register(Items.TOTEM_OF_UNDYING, 1.0, (level, pos, player) -> {
            if (player.getHealth() <= 6.0f) {
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1));
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
                spawnParticles(level, pos, ParticleTypes.TOTEM_OF_UNDYING, 30);
                level.playSound(null, pos, SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1f, 1f);
                return true;
            }
            return false; // молоть обычно
        });

        // ── Яйцо дракона ─────────────────────────────────────────────────────
        register(Items.DRAGON_EGG, 1.0, (level, pos, player) -> {
            // Слепота на 1 минуту
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 1200, 1));
            // Откид на ~10-15 блоков
            double angle = level.random.nextDouble() * Math.PI * 2;
            player.setDeltaMovement(Math.cos(angle) * 1.5, 0.8, Math.sin(angle) * 1.5);
            syncMotion(player);
            // Рёв дракона
            level.playSound(null, pos, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 5f, 0.8f);
            // Облако драконьего дыхания на 20 секунд
            AreaEffectCloud cloud = new AreaEffectCloud(level, pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5);
            cloud.setRadius(3.0f);
            cloud.setRadiusOnUse(-0.1f);
            cloud.setWaitTime(0);
            cloud.setDuration(400);
            cloud.setRadiusPerTick(-cloud.getRadius() / (float) cloud.getDuration());
            cloud.setParticle(ParticleTypes.DRAGON_BREATH);
            cloud.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0));
            level.addFreshEntity(cloud);
            spawnParticles(level, pos, ParticleTypes.DRAGON_BREATH, 60);
            return true;
        });

        // ── Слизь / пчёлы / рост ─────────────────────────────────────────────
        register(Items.HONEYCOMB, 1.0, (level, pos, player) -> {
            level.getEntitiesOfClass(Bee.class, new AABB(pos).inflate(10))
                 .forEach(bee -> bee.setTarget(player));
            level.playSound(null, pos, SoundEvents.BEE_STING, SoundSource.NEUTRAL, 1f, 1f);
            return false;
        });

        register(Items.SLIME_BALL, 1.0, (level, pos, player) -> {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 60, 2));
            spawnParticles(level, pos, ParticleTypes.ITEM_SLIME, 15);
            return false;
        });

        register(Items.BONE_MEAL, 1.0, (level, pos, player) -> {
            if (level instanceof ServerLevel serverLevel) {
                for (int bx = -2; bx <= 2; bx++) {
                    for (int bz = -2; bz <= 2; bz++) {
                        BlockPos target = pos.offset(bx, 0, bz);
                        var state = serverLevel.getBlockState(target);
                        if (state.getBlock() instanceof BonemealableBlock bonemealable
                                && bonemealable.isValidBonemealTarget(serverLevel, target, state)
                                && bonemealable.isBonemealSuccess(serverLevel, serverLevel.random, target, state)) {
                            bonemealable.performBonemeal(serverLevel, serverLevel.random, target, state);
                            serverLevel.levelEvent(1505, target, 0); // зелёные частицы как от костной муки
                        }
                    }
                }
            }
            return false;
        });
    }

    private static void register(Item item, double chance, GrindEffect effect) {
        EVENTS.put(item, new EventEntry(chance, effect));
    }

    /**
     * Возвращает true, если предмет был уничтожен (не давать результат помола).
     */
    public static boolean tryFire(Item item, Level level, BlockPos pos, Player player) {
        EventEntry entry = EVENTS.get(item);
        if (entry == null) return false;
        if (level.random.nextDouble() >= entry.chance()) return false;
        return entry.effect().apply(level, pos, player);
    }

    private static void explode(Level level, BlockPos pos, float power) {
        level.explode(null,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                power, Level.ExplosionInteraction.NONE);
    }

    private static void syncMotion(Player player) {
        if (player instanceof ServerPlayer sp) {
            sp.connection.send(new ClientboundSetEntityMotionPacket(sp));
        }
    }

    private static void spawnParticles(Level level, BlockPos pos, ParticleOptions type, int count) {
        for (int i = 0; i < count; i++) {
            double x = pos.getX() + 0.2 + level.random.nextDouble() * 0.6;
            double y = pos.getY() + 0.4 + level.random.nextDouble() * 0.6;
            double z = pos.getZ() + 0.2 + level.random.nextDouble() * 0.6;
            level.addParticle(type, x, y, z,
                    (level.random.nextDouble() - 0.5) * 0.4,
                    level.random.nextDouble() * 0.3,
                    (level.random.nextDouble() - 0.5) * 0.4);
        }
    }
}
