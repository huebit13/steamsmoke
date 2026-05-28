package com.huebit.steamsmoke;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class SteamSmokeClient {

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(SteamSmoke.HOOKAH.get(), RenderType.translucent());

            ItemProperties.register(
                    ModItems.MIXTURE.get(),
                    ResourceLocation.fromNamespaceAndPath(SteamSmoke.MODID, "flecks_variant"),
                    (stack, level, entity, seed) ->
                            MixtureItem.getFlecksVariant(stack) // 0 = нет слоя, 1..16 = вариант
            );
        });
    }

    @SubscribeEvent
    public void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.HOOKAH_BE.get(), HookahBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MORTAR_BE.get(), MortarBlockEntityRenderer::new);
    }
}