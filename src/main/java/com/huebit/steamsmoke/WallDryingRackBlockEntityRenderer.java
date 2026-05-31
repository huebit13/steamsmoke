package com.huebit.steamsmoke;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class WallDryingRackBlockEntityRenderer implements BlockEntityRenderer<WallDryingRackBlockEntity> {

    private final ItemRenderer itemRenderer;

    // 3 позиции по X вдоль палочки (bar x=0..16, отступаем от краёв)
    private static final float[] X_POSITIONS = { 4f / 16f, 8f / 16f, 12f / 16f };

    // Палочка на y=11-13, вешаем предметы чуть ниже
    private static final float Y_ITEM = 10f / 16f;

    // Палочка у задней стенки (z=15-16 для SOUTH)
    private static final float Z_ITEM = 15.5f / 16f;

    public WallDryingRackBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(WallDryingRackBlockEntity be, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {

        List<ItemStack> items = be.getNonEmptyItems();
        if (items.isEmpty()) return;

        Direction facing = be.getBlockState().getValue(WallDryingRackBlock.FACING);
        float yAngle = switch (facing) {
            case SOUTH -> 180f;
            case EAST  -> 90f;
            case WEST  -> 270f;
            default    -> 0f; // NORTH — без поворота
        };

        poseStack.pushPose();

        // Поворачиваем вокруг центра блока
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(yAngle));
        poseStack.translate(-0.5, -0.5, -0.5);

        int count = Math.min(items.size(), WallDryingRackBlockEntity.MAX_SLOTS);
        for (int i = 0; i < count; i++) {
            poseStack.pushPose();
            poseStack.translate(X_POSITIONS[i], Y_ITEM, Z_ITEM);
            poseStack.scale(0.3f, 0.3f, 0.05f);

            itemRenderer.renderStatic(
                    items.get(i),
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

        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 64;
    }
}
