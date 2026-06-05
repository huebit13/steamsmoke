package com.huebit.steamsmoke;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlendChestGuiModel implements BakedModel {

    private final BakedModel original;
    private final BakedModel icon;

    public BlendChestGuiModel(BakedModel original, BakedModel icon) {
        this.original = original;
        this.icon = icon;
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext ctx, PoseStack poseStack, boolean leftHand) {
        if (ctx == ItemDisplayContext.GUI) {
            return icon.applyTransform(ctx, poseStack, leftHand);
        }
        return original.applyTransform(ctx, poseStack, leftHand);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        return original.getQuads(state, side, rand);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData data, @Nullable RenderType renderType) {
        return original.getQuads(state, side, rand, data, renderType);
    }

    @Override
    public boolean useAmbientOcclusion() { return original.useAmbientOcclusion(); }

    @Override
    public boolean isGui3d() { return icon.isGui3d(); }

    @Override
    public boolean usesBlockLight() { return icon.usesBlockLight(); }

    @Override
    public boolean isCustomRenderer() { return original.isCustomRenderer(); }

    @Override
    public TextureAtlasSprite getParticleIcon() { return original.getParticleIcon(); }

    @Override
    public ItemOverrides getOverrides() { return original.getOverrides(); }

    @Override
    public ItemTransforms getTransforms() { return original.getTransforms(); }
}
