package com.huebit.steamsmoke;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashSet;
import java.util.Set;

public record SyncDiscoveriesPacket(Set<String> discovered) implements CustomPacketPayload {

    public static final Type<SyncDiscoveriesPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(SteamSmoke.MODID, "sync_discoveries"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncDiscoveriesPacket> CODEC = StreamCodec.of(
            (buf, pkt) -> {
                buf.writeVarInt(pkt.discovered().size());
                for (String s : pkt.discovered()) buf.writeUtf(s);
            },
            buf -> {
                int size = buf.readVarInt();
                Set<String> set = new HashSet<>(size);
                for (int i = 0; i < size; i++) set.add(buf.readUtf());
                return new SyncDiscoveriesPacket(set);
            }
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleClient(SyncDiscoveriesPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> SmokingDiscoveries.applySync(packet.discovered()));
    }
}
