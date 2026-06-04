package com.huebit.steamsmoke;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class SteamSmokeClient {

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(SteamSmoke.HOOKAH.get(), RenderType.translucent());

            ItemProperties.register(
                    ModItems.MIXTURE.get(),
                    ResourceLocation.fromNamespaceAndPath(SteamSmoke.MODID, "flecks_variant"),
                    (stack, level, entity, seed) ->
                            MixtureItem.getFlecksVariant(stack)
            );

            ItemProperties.register(
                    ModItems.PORTABLE_DIFFUSER.get(),
                    ResourceLocation.fromNamespaceAndPath(SteamSmoke.MODID, "fluid_variant"),
                    (stack, level, entity, seed) ->
                            (float) PortableDiffuserItem.getFluidVariant(stack)
            );
        });
    }

    @SubscribeEvent
    public void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.PORTABLE_DIFFUSER.get(), PortableDiffuserScreen::new);
        event.register(ModMenuTypes.BLEND_POUCH.get(), BlendPouchScreen::new);
    }

    @SubscribeEvent
    public void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.HOOKAH_BE.get(), HookahBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MORTAR_BE.get(), MortarBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.DRYING_RACK_BE.get(), DryingRackBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.WALL_DRYING_RACK_BE.get(), WallDryingRackBlockEntityRenderer::new);
    }
}