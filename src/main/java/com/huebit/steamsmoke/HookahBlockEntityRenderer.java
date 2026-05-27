package com.huebit.steamsmoke;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import org.joml.Matrix4f;

public class HookahBlockEntityRenderer implements BlockEntityRenderer<HookahBlockEntity> {

    // Элементы из liquid_fill.json: {x1, y1, z1, x2, y2, z2}
    private static final float[][] ELEMENTS = {
            {5.1f, 2.9f, 6.1f,   6.1f, 3.9f, 7f},
            {5.1f, 2.1f, 6.1f,   6.1f, 2.9f, 7f},
            {5.1f, 2.1f, 7f,     6.1f, 2.9f, 9f},
            {5.1f, 2.9f, 7f,     6.1f, 3.9f, 9f},
            {6.1f, 1.1f, 6.1f,   7f,   2.1f, 7f},
            {6.1f, 2.1f, 6.1f,   7.1f, 3.9f, 7.1f},
            {7f,   1.1f, 6.1f,   9f,   2.1f, 7f},
            {7f,   2.1f, 6.1f,   9f,   3f,   7f},
            {7.1f, 3f,   6.1f,   8.9f, 3.9f, 7.1f},
            {6.1f, 2.1f, 9.9f,   7f,   2.9f, 10.9f},
            {6.1f, 2.9f, 9.9f,   9.9f, 3.9f, 10.9f},
            {6.1f, 2.1f, 5.1f,   9.9f, 2.9f, 6.1f},
            {6.1f, 2.9f, 5.1f,   9.9f, 3.9f, 6.1f},
            {6.1f, 2.1f, 4.1f,   9.9f, 2.9f, 5.1f},
            {6.1f, 2.1f, 10.9f,  9.9f, 2.9f, 11.9f},
            {4.1f, 2.1f, 6.1f,   5.1f, 2.9f, 9.9f},
            {10.9f,2.1f, 6.1f,   11.9f,2.9f, 9.9f},
            {5.1f, 2.1f, 5.1f,   6.1f, 2.9f, 6.1f},
            {5.1f, 2.1f, 9.9f,   6.1f, 2.9f, 10.9f},
            {9.9f, 2.1f, 5.1f,   10.9f,2.9f, 6.1f},
            {9.9f, 2.1f, 9.9f,   10.9f,2.9f, 10.9f},
            {9.9f, 2.1f, 6.1f,   10.9f,2.9f, 9.9f},
            {9.9f, 2.9f, 6.1f,   10.9f,3.9f, 9.9f},
            {6.1f, 1.1f, 9f,     7f,   2.1f, 9.9f},
            {6.1f, 2.1f, 8.9f,   7.1f, 3.9f, 9.9f},
            {5.1f, 2.1f, 9f,     6.1f, 2.9f, 9.9f},
            {5.1f, 2.9f, 9f,     6.1f, 3.9f, 9.9f},
            {9f,   1.1f, 9f,     9.9f, 2.1f, 9.9f},
            {8.9f, 2.1f, 8.9f,   9.9f, 3.9f, 9.9f},
            {7f,   2.1f, 9.9f,   9.9f, 2.9f, 10.9f},
            {6.1f, 3.9f, 7.1f,   7.1f, 5.9f, 8.9f},
            {7f,   1.1f, 9f,     9f,   2.1f, 9.9f},
            {7.1f, 2.1f, 8.9f,   8.9f, 3.9f, 9.9f},
            {7.1f, 4f,   7.1f,   8.9f, 5.9f, 8.9f},
            {7.1f, 3.9f, 8.9f,   8.9f, 5.9f, 9.9f},
            {7.1f, 3.9f, 6.1f,   8.9f, 5.9f, 7.1f},
            {8.9f, 3.9f, 7.1f,   9.9f, 5.9f, 8.9f},
            {9f,   1.1f, 6.1f,   9.9f, 2.1f, 7f},
            {8.9f, 2.1f, 6.1f,   9.9f, 3.9f, 7.1f},
            {6.1f, 2.1f, 7.1f,   9.9f, 3.9f, 8.9f},
            {7.1f, 5.9f, 7.1f,   8.9f, 6.9f, 8.9f},
            {6.1f, 1.1f, 7f,     9.9f, 2.1f, 9f},
    };

    public HookahBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(HookahBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {

        HookahFluidType fluidType = be.getFluidType();
        if (fluidType.isEmpty()) return;

        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(fluidType.getTextureLocation());

        VertexConsumer vc = bufferSource.getBuffer(RenderType.translucent());
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();

        poseStack.pushPose();
        for (float[] el : ELEMENTS) {
            renderCube(matrix, pose, vc,
                    el[0], el[1], el[2],
                    el[3], el[4], el[5],
                    combinedLight, sprite);
        }
        poseStack.popPose();
    }

    private void renderCube(Matrix4f matrix, PoseStack.Pose pose, VertexConsumer vc,
                            float x1, float y1, float z1,
                            float x2, float y2, float z2,
                            int light, TextureAtlasSprite sprite) {
        final float p = 0.0625f; // 1 единица модели = 1/16 блока
        float nx1 = x1 * p, nx2 = x2 * p;
        float ny1 = y1 * p, ny2 = y2 * p;
        float nz1 = z1 * p, nz2 = z2 * p;

        float u0 = sprite.getU0(), u1 = sprite.getU1();
        float v0 = sprite.getV0(), v1 = sprite.getV1();

        quad(matrix, pose, vc, nx1,ny1,nz2, nx1,ny2,nz2, nx2,ny2,nz2, nx2,ny1,nz2,  0, 0, 1, light, u0,v1, u0,v0, u1,v0, u1,v1);
        quad(matrix, pose, vc, nx2,ny1,nz1, nx2,ny2,nz1, nx1,ny2,nz1, nx1,ny1,nz1,  0, 0,-1, light, u0,v1, u0,v0, u1,v0, u1,v1);
        quad(matrix, pose, vc, nx1,ny1,nz1, nx1,ny2,nz1, nx1,ny2,nz2, nx1,ny1,nz2, -1, 0, 0, light, u0,v1, u0,v0, u1,v0, u1,v1);
        quad(matrix, pose, vc, nx2,ny1,nz2, nx2,ny2,nz2, nx2,ny2,nz1, nx2,ny1,nz1,  1, 0, 0, light, u0,v1, u0,v0, u1,v0, u1,v1);
        quad(matrix, pose, vc, nx1,ny2,nz2, nx1,ny2,nz1, nx2,ny2,nz1, nx2,ny2,nz2,  0, 1, 0, light, u0,v1, u0,v0, u1,v0, u1,v1);
        quad(matrix, pose, vc, nx1,ny1,nz1, nx1,ny1,nz2, nx2,ny1,nz2, nx2,ny1,nz1,  0,-1, 0, light, u0,v1, u0,v0, u1,v0, u1,v1);
    }

    private void quad(Matrix4f matrix, PoseStack.Pose pose, VertexConsumer vc,
                      float x1, float y1, float z1,
                      float x2, float y2, float z2,
                      float x3, float y3, float z3,
                      float x4, float y4, float z4,
                      float nx, float ny, float nz, int light,
                      float ua, float va, float ub, float vb,
                      float uc, float vc2, float ud, float vd) {
        vc.addVertex(matrix, x1,y1,z1).setColor(1f,1f,1f,1f).setUv(ua,va).setOverlay(0).setLight(light).setNormal(pose,nx,ny,nz);
        vc.addVertex(matrix, x2,y2,z2).setColor(1f,1f,1f,1f).setUv(ub,vb).setOverlay(0).setLight(light).setNormal(pose,nx,ny,nz);
        vc.addVertex(matrix, x3,y3,z3).setColor(1f,1f,1f,1f).setUv(uc,vc2).setOverlay(0).setLight(light).setNormal(pose,nx,ny,nz);
        vc.addVertex(matrix, x4,y4,z4).setColor(1f,1f,1f,1f).setUv(ud,vd).setOverlay(0).setLight(light).setNormal(pose,nx,ny,nz);
    }
}