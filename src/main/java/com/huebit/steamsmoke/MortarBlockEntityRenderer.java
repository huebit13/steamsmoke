package com.huebit.steamsmoke;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

import java.util.List;

public class MortarBlockEntityRenderer implements BlockEntityRenderer<MortarBlockEntity> {

    private final ItemRenderer itemRenderer;

    // Угол поворота вокруг вертикальной оси для каждого слота
    private static final float[] Y_ROTATIONS = { 15f, -25f, 40f, -10f };

    public MortarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(MortarBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {

        List<ItemStack> items = be.getNonEmptyItems();
        if (items.isEmpty()) return;

        int count = Math.min(items.size(), 4);

        for (int i = 0; i < count; i++) {
            ItemStack stack = items.get(i);

            poseStack.pushPose();

            // Стопка: каждый следующий предмет чуть выше предыдущего
            float yOffset = 0.09f + i * 0.01f;
            poseStack.translate(0.5f, yOffset, 0.5f);

            // Кладём предмет плашмя горизонтально
            poseStack.mulPose(new Quaternionf().rotationX((float) Math.toRadians(90f)));

            // Поворачиваем каждый предмет на свой угол (после rotationX это даёт вращение вокруг вертикали)
            poseStack.mulPose(new Quaternionf().rotationZ((float) Math.toRadians(Y_ROTATIONS[i])));

            poseStack.scale(0.22f, 0.22f, 0.22f);

            itemRenderer.renderStatic(
                    stack,
                    ItemDisplayContext.FIXED,
                    combinedLight,
                    combinedOverlay,
                    poseStack,
                    bufferSource,
                    be.getLevel(),
                    (int) be.getBlockPos().asLong() + i
            );

            poseStack.popPose();
        }
    }
}