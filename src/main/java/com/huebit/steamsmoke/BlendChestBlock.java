package com.huebit.steamsmoke;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlendChestBlock extends Block implements EntityBlock {

    // Approximate shape from model: x=4-12, y=0-12, z=5-11 (in 1/16 units)
    private static final VoxelShape SHAPE = Shapes.box(4.0/16, 0.0, 5.0/16, 12.0/16, 12.0/16, 11.0/16);

    public BlendChestBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
            @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BlendChestBlockEntity(pos, state);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level,
            @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof BlendChestBlockEntity be) {
            player.openMenu(
                    new SimpleMenuProvider(
                            (id, inv, p) -> BlendChestMenu.fromBlock(id, inv, be),
                            Component.translatable("item.steamsmoke.blend_chest")
                    ),
                    buf -> {
                        buf.writeBoolean(true); // block mode
                        buf.writeBlockPos(pos);
                    }
            );
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
            @NotNull BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && !level.isClientSide()) {
            if (level.getBlockEntity(pos) instanceof BlendChestBlockEntity be) {
                ItemStack drop = new ItemStack(ModItems.BLEND_CHEST_ITEM.get());
                NonNullList<ItemStack> items = NonNullList.withSize(BlendChestItem.TOTAL_SLOTS, ItemStack.EMPTY);
                for (int i = 0; i < BlendChestItem.TOTAL_SLOTS; i++) items.set(i, be.container.getItem(i));
                BlendChestItem.setContents(drop, items);
                net.minecraft.world.Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), drop);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
