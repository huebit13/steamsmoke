package com.huebit.steamsmoke;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

public class SmokingDiscoveries {

    private static final String TAG = "ss_disc";

    /** Вызывается SERVER-SIDE при каждом курении — записывает открытые ингредиенты. */
    public static void recordSmoke(Player player, List<String> ingredients) {
        CompoundTag data = player.getPersistentData();
        CompoundTag disc = data.contains(TAG) ? data.getCompound(TAG) : new CompoundTag();
        for (String ing : ingredients) disc.putBoolean(ing, true);
        data.put(TAG, disc);
    }

    /** Вызывается CLIENT-SIDE из тултипов — проверяет, открыт ли ингредиент. */
    @OnlyIn(Dist.CLIENT)
    public static boolean isDiscovered(String ingredient) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.player == null) return false;
        CompoundTag data = mc.player.getPersistentData();
        if (!data.contains(TAG)) return false;
        return data.getCompound(TAG).getBoolean(ingredient);
    }
}
