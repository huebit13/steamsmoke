package com.huebit.steamsmoke;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
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

public class HookahBlock extends Block implements EntityBlock {

    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(4, 0, 4, 12, 10, 12),
            Block.box(6, 10, 6, 10, 16, 10)
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
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof HookahBlockEntity hookah)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // --- Вёдра с жидкостью → залить в кальян ---
        if (stack.is(Items.WATER_BUCKET)) {
            return fillHookah(hookah, level, player, hand, HookahFluidType.WATER, Items.BUCKET.getDefaultInstance());
        }

        if (stack.is(Items.LAVA_BUCKET)) {
            return fillHookah(hookah, level, player, hand, HookahFluidType.LAVA, Items.BUCKET.getDefaultInstance());
        }

        if (stack.is(Items.MILK_BUCKET)) {
            return fillHookah(hookah, level, player, hand, HookahFluidType.MILK, Items.BUCKET.getDefaultInstance());
        }

        // --- Пустое ведро → забрать жидкость из кальяна ---
        if (stack.is(Items.BUCKET)) {
            if (!hookah.getFluidType().isEmpty()) {
                if (!level.isClientSide) {
                    ItemStack filledBucket = getFilledBucket(hookah.getFluidType());
                    hookah.setFluidType(HookahFluidType.EMPTY);
                    if (!player.isCreative()) {
                        player.setItemInHand(hand, filledBucket);
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    // -------------------------------------------------------------------------
    // Вспомогательные методы
    // -------------------------------------------------------------------------

    /**
     * Заливает жидкость в кальян (заменяет старую).
     * @param returnStack предмет, который игрок получит обратно (обычно пустое ведро)
     */
    private ItemInteractionResult fillHookah(HookahBlockEntity hookah, Level level,
                                             Player player, InteractionHand hand,
                                             HookahFluidType type, ItemStack returnStack) {
        if (!level.isClientSide) {
            hookah.setFluidType(type);
            if (!player.isCreative()) {
                player.setItemInHand(hand, returnStack);
            }
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    /**
     * Возвращает заполненное ведро для соответствующего типа жидкости.
     */
    private ItemStack getFilledBucket(HookahFluidType type) {
        return switch (type) {
            case WATER -> Items.WATER_BUCKET.getDefaultInstance();
            case LAVA  -> Items.LAVA_BUCKET.getDefaultInstance();
            case MILK  -> Items.MILK_BUCKET.getDefaultInstance();
            default    -> Items.BUCKET.getDefaultInstance();
        };
    }
}