package com.huebit.steamsmoke;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MortarBlock extends Block implements EntityBlock {

    private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 6, 12);

    public MortarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new MortarBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof MortarBlockEntity mortar) {
                for (ItemStack stack : mortar.getNonEmptyItems()) {
                    popResource(level, pos, stack);
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level,
                                                       @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand,
                                                       @NotNull BlockHitResult hitResult) {

        if (!(level.getBlockEntity(pos) instanceof MortarBlockEntity mortar)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (stack.isEmpty()) {
            if (!mortar.isEmpty()) {
                if (!level.isClientSide) {
                    List<ItemStack> contents = mortar.removeAll();
                    for (ItemStack item : contents) {
                        if (!player.addItem(item)) {
                            ItemEntity entity = new ItemEntity(level,
                                    pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, item);
                            level.addFreshEntity(entity);
                        }
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.8f, 1.0f);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (stack.is(ModItems.PESTLE.get())) {
            return handlePestle(mortar, stack, level, pos, player, hand);
        }

        return handleAddItem(mortar, stack, level, pos, player);
    }

    private ItemInteractionResult handlePestle(MortarBlockEntity mortar, ItemStack pestle,
                                               Level level, BlockPos pos,
                                               Player player, InteractionHand hand) {
        if (mortar.isEmpty()) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("block.steamsmoke.mortar.empty"), true);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        List<ItemStack> contents = mortar.getNonEmptyItems();
        int count = contents.size();

        if (!level.isClientSide) {
            if (count == 1) {
                ItemStack single = contents.getFirst();
                ItemStack ground = GrindingRecipes.getResult(single);

                if (ground.isEmpty()) {
                    player.displayClientMessage(Component.translatable("block.steamsmoke.mortar.cannot_grind"), true);
                    return ItemInteractionResult.sidedSuccess(false);
                }

                mortar.replaceWith(ground);
                damagePestle(pestle, player, hand);
                level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1.0f, 1.2f);
                spawnGrindParticles(level, pos);
                player.displayClientMessage(Component.translatable("block.steamsmoke.mortar.ground"), true);

            } else {
                boolean allGround = contents.stream().allMatch(GrindingRecipes::isGround);

                if (!allGround) {
                    player.displayClientMessage(Component.translatable("block.steamsmoke.mortar.not_all_ground"), true);
                    return ItemInteractionResult.sidedSuccess(false);
                }

                boolean hasTobacco = contents.stream().anyMatch(s -> s.is(ModItems.DRIED_GROUND_TOBACCO.get()));

                if (!hasTobacco) {
                    player.displayClientMessage(Component.translatable("block.steamsmoke.mortar.no_tobacco"), true);
                    return ItemInteractionResult.sidedSuccess(false);
                }

                List<String> ingredients = contents.stream()
                        .map(s -> s.getItemHolder().unwrapKey()
                                .map(key -> key.location().getPath())
                                .orElse(""))
                        .filter(path -> !path.isEmpty())
                        .toList();

                ItemStack mixture = MixtureItem.createMixture(ingredients);
                mortar.removeAll();
                mortar.addItem(mixture);

                damagePestle(pestle, player, hand);
                level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1.0f, 0.6f);
                spawnMixParticles(level, pos);
                player.displayClientMessage(Component.translatable("block.steamsmoke.mortar.mixed"), true);
            }
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    private ItemInteractionResult handleAddItem(MortarBlockEntity mortar, ItemStack stack,
                                                Level level, BlockPos pos, Player player) {
        // Нельзя класть mixture обратно в ступку// MortarBlock.java
        //private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 6, 12);
        if (stack.is(ModItems.MIXTURE.get())) {
            if (!level.isClientSide) {
                player.displayClientMessage(
                        Component.translatable("block.steamsmoke.mortar.no_mixture"), true);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (mortar.isFull()) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("block.steamsmoke.mortar.full"), true);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (!level.isClientSide) {
            mortar.addItem(stack);
            if (!player.isCreative()) stack.shrink(1);
            level.playSound(null, pos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 0.5f, 1.4f);
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    private void damagePestle(ItemStack pestle, Player player, InteractionHand hand) {
        EquipmentSlot slot = (hand == InteractionHand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
        pestle.hurtAndBreak(1, player, slot);
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