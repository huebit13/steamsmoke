package com.huebit.steamsmoke;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DryingRackBlockEntityRenderer implements BlockEntityRenderer<DryingRackBlockEntity> {

    private final ItemRenderer itemRenderer;

    // 3 позиции по X вдоль палочки (x=3..13 в модели, т.е. 3/16..13/16 в блок-координатах)
    private static final float[] X_POSITIONS = { 5f / 16f, 8f / 16f, 11f / 16f };

    // Глубина палочек (z=7.5/16)
    private static final float Z = 7.5f / 16f;

    // Нижняя палочка: y=12/16 + 4px, вешаем чуть ниже
    private static final float Y_LOWER = 13.5f / 16f - 0.18f;

    // Верхняя палочка: y=23/16 + 1.5px, вешаем чуть ниже (в координатах нижнего блока)
    private static final float Y_UPPER = 24.5f / 16f - 0.18f;

    public DryingRackBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(DryingRackBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {

        List<ItemStack> items = be.getNonEmptyItems();
        if (items.isEmpty()) return;

        int count = Math.min(items.size(), DryingRackBlockEntity.MAX_SLOTS);

        for (int i = 0; i < count; i++) {
            ItemStack stack = items.get(i);

            float x = X_POSITIONS[i % 3];
            float y = i < 3 ? Y_LOWER : Y_UPPER;

            poseStack.pushPose();
            poseStack.translate(x, y, Z);
            // Тонкий по Z — предмет плоский, висит вертикально лицом на юг
            poseStack.scale(0.3f, 0.3f, 0.05f);

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

    @Override
    public int getViewDistance() {
        return 64;
    }
}
