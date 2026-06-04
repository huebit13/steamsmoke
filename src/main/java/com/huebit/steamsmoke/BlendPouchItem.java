package com.huebit.steamsmoke;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;

public class BlendPouchItem extends Item {

    public static final int MIXTURE_SLOTS    = 18;
    public static final int INGREDIENT_SLOTS = 36;
    public static final int TOTAL_SLOTS      = 54;

    public BlendPouchItem(Properties properties) {
        super(properties);
    }

    public static boolean isIngredient(ItemStack stack) {
        if (stack.isEmpty()) return false;
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (!SteamSmoke.MODID.equals(key.getNamespace())) return false;
        String path = key.getPath();
        return path.startsWith("ground_") || path.startsWith("wet_ground_") || path.startsWith("dried_ground_");
    }

    public static NonNullList<ItemStack> getContents(ItemStack pouch) {
        NonNullList<ItemStack> items = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);
        pouch.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(items);
        return items;
    }

    public static void setContents(ItemStack pouch, NonNullList<ItemStack> items) {
        pouch.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(items));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            level.playSound(null, player.blockPosition(),
                    SoundEvents.BUNDLE_INSERT, SoundSource.PLAYERS, 0.6f, 1.1f);
            player.openMenu(
                    new SimpleMenuProvider(
                            (id, inv, p) -> new BlendPouchMenu(id, inv, hand),
                            Component.translatable("item.steamsmoke.blend_pouch")
                    ),
                    buf -> buf.writeEnum(hand)
            );
        }
        return InteractionResultHolder.success(stack);
    }
}
