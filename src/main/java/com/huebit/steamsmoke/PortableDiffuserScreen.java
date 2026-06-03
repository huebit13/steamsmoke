package com.huebit.steamsmoke;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class PortableDiffuserScreen extends AbstractContainerScreen<PortableDiffuserMenu> {

    // Colours
    private static final int BG        = 0xFF2D2B27;
    private static final int PANEL     = 0xFF3A3832;
    private static final int BORDER    = 0xFF1A1815;
    private static final int SEP_DARK  = 0xFF1A1815;
    private static final int SEP_LIGHT = 0xFF5A5850;
    private static final int SLOT_OUT  = 0xFF1A1815;
    private static final int SLOT_IN   = 0xFF4E4B45;

    public PortableDiffuserScreen(PortableDiffuserMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth  = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos;

        // Main panel
        g.fill(x, y, x + imageWidth, y + imageHeight, BG);

        // Top area (diffuser section)
        g.fill(x + 7, y + 16, x + imageWidth - 7, y + 62, PANEL);

        // Separator
        g.fill(x + 7, y + 62, x + imageWidth - 7, y + 63, SEP_DARK);
        g.fill(x + 7, y + 63, x + imageWidth - 7, y + 64, SEP_LIGHT);

        // Inventory area
        g.fill(x + 7, y + 64, x + imageWidth - 7, y + imageHeight - 7, PANEL);

        // Outer border
        g.fill(x,                  y,                  x + 1,           y + imageHeight, BORDER);
        g.fill(x + imageWidth - 1, y,                  x + imageWidth,  y + imageHeight, BORDER);
        g.fill(x,                  y,                  x + imageWidth,  y + 1,           BORDER);
        g.fill(x,                  y + imageHeight - 1, x + imageWidth, y + imageHeight, BORDER);

        // Mixture slot background (slot is at MIXTURE_SLOT_X, MIXTURE_SLOT_Y → bg 1px outside)
        drawSlotBg(g, x + PortableDiffuserMenu.MIXTURE_SLOT_X - 1,
                      y + PortableDiffuserMenu.MIXTURE_SLOT_Y - 1);

        // Player inventory slot backgrounds
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                drawSlotBg(g, x + 7 + col * 18, y + 83 + row * 18);
            }
        }
        // Hotbar
        for (int col = 0; col < 9; col++) {
            drawSlotBg(g, x + 7 + col * 18, y + 141);
        }
    }

    private void drawSlotBg(GuiGraphics g, int x, int y) {
        g.fill(x,     y,     x + 18, y + 18, SLOT_OUT);
        g.fill(x + 1, y + 1, x + 17, y + 17, SLOT_IN);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics g, int mouseX, int mouseY) {
        int cx = imageWidth / 2;
        // Title
        g.drawCenteredString(font, title, cx, 5, 0xFFE0D0A0);
        // "Смесь" label above slot
        g.drawCenteredString(font,
                Component.translatable("item.steamsmoke.portable_diffuser.slot_label"),
                cx, 17, 0xFFA09070);
        // Inventory label
        g.drawString(font,
                Component.translatable("container.inventory"),
                8, imageHeight - 94, 0xFF908070, false);
    }
}
