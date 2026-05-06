package com.huebit.steamsmoke;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

public class HookahBlockEntityRenderer implements BlockEntityRenderer<HookahBlockEntity> {

    public HookahBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(HookahBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        FluidStack fluidStack = be.fluidTank.getFluid();
        if (fluidStack.isEmpty()) return;

        // Получаем цвет жидкости (с учетом прозрачности)
        IClientFluidTypeExtensions fluidExt = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        int color = fluidExt.getTintColor(fluidStack);

        float a = ((color >> 24) & 0xFF) / 255.0f;
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.translucent());
        Matrix4f matrix = poseStack.last().pose();

        poseStack.pushPose();

        // Рисуем твою ступенчатую модель кубами
        // Координаты ниже подобраны под VoxelShape твоего HookahBlock (4, 0, 4, 12, 10, 12)

        // Нижний широкий слой (основание жидкости)
        renderCube(matrix, vertexConsumer, 4.1f, 0.1f, 4.1f, 11.9f, 4.0f, 11.9f, r, g, b, a, combinedLight);

        // Средний слой (сужение)
        renderCube(matrix, vertexConsumer, 5.0f, 4.0f, 5.0f, 11.0f, 7.0f, 11.0f, r, g, b, a, combinedLight);

        // Верхний слой (горлышко жидкости)
        renderCube(matrix, vertexConsumer, 6.1f, 7.0f, 6.1f, 9.9f, 9.5f, 9.9f, r, g, b, a, combinedLight);

        poseStack.popPose();
    }

    private void renderCube(Matrix4f matrix, VertexConsumer builder, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a, int light) {
        float min = 0.0625f; // 1 пиксель в Minecraft
        renderFace(matrix, builder, x1*min, x2*min, y1*min, y2*min, z2*min, z2*min, r, g, b, a, light); // Front
        renderFace(matrix, builder, x1*min, x2*min, y1*min, y2*min, z1*min, z1*min, r, g, b, a, light); // Back
        renderFace(matrix, builder, x1*min, x1*min, y1*min, y2*min, z1*min, z2*min, r, g, b, a, light); // Left
        renderFace(matrix, builder, x2*min, x2*min, y1*min, y2*min, z1*min, z2*min, r, g, b, a, light); // Right
        renderFace(matrix, builder, x1*min, x2*min, y2*min, y2*min, z1*min, z2*min, r, g, b, a, light); // Top
        renderFace(matrix, builder, x1*min, x2*min, y1*min, y1*min, z1*min, z2*min, r, g, b, a, light); // Bottom
    }

    private void renderFace(Matrix4f matrix, VertexConsumer builder, float x1, float x2, float y1, float y2, float z1, float z2, float r, float g, float b, float a, int light) {
        builder.addVertex(matrix, x1, y1, z1).setColor(r, g, b, a).setLight(light);
        builder.addVertex(matrix, x1, y2, z2).setColor(r, g, b, a).setLight(light);
        builder.addVertex(matrix, x2, y2, z2).setColor(r, g, b, a).setLight(light);
        builder.addVertex(matrix, x2, y1, z1).setColor(r, g, b, a).setLight(light);
    }
}