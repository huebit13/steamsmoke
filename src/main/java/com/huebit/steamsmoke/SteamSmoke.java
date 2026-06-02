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

    public static final DeferredBlock<Block> WALL_DRYING_RACK = BLOCKS.register("wall_drying_rack",
            () -> new WallDryingRackBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(1.5f)
                    .noOcclusion()
            )
    );
    public static final DeferredItem<BlockItem> WALL_DRYING_RACK_ITEM = ITEMS.registerSimpleBlockItem("wall_drying_rack", WALL_DRYING_RACK);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> STEAM_SMOKE_TAB =
            CREATIVE_TABS.register("steam_smoke_tab", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.steamsmoke"))
                            .icon(() -> HOOKAH_ITEM.get().getDefaultInstance())
                            .displayItems((params, output) -> {
                                // ── Блоки и инструменты ───────────────────────────────
                                output.accept(HOOKAH_ITEM.get());
                                output.accept(MORTAR_ITEM.get());
                                output.accept(DRYING_RACK_ITEM.get());
                                output.accept(WALL_DRYING_RACK_ITEM.get());
                                output.accept(ModItems.PESTLE.get());
                                // ── Сырьё ────────────────────────────────────────────
                                output.accept(ModItems.TOBACCO_LEAF.get());
                                // ── Высушенные ───────────────────────────────────────
                                output.accept(ModItems.DRIED_TOBACCO_LEAF.get());
                                output.accept(ModItems.DRIED_APPLE.get());
                                output.accept(ModItems.DRIED_BROWN_MUSHROOM.get());
                                output.accept(ModItems.DRIED_RED_MUSHROOM.get());
                                output.accept(ModItems.DRIED_KELP_HERB.get());
                                output.accept(ModItems.DRIED_MELON.get());
                                output.accept(ModItems.DRIED_CACTUS.get());
                                output.accept(ModItems.DRIED_NETHER_WART.get());
                                output.accept(ModItems.DRIED_CRIMSON_FUNGUS.get());
                                output.accept(ModItems.DRIED_WARPED_FUNGUS.get());
                                output.accept(ModItems.DRIED_CHORUS_FRUIT.get());
                                output.accept(ModItems.DRIED_GLOW_BERRIES.get());
                                output.accept(ModItems.DRIED_PHANTOM_MEMBRANE.get());
                                output.accept(ModItems.DRIED_RABBIT_HIDE.get());
                                output.accept(ModItems.DRIED_INK_SAC.get());
                                output.accept(ModItems.DRIED_SLIMEBALL.get());
                                output.accept(ModItems.DRIED_SEA_PICKLE.get());
                                output.accept(ModItems.DRIED_MAGMA_CREAM.get());
                                output.accept(ModItems.DRIED_SPIDER_EYE.get());
                                output.accept(ModItems.DRIED_FERMENTED_SPIDER_EYE.get());
                                output.accept(ModItems.DRIED_BAMBOO.get());
                                output.accept(ModItems.DRIED_PUFFERFISH.get());
                                output.accept(ModItems.DRIED_LEAVES.get());
                                // ── Сырые перемолотые (wet_ground) ───────────────────
                                output.accept(ModItems.WET_GROUND_APPLE.get());
                                output.accept(ModItems.WET_GROUND_BROWN_MUSHROOM.get());
                                output.accept(ModItems.WET_GROUND_RED_MUSHROOM.get());
                                output.accept(ModItems.WET_GROUND_KELP.get());
                                output.accept(ModItems.WET_GROUND_MELON.get());
                                output.accept(ModItems.WET_GROUND_CACTUS.get());
                                output.accept(ModItems.WET_GROUND_NETHER_WART.get());
                                output.accept(ModItems.WET_GROUND_CRIMSON_FUNGUS.get());
                                output.accept(ModItems.WET_GROUND_WARPED_FUNGUS.get());
                                output.accept(ModItems.WET_GROUND_CHORUS_FRUIT.get());
                                output.accept(ModItems.WET_GROUND_GLOW_BERRIES.get());
                                output.accept(ModItems.WET_GROUND_PHANTOM_MEMBRANE.get());
                                output.accept(ModItems.WET_GROUND_INK_SAC.get());
                                output.accept(ModItems.WET_GROUND_SLIMEBALL.get());
                                output.accept(ModItems.WET_GROUND_SEA_PICKLE.get());
                                output.accept(ModItems.WET_GROUND_MAGMA_CREAM.get());
                                output.accept(ModItems.WET_GROUND_SPIDER_EYE.get());
                                output.accept(ModItems.WET_GROUND_FERMENTED_SPIDER_EYE.get());
                                output.accept(ModItems.WET_GROUND_BAMBOO.get());
                                // ── Сушёные перемолотые (dried_ground) ───────────────
                                output.accept(ModItems.DRIED_GROUND_TOBACCO.get());
                                output.accept(ModItems.DRIED_GROUND_APPLE.get());
                                output.accept(ModItems.DRIED_GROUND_BROWN_MUSHROOM.get());
                                output.accept(ModItems.DRIED_GROUND_RED_MUSHROOM.get());
                                output.accept(ModItems.DRIED_GROUND_KELP.get());
                                output.accept(ModItems.DRIED_GROUND_MELON.get());
                                output.accept(ModItems.DRIED_GROUND_CACTUS.get());
                                output.accept(ModItems.DRIED_GROUND_NETHER_WART.get());
                                output.accept(ModItems.DRIED_GROUND_CRIMSON_FUNGUS.get());
                                output.accept(ModItems.DRIED_GROUND_WARPED_FUNGUS.get());
                                output.accept(ModItems.DRIED_GROUND_CHORUS_FRUIT.get());
                                output.accept(ModItems.DRIED_GROUND_GLOW_BERRIES.get());
                                output.accept(ModItems.DRIED_GROUND_PHANTOM_MEMBRANE.get());
                                output.accept(ModItems.DRIED_GROUND_RABBIT_HIDE.get());
                                output.accept(ModItems.DRIED_GROUND_INK_SAC.get());
                                output.accept(ModItems.DRIED_GROUND_SLIMEBALL.get());
                                output.accept(ModItems.DRIED_GROUND_SEA_PICKLE.get());
                                output.accept(ModItems.DRIED_GROUND_MAGMA_CREAM.get());
                                output.accept(ModItems.DRIED_GROUND_SPIDER_EYE.get());
                                output.accept(ModItems.DRIED_GROUND_FERMENTED_SPIDER_EYE.get());
                                output.accept(ModItems.DRIED_GROUND_BAMBOO.get());
                                output.accept(ModItems.DRIED_GROUND_PUFFERFISH.get());
                                output.accept(ModItems.DRIED_GROUND_LEAVES.get());
                                // ── Обычные перемолотые ───────────────────────────────
                                output.accept(ModItems.GROUND_SUGAR.get());
                                output.accept(ModItems.GROUND_SWEET_BERRIES.get());
                                output.accept(ModItems.GROUND_HONEYCOMB.get());
                                output.accept(ModItems.GROUND_DANDELION.get());
                                output.accept(ModItems.GROUND_POPPY.get());
                                output.accept(ModItems.GROUND_LILY_OF_THE_VALLEY.get());
                                output.accept(ModItems.GROUND_TORCHFLOWER.get());
                                output.accept(ModItems.GROUND_BLAZE_POWDER.get());
                                output.accept(ModItems.GROUND_GHAST_TEAR.get());
                                output.accept(ModItems.GROUND_WITHER_ROSE.get());
                                output.accept(ModItems.GROUND_ALLIUM.get());
                                output.accept(ModItems.GROUND_SUNFLOWER.get());
                                output.accept(ModItems.GROUND_CORNFLOWER.get());
                                output.accept(ModItems.GROUND_AZURE_BLUET.get());
                                output.accept(ModItems.GROUND_OXEYE_DAISY.get());
                                output.accept(ModItems.GROUND_RED_TULIP.get());
                                output.accept(ModItems.GROUND_ORANGE_TULIP.get());
                                output.accept(ModItems.GROUND_WHITE_TULIP.get());
                                output.accept(ModItems.GROUND_PINK_TULIP.get());
                                output.accept(ModItems.GROUND_LILAC.get());
                                output.accept(ModItems.GROUND_PEONY.get());
                                output.accept(ModItems.GROUND_ROSE_BUSH.get());
                                output.accept(ModItems.GROUND_CARROT.get());
                                output.accept(ModItems.GROUND_BEETROOT.get());
                                output.accept(ModItems.GROUND_POTATO.get());
                                output.accept(ModItems.GROUND_COCOA_BEANS.get());
                                output.accept(ModItems.GROUND_SPORE_BLOSSOM.get());
                                output.accept(ModItems.GROUND_PRISMARINE_SHARD.get());
                                output.accept(ModItems.GROUND_ENDER_PEARL.get());
                                output.accept(ModItems.GROUND_PINK_PETALS.get());
                                output.accept(ModItems.GROUND_GUNPOWDER.get());
                                output.accept(ModItems.GROUND_REDSTONE.get());
                                output.accept(ModItems.GROUND_MOSS.get());
                                output.accept(ModItems.GROUND_DRAGON_EGG.get());
                                output.accept(ModItems.GROUND_AMETHYST.get());
                                output.accept(ModItems.GROUND_WEEPING_VINES.get());
                                output.accept(ModItems.GROUND_PUMPKIN.get());
                                output.accept(ModItems.GROUND_COAL.get());
                                output.accept(ModItems.GROUND_FIRE_CHARGE.get());
                                output.accept(ModItems.GROUND_BONE_MEAL.get());
                                output.accept(ModItems.GROUND_WIND_CHARGE.get());
                                output.accept(ModItems.GROUND_TOTEM_OF_UNDYING.get());
                                output.accept(ModItems.GROUND_TNT.get());
                                output.accept(ModItems.GROUND_END_CRYSTAL.get());
                                output.accept(ModItems.GROUND_IRON_NUGGET.get());
                                output.accept(ModItems.GROUND_GOLD_NUGGET.get());
                                output.accept(ModItems.GROUND_NETHERITE.get());
                                output.accept(ModItems.GROUND_NETHER_STAR.get());
                                output.accept(ModItems.GROUND_ECHO_SHARD.get());
                                output.accept(ModItems.GROUND_GOLDEN_CARROT.get());
                                // ── Замес ─────────────────────────────────────────────
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