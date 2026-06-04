package com.huebit.steamsmoke;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class BlendPouchScreen extends AbstractContainerScreen<BlendPouchMenu> {

    private static final int BG        = 0xFF2D2B27;
    private static final int PANEL     = 0xFF3A3832;
    private static final int BAND      = 0xFF2A2820;
    private static final int BORDER    = 0xFF1A1815;
    private static final int SEP_DARK  = 0xFF1A1815;
    private static final int SEP_LIGHT = 0xFF5A5850;
    private static final int SLOT_OUT  = 0xFF1A1815;
    private static final int SLOT_IN   = 0xFF4E4B45;
    private static final int BTN_NORM  = 0xFF4A4640;
    private static final int BTN_HOVER = 0xFF5A5650;

    // Button position in GUI-local coordinates
    private static final int BTN_X = 58;
    private static final int BTN_Y = 150;
    private static final int BTN_W = 60;
    private static final int BTN_H = 12;

    public BlendPouchScreen(BlendPouchMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth  = 176;
        this.imageHeight = 252;
        this.inventoryLabelY = 166;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos;

        g.fill(x, y, x + imageWidth, y + imageHeight, BG);

        // Mixture panel
        g.fill(x + 7, y + 14, x + 169, y + 60, PANEL);

        // Separator 1
        g.fill(x + 7, y + 60, x + 169, y + 61, SEP_DARK);
        g.fill(x + 7, y + 61, x + 169, y + 62, SEP_LIGHT);

        // Ingredient panel
        g.fill(x + 7, y + 62, x + 169, y + 145, PANEL);

        // Button band
        g.fill(x + 7, y + 145, x + 169, y + 163, BAND);

        // Collect button
        boolean hovered = mouseX >= x + BTN_X && mouseX < x + BTN_X + BTN_W
                       && mouseY >= y + BTN_Y && mouseY < y + BTN_Y + BTN_H;
        g.fill(x + BTN_X,     y + BTN_Y,     x + BTN_X + BTN_W,     y + BTN_Y + BTN_H,     BORDER);
        g.fill(x + BTN_X + 1, y + BTN_Y + 1, x + BTN_X + BTN_W - 1, y + BTN_Y + BTN_H - 1, hovered ? BTN_HOVER : BTN_NORM);

        // Separator 2
        g.fill(x + 7, y + 163, x + 169, y + 164, SEP_DARK);
        g.fill(x + 7, y + 164, x + 169, y + 165, SEP_LIGHT);

        // Inventory panel
        g.fill(x + 7, y + 165, x + 169, y + imageHeight, PANEL);

        // Outer border
        g.fill(x,                  y,                  x + 1,           y + imageHeight, BORDER);
        g.fill(x + imageWidth - 1, y,                  x + imageWidth,  y + imageHeight, BORDER);
        g.fill(x,                  y,                  x + imageWidth,  y + 1,           BORDER);
        g.fill(x,                  y + imageHeight - 1, x + imageWidth, y + imageHeight, BORDER);

        // Mixture slot backgrounds (2 rows × 9)
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 9; col++) {
                drawSlotBg(g, x + 7 + col * 18, y + 23 + row * 18);
            }
        }

        // Ingredient slot backgrounds (4 rows × 9)
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 9; col++) {
                drawSlotBg(g, x + 7 + col * 18, y + 71 + row * 18);
            }
        }

        // Player inventory slot backgrounds (3 rows × 9)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                drawSlotBg(g, x + 7 + col * 18, y + 175 + row * 18);
            }
        }

        // Hotbar
        for (int col = 0; col < 9; col++) {
            drawSlotBg(g, x + 7 + col * 18, y + 233);
        }
    }

    private void drawSlotBg(GuiGraphics g, int x, int y) {
        g.fill(x,     y,     x + 18, y + 18, SLOT_OUT);
        g.fill(x + 1, y + 1, x + 17, y + 17, SLOT_IN);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics g, int mouseX, int mouseY) {
        int cx = imageWidth / 2;
        g.drawCenteredString(font, title, cx, 4, 0xFFE0D0A0);
        g.drawCenteredString(font,
                Component.translatable("item.steamsmoke.blend_pouch.mixtures"),
                cx, 15, 0xFFA09070);
        g.drawCenteredString(font,
                Component.translatable("item.steamsmoke.blend_pouch.ingredients"),
                cx, 63, 0xFFA09070);
        g.drawCenteredString(font,
                Component.translatable("item.steamsmoke.blend_pouch.collect"),
                cx, 153, 0xFFD0C090);
        g.drawString(font,
                Component.translatable("container.inventory"),
                8, 166, 0xFF908070, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int bx = leftPos + BTN_X, by = topPos + BTN_Y;
            if (mouseX >= bx && mouseX < bx + BTN_W && mouseY >= by && mouseY < by + BTN_H) {
                PacketDistributor.sendToServer(new CollectToPouchPacket());
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
