package com.huebit.steamsmoke;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlendChestLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public BlendChestLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light,
            AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
            float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!chestStack.is(ModItems.BLEND_CHEST_ITEM.get())) return;

        poseStack.pushPose();
        this.getParentModel().body.translateAndRotate(poseStack);
        // Body-space: Y+=down, Z+=toward player front.
        // Push to back of body (Z- = toward back in body-space)
        poseStack.translate(-0.5F, -0.5F, 0.825F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.translate(-0.5F, -0.75F, -0.5F);

        Minecraft.getInstance().getItemRenderer().renderStatic(
                chestStack,
                ItemDisplayContext.NONE,
                light, OverlayTexture.NO_OVERLAY,
                poseStack, buffer, player.level(), 0
        );
        poseStack.popPose();
    }
}
