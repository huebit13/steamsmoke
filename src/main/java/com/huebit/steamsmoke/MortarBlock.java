package com.huebit.steamsmoke;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MortarBlock extends Block implements EntityBlock {

    // Форма ступки — приземистый цилиндр (аппроксимация боксом)
    private static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 7, 13);

    public MortarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MortarBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
                                              BlockPos pos, Player player, InteractionHand hand,
                                              BlockHitResult hitResult) {

        if (!(level.getBlockEntity(pos) instanceof MortarBlockEntity mortar)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // ── Пустая рука → забрать содержимое ──────────────────────────────
        if (stack.isEmpty()) {
            if (!mortar.isEmpty()) {
                if (!level.isClientSide) {
                    List<ItemStack> contents = mortar.removeAll();
                    for (ItemStack item : contents) {
                        if (!player.addItem(item)) {
                            // Если инвентарь полон — дроп на землю
                            ItemEntity entity = new ItemEntity(level,
                                    pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, item);
                            level.addFreshEntity(entity);
                        }
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.8f, 1.0f);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // ── Пестик → обработка содержимого ────────────────────────────────
        if (stack.is(ModItems.PESTLE.get())) {
            return handlePestle(mortar, stack, level, pos, player, hand);
        }

        // ── Любой другой предмет → положить в ступку ──────────────────────
        return handleAddItem(mortar, stack, level, pos, player, hand);
    }

    // ─────────────────────────────────────────────────────────────────────────

    private ItemInteractionResult handlePestle(MortarBlockEntity mortar, ItemStack pestle,
                                               Level level, BlockPos pos,
                                               Player player, InteractionHand hand) {
        if (mortar.isEmpty()) {
            if (!level.isClientSide) {
                player.displayClientMessage(
                        Component.translatable("block.steamsmoke.mortar.empty"), true);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        List<ItemStack> contents = mortar.getNonEmptyItems();
        int count = contents.size();

        if (!level.isClientSide) {
            // Один предмет → измельчить
            if (count == 1) {
                ItemStack single = contents.get(0);
                ItemStack ground = GrindingRecipes.getResult(single);

                if (ground.isEmpty()) {
                    player.displayClientMessage(
                            Component.translatable("block.steamsmoke.mortar.cannot_grind"), true);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }

                mortar.replaceWith(ground);
                damagePestle(pestle, player, hand);
                level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1.0f, 1.2f);
                spawnGrindParticles(level, pos);
                player.displayClientMessage(
                        Component.translatable("block.steamsmoke.mortar.ground"), true);

                // Несколько измельчённых → смешать в замес
            } else if (count >= 2) {
                // Проверяем что все предметы — измельчённые
                boolean allGround = contents.stream().allMatch(GrindingRecipes::isGround);

                if (!allGround) {
                    player.displayClientMessage(
                            Component.translatable("block.steamsmoke.mortar.not_all_ground"), true);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }

                // Обязательно должен быть измельчённый табак
                boolean hasTobacco = contents.stream()
                        .anyMatch(s -> s.is(ModItems.GROUND_TOBACCO.get()));

                if (!hasTobacco) {
                    player.displayClientMessage(
                            Component.translatable("block.steamsmoke.mortar.no_tobacco"), true);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }

                // Собираем список ингредиентов в замес
                List<String> ingredients = contents.stream()
                        .map(s -> s.getItem().builtInRegistryHolder().key().location().getPath())
                        .toList();

                ItemStack mixture = MixtureItem.createMixture(ingredients);
                mortar.removeAll();
                mortar.addItem(mixture);

                damagePestle(pestle, player, hand);
                level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1.0f, 0.8f);
                spawnMixParticles(level, pos);
                player.displayClientMessage(
                        Component.translatable("block.steamsmoke.mortar.mixed"), true);
            }
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    private ItemInteractionResult handleAddItem(MortarBlockEntity mortar, ItemStack stack,
                                                Level level, BlockPos pos,
                                                Player player, InteractionHand hand) {
        if (mortar.isFull()) {
            if (!level.isClientSide) {
                player.displayClientMessage(
                        Component.translatable("block.steamsmoke.mortar.full"), true);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (!level.isClientSide) {
            mortar.addItem(stack);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.6f, 0.8f);
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    // ─────────────────────────────────────────────────────────────────────────

    private void damagePestle(ItemStack pestle, Player player, InteractionHand hand) {
        pestle.hurtAndBreak(1, player, hand);
    }

    private void spawnGrindParticles(Level level, BlockPos pos) {
        for (int i = 0; i < 8; i++) {
            double x = pos.getX() + 0.3 + level.random.nextDouble() * 0.4;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.3 + level.random.nextDouble() * 0.4;
            level.addParticle(ParticleTypes.CRIT, x, y, z, 0, 0.05, 0);
        }
    }

    private void spawnMixParticles(Level level, BlockPos pos) {
        for (int i = 0; i < 12; i++) {
            double x = pos.getX() + 0.3 + level.random.nextDouble() * 0.4;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.3 + level.random.nextDouble() * 0.4;
            level.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0.1, 0);
        }
    }
}