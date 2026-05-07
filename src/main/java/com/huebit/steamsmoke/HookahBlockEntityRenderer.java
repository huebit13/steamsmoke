package com.huebit.steamsmoke;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

public class HookahBlockEntityRenderer implements BlockEntityRenderer<HookahBlockEntity> {

    // Точные координаты из liquid_fill.json: {x1, y1, z1, x2, y2, z2}
    private static final float[][] ELEMENTS = {
            {5, 3, 6,  6, 4, 7},
            {5, 2, 6,  6, 3, 7},
            {5, 2, 7,  6, 3, 9},
            {5, 3, 7,  6, 4, 9},
            {6, 1, 6,  7, 2, 7},
            {6, 2, 6,  7, 4, 7},
            {7, 1, 6,  9, 2, 7},
            {7, 2, 6,  9, 3, 7},
            {7, 3, 6,  9, 4, 7},
            {6, 1, 7,  10, 2, 9},
            {6, 2, 10, 7, 3, 11},
            {6, 3, 10, 10, 4, 11},
            {6, 2, 5,  10, 3, 6},
            {6, 3, 5,  10, 4, 6},
            {6, 2, 4,  10, 3, 5},
            {6, 2, 11, 10, 3, 12},
            {4, 2, 6,  5, 3, 10},
            {11, 2, 6, 12, 3, 10},
            {5, 2, 5,  6, 3, 6},
            {5, 2, 10, 6, 3, 11},
            {10, 2, 5, 11, 3, 6},
            {10, 2, 10, 11, 3, 11},
            {10, 2, 6, 11, 3, 10},
            {10, 3, 6, 11, 4, 10},
            {6, 1, 9,  7, 2, 10},
            {6, 2, 9,  7, 4, 10},
            {5, 2, 9,  6, 3, 10},
            {5, 3, 9,  6, 4, 10},
            {9, 1, 9,  10, 2, 10},
            {9, 2, 9,  10, 4, 10},
            {7, 2, 10, 10, 3, 11},
            {6, 4, 7,  7, 6, 9},
            {7, 1, 9,  9, 2, 10},
            {7, 2, 9,  9, 4, 10},
            {7, 4, 7,  9, 6, 9},
            {7, 4, 9,  9, 6, 10},
            {7, 4, 6,  9, 6, 7},
            {9, 4, 7,  10, 6, 9},
            {9, 1, 6,  10, 2, 7},
            {9, 2, 6,  10, 4, 7},
            {6, 2, 7,  10, 4, 9},
            {7, 6, 7,  9, 6, 9},
            {7, 6, 7,  9, 7, 9}
    };

    private static final float FLUID_Y_MIN = 1.0f;
    private static final float FLUID_Y_MAX = 7.0f;

    public HookahBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(HookahBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {

        FluidStack fluidStack = be.fluidTank.getFluid();
        if (fluidStack.isEmpty()) return;

        float fillPercent = (float) fluidStack.getAmount() / (float) be.fluidTank.getCapacity();
        float fluidTopY = FLUID_Y_MIN + (FLUID_Y_MAX - FLUID_Y_MIN) * fillPercent;

        IClientFluidTypeExtensions fluidExt = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        int color = fluidExt.getTintColor(fluidStack);

        float a = ((color >> 24) & 0xFF) / 255.0f;
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8)  & 0xFF) / 255.0f;
        float b = (color & 0xFF)         / 255.0f;

        if (a <= 0.01f) a = 0.8f;
        else a = Math.min(a, 0.85f);

        ResourceLocation stillTex = fluidExt.getStillTexture(fluidStack);
        if (stillTex == null) stillTex = ResourceLocation.withDefaultNamespace("block/water_still");

        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(stillTex);

        VertexConsumer vc = bufferSource.getBuffer(RenderType.translucent());
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();

        poseStack.pushPose();

        for (float[] el : ELEMENTS) {
            float x1 = el[0], y1 = el[1], z1 = el[2];
            float x2 = el[3], y2 = el[4], z2 = el[5];

            // Пропускаем куб, если он полностью выше уровня жидкости
            if (y1 >= fluidTopY) continue;

            // Обрезаем верхнюю грань куба по уровню жидкости
            float topY = Math.min(y2, fluidTopY);

            renderCube(matrix, pose, vc,
                    x1, y1, z1, x2, topY, z2,
                    r, g, b, a, combinedLight, sprite);
        }

        poseStack.popPose();
    }

    private void renderCube(Matrix4f matrix, PoseStack.Pose pose, VertexConsumer vc,
                            float x1, float y1, float z1,
                            float x2, float y2, float z2,
                            float r, float g, float b, float a, int light,
                            TextureAtlasSprite sprite) {
        float p = 0.0625f; // Конвертация из модельных координат (1/16) в мировые
        float nx1 = x1 * p, nx2 = x2 * p;
        float ny1 = y1 * p, ny2 = y2 * p;
        float nz1 = z1 * p, nz2 = z2 * p;

        float u0 = sprite.getU0(), u1 = sprite.getU1();
        float v0 = sprite.getV0(), v1 = sprite.getV1();

        // Front +Z
        quad(matrix, pose, vc, nx1,ny1,nz2, nx1,ny2,nz2, nx2,ny2,nz2, nx2,ny1,nz2, 0,0,1, r,g,b,a,light, u0,v1,u0,v0,u1,v0,u1,v1);
        // Back -Z
        quad(matrix, pose, vc, nx2,ny1,nz1, nx2,ny2,nz1, nx1,ny2,nz1, nx1,ny1,nz1, 0,0,-1, r,g,b,a,light, u0,v1,u0,v0,u1,v0,u1,v1);
        // Left -X
        quad(matrix, pose, vc, nx1,ny1,nz1, nx1,ny2,nz1, nx1,ny2,nz2, nx1,ny1,nz2, -1,0,0, r,g,b,a,light, u0,v1,u0,v0,u1,v0,u1,v1);
        // Right +X
        quad(matrix, pose, vc, nx2,ny1,nz2, nx2,ny2,nz2, nx2,ny2,nz1, nx2,ny1,nz1, 1,0,0, r,g,b,a,light, u0,v1,u0,v0,u1,v0,u1,v1);
        // Top +Y
        quad(matrix, pose, vc, nx1,ny2,nz2, nx1,ny2,nz1, nx2,ny2,nz1, nx2,ny2,nz2, 0,1,0, r,g,b,a,light, u0,v1,u0,v0,u1,v0,u1,v1);
        // Bottom -Y
        quad(matrix, pose, vc, nx1,ny1,nz1, nx1,ny1,nz2, nx2,ny1,nz2, nx2,ny1,nz1, 0,-1,0, r,g,b,a,light, u0,v1,u0,v0,u1,v0,u1,v1);
    }

    private void quad(Matrix4f matrix, PoseStack.Pose pose, VertexConsumer vc,
                      float x1, float y1, float z1, float x2, float y2, float z2,
                      float x3, float y3, float z3, float x4, float y4, float z4,
                      float nx, float ny, float nz,
                      float r, float g, float b, float a, int light,
                      float ua, float va, float ub, float vb, float uc, float vc2, float ud, float vd) {
        vc.addVertex(matrix, x1, y1, z1).setColor(r, g, b, a).setUv(ua, va).setOverlay(0).setLight(light).setNormal(pose, nx, ny, nz);
        vc.addVertex(matrix, x2, y2, z2).setColor(r, g, b, a).setUv(ub, vb).setOverlay(0).setLight(light).setNormal(pose, nx, ny, nz);
        vc.addVertex(matrix, x3, y3, z3).setColor(r, g, b, a).setUv(uc, vc2).setOverlay(0).setLight(light).setNormal(pose, nx, ny, nz);
        vc.addVertex(matrix, x4, y4, z4).setColor(r, g, b, a).setUv(ud, vd).setOverlay(0).setLight(light).setNormal(pose, nx, ny, nz);
    }
}