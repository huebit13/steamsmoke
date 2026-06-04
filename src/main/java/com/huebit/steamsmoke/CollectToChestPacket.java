package com.huebit.steamsmoke;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CollectToChestPacket() implements CustomPacketPayload {

    public static final Type<CollectToChestPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(SteamSmoke.MODID, "collect_to_chest")
    );

    public static final StreamCodec<FriendlyByteBuf, CollectToChestPacket> CODEC =
            StreamCodec.unit(new CollectToChestPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handleServer(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            if (player.containerMenu instanceof BlendChestMenu menu) {
                menu.collectFromInventory();
            }
        });
    }
}
