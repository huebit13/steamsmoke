package com.huebit.steamsmoke;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CollectToPouchPacket() implements CustomPacketPayload {

    public static final Type<CollectToPouchPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(SteamSmoke.MODID, "collect_to_pouch")
    );

    public static final StreamCodec<FriendlyByteBuf, CollectToPouchPacket> CODEC =
            StreamCodec.unit(new CollectToPouchPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handleServer(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            if (player.containerMenu instanceof BlendPouchMenu menu) {
                menu.collectFromInventory();
            }
        });
    }
}
