package com.huebit.steamsmoke;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
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

        // --- Молоко ---
        if (stack.getItem() instanceof MilkBucketItem) {
            if (!level.isClientSide) {
                IFluidHandlerItem milkHandler = stack.getCapability(Capabilities.FluidHandler.ITEM);
                if (milkHandler != null) {
                    FluidStack milkFluid = milkHandler.drain(1000, IFluidHandlerItem.FluidAction.SIMULATE);
                    if (!milkFluid.isEmpty()) {
                        milkHandler.drain(1000, IFluidHandlerItem.FluidAction.EXECUTE);
                        hookah.fluidTank.setFluid(new FluidStack(milkFluid.getFluid(), 1000));
                        hookah.syncToClients(); // явная синхронизация
                        if (!player.isCreative()) {
                            player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                        }
                    }
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        // --- Обычные жидкости (вода, лава, и др.) ---
        if (stack.getItem() instanceof BucketItem bucketItem) {
            Fluid fluid = bucketItem.content;

            // Полное ведро — заливаем (заменяем старую жидкость)
            if (fluid != Fluids.EMPTY) {
                if (!level.isClientSide) {
                    hookah.fluidTank.setFluid(new FluidStack(fluid, 1000));
                    hookah.syncToClients(); // явная синхронизация
                    if (!player.isCreative()) {
                        player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }

            // Пустое ведро — забираем жидкость из кальяна
            if (!hookah.fluidTank.isEmpty()) {
                if (!level.isClientSide) {
                    FluidStack fluidInHookah = hookah.fluidTank.getFluid().copy();
                    hookah.fluidTank.setFluid(FluidStack.EMPTY);
                    hookah.syncToClients(); // явная синхронизация
                    if (!player.isCreative()) {
                        ItemStack filledBucket = FluidUtil.getFilledBucket(fluidInHookah);
                        if (!filledBucket.isEmpty()) {
                            player.setItemInHand(hand, filledBucket);
                        }
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}