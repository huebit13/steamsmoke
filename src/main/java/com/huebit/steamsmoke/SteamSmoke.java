package com.huebit.steamsmoke;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(SteamSmoke.MODID)
public class SteamSmoke {
    public static final String MODID = "steamsmoke";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredBlock<Block> HOOKAH = BLOCKS.register("hookah",
            () -> new HookahBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.0f)
                    .noOcclusion()
            )
    );
    public static final DeferredItem<BlockItem> HOOKAH_ITEM = ITEMS.registerSimpleBlockItem("hookah", HOOKAH);

    public static final DeferredBlock<Block> MORTAR = BLOCKS.register("mortar",
            () -> new MortarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(2.0f)
                    .noOcclusion()
            )
    );
    public static final DeferredItem<BlockItem> MORTAR_ITEM = ITEMS.registerSimpleBlockItem("mortar", MORTAR);

    public static final DeferredBlock<Block> DRYING_RACK = BLOCKS.register("drying_rack",
            () -> new DryingRackBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(1.5f)
                    .noOcclusion()
            )
    );
    public static final DeferredItem<BlockItem> DRYING_RACK_ITEM = ITEMS.registerSimpleBlockItem("drying_rack", DRYING_RACK);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> STEAM_SMOKE_TAB =
            CREATIVE_TABS.register("steam_smoke_tab", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.steamsmoke"))
                            .icon(() -> HOOKAH_ITEM.get().getDefaultInstance())
                            .displayItems((params, output) -> {
                                output.accept(HOOKAH_ITEM.get());
                                output.accept(MORTAR_ITEM.get());
                                output.accept(DRYING_RACK_ITEM.get());
                                output.accept(ModItems.PESTLE.get());
                                output.accept(ModItems.TOBACCO_LEAF.get());
                                // Базовые перемолотые
                                output.accept(ModItems.GROUND_TOBACCO.get());
                                output.accept(ModItems.GROUND_APPLE.get());
                                output.accept(ModItems.GROUND_SUGAR.get());
                                output.accept(ModItems.GROUND_SWEET_BERRIES.get());
                                output.accept(ModItems.GROUND_HONEYCOMB.get());
                                // Ранняя игра
                                output.accept(ModItems.GROUND_DANDELION.get());
                                output.accept(ModItems.GROUND_POPPY.get());
                                output.accept(ModItems.GROUND_BROWN_MUSHROOM.get());
                                output.accept(ModItems.GROUND_RED_MUSHROOM.get());
                                output.accept(ModItems.GROUND_KELP.get());
                                output.accept(ModItems.GROUND_MELON.get());
                                output.accept(ModItems.GROUND_CACTUS.get());
                                output.accept(ModItems.GROUND_LILY_OF_THE_VALLEY.get());
                                // Средняя игра
                                output.accept(ModItems.GROUND_NETHER_WART.get());
                                output.accept(ModItems.GROUND_CRIMSON_FUNGUS.get());
                                output.accept(ModItems.GROUND_WARPED_FUNGUS.get());
                                output.accept(ModItems.GROUND_CHORUS_FRUIT.get());
                                output.accept(ModItems.GROUND_GLOW_BERRIES.get());
                                output.accept(ModItems.GROUND_TORCHFLOWER.get());
                                // Поздняя игра
                                output.accept(ModItems.GROUND_BLAZE_POWDER.get());
                                output.accept(ModItems.GROUND_GHAST_TEAR.get());
                                output.accept(ModItems.GROUND_PHANTOM_MEMBRANE.get());
                                output.accept(ModItems.GROUND_WITHER_ROSE.get());
                                output.accept(ModItems.MIXTURE.get());
                            })
                            .build()
            );

    public SteamSmoke(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.register(new SteamSmokeClient());
        }
    }
}