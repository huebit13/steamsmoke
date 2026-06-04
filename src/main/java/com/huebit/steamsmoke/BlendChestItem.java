package com.huebit.steamsmoke;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BlendChestItem extends Item {

    public static final int MIXTURE_SLOTS    = 18;
    public static final int INGREDIENT_SLOTS = 36;
    public static final int TOTAL_SLOTS      = 54;

    public BlendChestItem(Properties properties) {
        super(properties);
    }

    public static NonNullList<ItemStack> getContents(ItemStack stack) {
        NonNullList<ItemStack> items = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);
        stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(items);
        return items;
    }

    public static void setContents(ItemStack stack, NonNullList<ItemStack> items) {
        stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(items));
    }

    // Allow equipping in chestplate slot
    @Override
    public boolean canEquip(@NotNull ItemStack stack, @NotNull EquipmentSlot armorType, @NotNull LivingEntity entity) {
        return armorType == EquipmentSlot.CHEST;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            level.playSound(null, player.blockPosition(),
                    SoundEvents.BUNDLE_INSERT, SoundSource.PLAYERS, 0.6f, 1.1f);
            player.openMenu(
                    new SimpleMenuProvider(
                            (id, inv, p) -> BlendChestMenu.fromHand(id, inv, hand),
                            Component.translatable("item.steamsmoke.blend_chest")
                    ),
                    buf -> {
                        buf.writeBoolean(false); // item mode
                        buf.writeEnum(hand);
                    }
            );
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext ctx) {
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        if (player == null) return InteractionResult.PASS;

        Direction face = ctx.getClickedFace();
        BlockPos placePos = ctx.getClickedPos().relative(face);
        BlockState existing = level.getBlockState(placePos);

        if (!existing.canBeReplaced()) return InteractionResult.PASS;

        if (!level.isClientSide()) {
            ItemStack stack = ctx.getItemInHand();
            level.setBlock(placePos, SteamSmoke.BLEND_CHEST_BLOCK.get().defaultBlockState(), 3);
            if (level.getBlockEntity(placePos) instanceof BlendChestBlockEntity be) {
                NonNullList<ItemStack> items = getContents(stack);
                for (int i = 0; i < TOTAL_SLOTS; i++) be.container.setItem(i, items.get(i));
                be.setChanged();
            }
            if (!player.getAbilities().instabuild) stack.shrink(1);
            level.playSound(null, placePos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0f, 0.8f);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
