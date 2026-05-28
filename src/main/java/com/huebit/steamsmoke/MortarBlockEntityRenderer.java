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

    public MortarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    // Позиции для 1, 2, 3, 4 предметов внутри ступки
    // Каждый массив: {offsetX, offsetZ} от центра блока (0.5, 0.5)
    private static final float[][][] SLOT_POSITIONS = {
            // 1 предмет
            {{ 0.0f, 0.0f }},
            // 2 предмета
            {{ -0.1f, 0.0f }, { 0.1f, 0.0f }},
            // 3 предмета
            {{ 0.0f, -0.1f }, { -0.1f, 0.08f }, { 0.1f, 0.08f }},
            // 4 предмета
            {{ -0.1f, -0.1f }, { 0.1f, -0.1f }, { -0.1f, 0.1f }, { 0.1f, 0.1f }}
    };

    @Override
    public void render(MortarBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {

        List<ItemStack> items = be.getNonEmptyItems();
        if (items.isEmpty()) return;

        int count = Math.min(items.size(), 4);
        float[][] positions = SLOT_POSITIONS[count - 1];

        for (int i = 0; i < count; i++) {
            ItemStack stack = items.get(i);
            float ox = positions[i][0];
            float oz = positions[i][1];

            poseStack.pushPose();

            // Смещаем внутрь чаши ступки
            // Y = 0.22f — высота внутри ступки (подобрано под модель)
            poseStack.translate(0.5f + ox, 0.22f, 0.5f + oz);

            // Кладём предмет плашмя горизонтально
            poseStack.mulPose(new Quaternionf().rotationX((float) Math.toRadians(90f)));

            // Маленький размер чтобы влезть в чашу
            float scale = 0.22f;
            poseStack.scale(scale, scale, scale);

            itemRenderer.renderStatic(
                    stack,
                    ItemDisplayContext.FIXED,
                    combinedLight,
                    combinedOverlay,
                    poseStack,
                    bufferSource,
                    be.getLevel(),
                    (int) be.getBlockPos().asLong() + i // уникальный seed для каждого слота
            );

            poseStack.popPose();
        }
    }
}