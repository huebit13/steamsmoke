package com.huebit.steamsmoke;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public class SteamSmokeClient {

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(SteamSmoke.HOOKAH.get(), RenderType.translucent());
        });
    }

    @SubscribeEvent
    public void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.HOOKAH_BE.get(), HookahBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MORTAR_BE.get(), MortarBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {

            if (tintIndex == 1) {
                return MixtureItem.getBlendColor(stack);
            }
            return -1;
        }, ModItems.MIXTURE.get());
    }
}