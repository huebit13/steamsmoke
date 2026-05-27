package com.huebit.steamsmoke;

import net.minecraft.resources.ResourceLocation;

public enum HookahFluidType {
    EMPTY("empty_fill"),
    WATER("water_fill"),
    LAVA("lava_fill"),
    MILK("milk_fill");

    private final String textureName;

    HookahFluidType(String textureName) {
        this.textureName = textureName;
    }

    public ResourceLocation getTextureLocation() {
        return ResourceLocation.fromNamespaceAndPath(SteamSmoke.MODID, "block/" + textureName);
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }
}