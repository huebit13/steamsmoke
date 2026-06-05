package com.huebit.steamsmoke;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.util.Map;

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
        event.register(ModMenuTypes.BLEND_CHEST.get(), BlendChestScreen::new);
    }

    @SubscribeEvent
    public void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.HOOKAH_BE.get(), HookahBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MORTAR_BE.get(), MortarBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.DRYING_RACK_BE.get(), DryingRackBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.WALL_DRYING_RACK_BE.get(), WallDryingRackBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerSkin.Model skin : event.getSkins()) {
            PlayerRenderer renderer = event.getSkin(skin);
            if (renderer != null) {
                renderer.addLayer(new BlendChestLayer(renderer));
            }
        }
    }

    @SubscribeEvent
    public void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(new ModelResourceLocation(
                ResourceLocation.fromNamespaceAndPath(SteamSmoke.MODID, "item/blend_chest_icon"), "standalone"));
    }

    @SubscribeEvent
    public void modifyBakingResult(ModelEvent.ModifyBakingResult event) {
        Map<ModelResourceLocation, BakedModel> models = event.getModels();

        ModelResourceLocation chestKey = new ModelResourceLocation(
                ResourceLocation.fromNamespaceAndPath(SteamSmoke.MODID, "blend_chest"), "inventory");
        BakedModel chestModel = models.get(chestKey);

        BakedModel iconModel = models.entrySet().stream()
                .filter(e -> e.getKey().id().getNamespace().equals(SteamSmoke.MODID)
                          && e.getKey().id().getPath().equals("item/blend_chest_icon"))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);

        if (chestModel != null && iconModel != null) {
            models.put(chestKey, new BlendChestGuiModel(chestModel, iconModel));
        }
    }
}