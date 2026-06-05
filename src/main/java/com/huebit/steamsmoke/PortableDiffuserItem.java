package com.huebit.steamsmoke;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PortableDiffuserItem extends Item {

    private static final int CHARGE_TICKS = 40;
    // Keys stored in CUSTOM_DATA — no ItemStack serialisation, just plain tags
    private static final String INGREDIENTS_TAG  = "DiffuserIngredients";
    private static final String DAMAGE_TAG        = "DiffuserDamage";
    static final String FLUID_VARIANT_TAG = "DiffuserFluidVariant";

    public PortableDiffuserItem(Properties properties) {
        super(properties);
    }

    // ── Data helpers ──────────────────────────────────────────────────────

    /**
     * Reconstructs a Mixture ItemStack from the data stored in the diffuser.
     * No HolderLookup.Provider needed — only plain NBT reads.
     */
    public static ItemStack getMixture(ItemStack diffuser) {
        CustomData data = diffuser.get(DataComponents.CUSTOM_DATA);
        if (data == null) return ItemStack.EMPTY;
        CompoundTag tag = data.copyTag();
        if (!tag.contains(INGREDIENTS_TAG, Tag.TAG_LIST)) return ItemStack.EMPTY;

        ListTag list = tag.getList(INGREDIENTS_TAG, Tag.TAG_STRING);
        if (list.isEmpty()) return ItemStack.EMPTY;

        List<String> ingredients = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) ingredients.add(list.getString(i));

        ItemStack mixture = MixtureItem.createMixture(ingredients);
        mixture.setDamageValue(tag.getInt(DAMAGE_TAG));
        return mixture;
    }

    /** Saves a Mixture's key data (ingredients + damage) into the diffuser. No registry needed. */
    public static void setMixture(ItemStack diffuser, ItemStack mixture) {
        CustomData.update(DataComponents.CUSTOM_DATA, diffuser, tag -> {
            if (mixture.isEmpty()) {
                tag.remove(INGREDIENTS_TAG);
                tag.remove(DAMAGE_TAG);
            } else {
                List<String> ingredients = MixtureItem.getIngredients(mixture);
                ListTag list = new ListTag();
                for (String ing : ingredients) list.add(StringTag.valueOf(ing));
                tag.put(INGREDIENTS_TAG, list);
                tag.putInt(DAMAGE_TAG, mixture.getDamageValue());
            }
        });
    }

    public static int getFluidVariant(ItemStack diffuser) {
        CustomData data = diffuser.get(DataComponents.CUSTOM_DATA);
        if (data == null) return 0;
        return data.copyTag().getInt(FLUID_VARIANT_TAG);
    }

    public static void setFluidVariant(ItemStack diffuser, int variant) {
        CustomData.update(DataComponents.CUSTOM_DATA, diffuser,
                tag -> tag.putInt(FLUID_VARIANT_TAG, variant));
    }

    /** Consumes one use from the stored mixture. Returns true if mixture still has uses left. */
    public static boolean consumeUse(ItemStack diffuser) {
        ItemStack mixture = getMixture(diffuser);
        if (mixture.isEmpty()) return false;
        int newDamage = mixture.getDamageValue() + 1;
        if (newDamage >= mixture.getMaxDamage()) {
            setMixture(diffuser, ItemStack.EMPTY);
            setFluidVariant(diffuser, 0);
            return false;
        }
        mixture.setDamageValue(newDamage);
        setMixture(diffuser, mixture);
        return true;
    }

    // ── Use ───────────────────────────────────────────────────────────────

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player,
                                                           @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            if (!level.isClientSide()) {
                level.playSound(null, player.blockPosition(),
                        SoundEvents.WOODEN_BUTTON_CLICK_ON, SoundSource.PLAYERS, 0.4f, 1.2f);
                player.openMenu(
                        new SimpleMenuProvider(
                                (id, inv, p) -> new PortableDiffuserMenu(id, inv, hand),
                                Component.translatable("item.steamsmoke.portable_diffuser")
                        ),
                        buf -> buf.writeEnum(hand)
                );
            }
            return InteractionResultHolder.success(stack);
        }

        if (getMixture(stack).isEmpty()) {
            if (!level.isClientSide()) {
                player.displayClientMessage(
                        Component.translatable("item.steamsmoke.portable_diffuser.no_mixture"), true);
            }
            return InteractionResultHolder.fail(stack);
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level,
                                              @NotNull LivingEntity entity) {
        if (!level.isClientSide() && entity instanceof Player player) {
            applySmoke(stack, level, player);
        }
        return stack;
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity livingEntity,
                          @NotNull ItemStack stack, int remainingUseDuration) {
        int ticksUsed = getUseDuration(stack, livingEntity) - remainingUseDuration;
        if (ticksUsed == 1 && !level.isClientSide()) {
            // Тихое шипение при начале затяжки
            level.playSound(null, livingEntity.blockPosition(),
                    SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS,
                    0.35f, 1.6f + level.random.nextFloat() * 0.3f);
        }
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level,
                             @NotNull LivingEntity livingEntity, int timeCharged) {
        // Released early — no effect applied
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return CHARGE_TICKS;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }

    // ── Tooltip ───────────────────────────────────────────────────────────

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
                                @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        ItemStack mixture = getMixture(stack);
        if (mixture.isEmpty()) {
            tooltip.add(Component.translatable("item.steamsmoke.portable_diffuser.no_mixture")
                    .withStyle(ChatFormatting.GRAY));
        } else {
            mixture.getItem().appendHoverText(mixture, context, tooltip, flag);
        }
    }

    // ── Smoke logic ───────────────────────────────────────────────────────

    private void applySmoke(ItemStack stack, Level level, Player player) {
        ItemStack mixture = getMixture(stack);
        if (mixture.isEmpty()) return;

        List<String> ingredients = MixtureItem.getIngredients(mixture);
        HookahSmokingRecipes.IngredientEffects effects =
                HookahSmokingRecipes.getEffects(ingredients, HookahFluidType.WATER);

        SmokingDiscoveries.recordSmoke(player, ingredients);
        effects.all().forEach(player::addEffect);

        // Затяжка
        level.playSound(null, player.blockPosition(),
                SoundEvents.HONEY_DRINK, SoundSource.PLAYERS,
                1.0f, 0.75f + level.random.nextFloat() * 0.25f);
        // Выдох дыма
        level.playSound(null, player.blockPosition(),
                SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS,
                0.25f, 0.7f + level.random.nextFloat() * 0.2f);

        boolean alive = consumeUse(stack);
        if (!alive) {
            player.displayClientMessage(
                    Component.translatable("block.steamsmoke.hookah.mixture_spent"), true);
        }

        player.getInventory().setChanged();
    }
}
