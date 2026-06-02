package com.huebit.steamsmoke;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SmokingDiscoveries {

    private static final String TAG = "ss_disc";

    // Client-side cache, populated via SyncDiscoveriesPacket; never read on server
    private static final Set<String> localDiscovered = new HashSet<>();

    /** SERVER-SIDE: записывает открытые ингредиенты и отправляет синхронизацию клиенту. */
    public static void recordSmoke(Player player, List<String> ingredients) {
        CompoundTag data = player.getPersistentData();
        CompoundTag disc = data.contains(TAG) ? data.getCompound(TAG) : new CompoundTag();
        for (String ing : ingredients) disc.putBoolean(ing, true);
        data.put(TAG, disc);
        if (player instanceof ServerPlayer sp) {
            PacketDistributor.sendToPlayer(sp, new SyncDiscoveriesPacket(getAllDiscovered(disc)));
        }
    }

    /** SERVER-SIDE: полная синхронизация открытий (при входе в мир). */
    public static void syncToClient(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        CompoundTag disc = data.contains(TAG) ? data.getCompound(TAG) : new CompoundTag();
        PacketDistributor.sendToPlayer(player, new SyncDiscoveriesPacket(getAllDiscovered(disc)));
    }

    private static Set<String> getAllDiscovered(CompoundTag disc) {
        Set<String> result = new HashSet<>();
        for (String key : disc.getAllKeys()) {
            if (disc.getBoolean(key)) result.add(key);
        }
        return result;
    }

    /** CLIENT-SIDE: вызывается обработчиком пакета для обновления локального кэша. */
    public static void applySync(Set<String> discovered) {
        localDiscovered.clear();
        localDiscovered.addAll(discovered);
    }

    /** CLIENT-SIDE: проверяет, открыт ли ингредиент (вызывается из тултипов). */
    public static boolean isDiscovered(String ingredient) {
        return localDiscovered.contains(ingredient);
    }
}
