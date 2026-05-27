package com.huebit.steamsmoke;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
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

    // РЕГИСТРАЦИЯ СТУПКИ (Убирает предупреждение "Class MortarBlock is never used")
    public static final DeferredBlock<Block> MORTAR = BLOCKS.register("mortar",
            () -> new MortarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(2.0f)
                    .noOcclusion()
            )
    );
    public static final DeferredItem<BlockItem> MORTAR_ITEM = ITEMS.registerSimpleBlockItem("mortar", MORTAR);

    public SteamSmoke(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus); // Вызываем, чтобы убрать "Method register is never used"

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.register(new SteamSmokeClient());
        }
    }
}