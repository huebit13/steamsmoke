package com.huebit.steamsmoke;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HookahBlock extends Block implements EntityBlock {

    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(4, 0, 4, 12, 9, 12),   // колба
            Block.box(7, 9, 7, 9, 14, 9)      // шахта/трубка
    );

    public HookahBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HookahBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof HookahBlockEntity hookah) {
                if (hookah.hasMixture()) {
                    popResource(level, pos, hookah.getMixtureStack());
                }
                if (!hookah.getFluidType().isEmpty()) {
                    ItemStack bucket = getFilledBucket(hookah.getFluidType());
                    if (!bucket.isEmpty()) popResource(level, pos, bucket);
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (!(level.getBlockEntity(pos) instanceof HookahBlockEntity hookah)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (stack.is(Items.WATER_BUCKET)) return fillHookah(hookah, level, player, hand, HookahFluidType.WATER, Items.BUCKET.getDefaultInstance());
        if (stack.is(Items.LAVA_BUCKET))  return fillHookah(hookah, level, player, hand, HookahFluidType.LAVA,  Items.BUCKET.getDefaultInstance());
        if (stack.is(Items.MILK_BUCKET))  return fillHookah(hookah, level, player, hand, HookahFluidType.MILK,  Items.BUCKET.getDefaultInstance());

        if (stack.is(Items.BUCKET) && !hookah.getFluidType().isEmpty()) {
            if (!level.isClientSide) {
                ItemStack filled = getFilledBucket(hookah.getFluidType());
                hookah.setFluidType(HookahFluidType.EMPTY);
                if (!player.isCreative()) player.setItemInHand(hand, filled);
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (stack.is(ModItems.MIXTURE.get())) {
            if (!hookah.hasMixture()) {
                if (!level.isClientSide) {
                    hookah.setMixture(stack);
                    if (!player.isCreative()) stack.shrink(1);
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.8f, 0.6f);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            } else {
                if (!level.isClientSide) {
                    player.displayClientMessage(
                            Component.translatable("block.steamsmoke.hookah.already_loaded"), true);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof HookahBlockEntity hookah)) {
            return InteractionResult.PASS;
        }

        if (!hookah.hasMixture()) {
            if (!level.isClientSide) {
                player.displayClientMessage(
                        Component.translatable("block.steamsmoke.hookah.no_mixture"), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (hookah.getFluidType().isEmpty()) {
            if (!level.isClientSide) {
                player.displayClientMessage(
                        Component.translatable("block.steamsmoke.hookah.no_fluid"), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (!level.isClientSide) {
            smoke(hookah, level, pos, player);
        } else {
            spawnSmokeParticles(level, pos, hookah.getFluidType());
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private void smoke(HookahBlockEntity hookah, Level level, BlockPos pos, Player player) {
        List<String> ingredients = MixtureItem.getIngredients(hookah.getMixtureStack());
        HookahSmokingRecipes.IngredientEffects effects =
                HookahSmokingRecipes.getEffects(ingredients, hookah.getFluidType());

        SmokingDiscoveries.recordSmoke(player, ingredients);

        effects.all().forEach(player::addEffect);

        if (hookah.getFluidType() == HookahFluidType.LAVA) {
            player.setRemainingFireTicks(40);
        }

        level.playSound(null, pos, SoundEvents.HONEY_DRINK, SoundSource.PLAYERS, 1.0f, 0.8f);

        boolean alive = hookah.consumeUse();
        if (!alive) {
            player.displayClientMessage(
                    Component.translatable("block.steamsmoke.hookah.mixture_spent"), true);
        }
    }

    private void spawnSmokeParticles(Level level, BlockPos pos, HookahFluidType fluid) {
        for (int i = 0; i < 6; i++) {
            double x = pos.getX() + 0.4 + level.random.nextDouble() * 0.2;
            double y = pos.getY() + 1.1;
            double z = pos.getZ() + 0.4 + level.random.nextDouble() * 0.2;
            double vy = 0.05 + level.random.nextDouble() * 0.03;

            if (fluid == HookahFluidType.LAVA) {
                level.addParticle(ParticleTypes.LAVA, x, y, z, 0, vy, 0);
            } else {
                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 0, vy, 0);
            }
        }
    }

    private ItemInteractionResult fillHookah(HookahBlockEntity hookah, Level level,
                                             Player player, InteractionHand hand,
                                             HookahFluidType type, ItemStack returnStack) {
        if (!level.isClientSide) {
            if (!hookah.getFluidType().isEmpty() && hookah.getFluidType() != type) {
                ItemStack oldBucket = getFilledBucket(hookah.getFluidType());
                if (!player.addItem(oldBucket)) {
                    popResource(level, hookah.getBlockPos(), oldBucket);
                }
            }
            hookah.setFluidType(type);
            if (!player.isCreative()) player.setItemInHand(hand, returnStack);
            level.playSound(null, hookah.getBlockPos(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    private ItemStack getFilledBucket(HookahFluidType type) {
        return switch (type) {
            case WATER -> Items.WATER_BUCKET.getDefaultInstance();
            case LAVA  -> Items.LAVA_BUCKET.getDefaultInstance();
            case MILK  -> Items.MILK_BUCKET.getDefaultInstance();
            default    -> ItemStack.EMPTY;
        };
    }
}