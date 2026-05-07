package com.huebit.steamsmoke;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(SteamSmoke.MODID)
public class SteamSmoke {
    public static final String MODID = "steamsmoke";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredBlock<Block> HOOKAH = BLOCKS.register("hookah",
            () -> new HookahBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.0f)
                    .noOcclusion()
            )
    );

    public static final DeferredItem<BlockItem> HOOKAH_ITEM = ITEMS.registerSimpleBlockItem("hookah", HOOKAH);

    public SteamSmoke(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        modEventBus.addListener(this::registerCapabilities);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.register(SteamSmokeClient.class);
        }
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.HOOKAH_BE.get(),
                (be, side) -> be.fluidTank
        );
    }
}