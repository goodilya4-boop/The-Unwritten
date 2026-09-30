package com.theunwritten.client;

import com.theunwritten.TheUnwritten;
import com.theunwritten.character.ResourceType;
import com.theunwritten.network.ResourceSyncClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = TheUnwritten.MODID, bus = Bus.GAME, value = Dist.CLIENT)
public final class CharacterHud {
    private static final ResourceLocation PANEL =
            ResourceLocation.fromNamespaceAndPath(
                    TheUnwritten.MODID,
                    "textures/gui/button.png"
            );

    private static final int PANEL_WIDTH = 180;
    private static final int PANEL_HEIGHT = 24;
    private static final int BAR_WIDTH = 116;
    private static final int BAR_HEIGHT = 7;
    private static final int GAP = 4;
    private static final int LEFT = 12;
    private static final int BOTTOM = 12;

    private CharacterHud() {
    }

    @SubscribeEvent
    public static void render(RenderGuiEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.screen != null) {
            return;
        }

        GuiGraphics graphics = event.getGuiGraphics();
        int x = LEFT;
        int y = graphics.guiHeight() - BOTTOM - (PANEL_HEIGHT * 3 + GAP * 2);

        drawResource(graphics, x, y, ResourceType.HEALTH, "♥", "Здоровье", 0xFFB83A3A);
        y += PANEL_HEIGHT + GAP;
        drawResource(graphics, x, y, ResourceType.MANA, "◆", "Мана", 0xFF4A72B8);
        y += PANEL_HEIGHT + GAP;
        drawResource(graphics, x, y, ResourceType.STAMINA, "◇", "Выносливость", 0xFFB89A3A);
    }

    private static void drawResource(
            GuiGraphics graphics,
            int x,
            int y,
            ResourceType type,
            String icon,
            String label,
            int fillColor
    ) {
        double ratio = Math.max(0.0D, Math.min(1.0D, ResourceSyncClient.ratio(type)));
        int fillWidth = (int) Math.round(ratio * BAR_WIDTH);

        // Тот же материал, что используется у кнопок главного меню.
        graphics.blit(
                PANEL,
                x,
                y,
                0,
                0,
                PANEL_WIDTH,
                PANEL_HEIGHT,
                PANEL_WIDTH,
                PANEL_HEIGHT
        );

        Minecraft minecraft = Minecraft.getInstance();

        int textX = x + 8;
        int barX = x + 8;
        int barY = y + 13;

        graphics.drawString(
                minecraft.font,
                icon,
                textX,
                y + 4,
                0xFFFFFFFF
        );

        graphics.drawString(
                minecraft.font,
                label,
                textX + 12,
                y + 4,
                0xFFFFFFFF
        );

        // Внутренняя рамка ресурса.
        graphics.fill(
                barX,
                barY,
                barX + BAR_WIDTH,
                barY + BAR_HEIGHT,
                0xAA000000
        );

        if (fillWidth > 0) {
            graphics.fill(
                    barX,
                    barY,
                    barX + fillWidth,
                    barY + BAR_HEIGHT,
                    fillColor
            );
        }

        // Тонкий светлый кант, чтобы полосы визуально совпадали с UI кнопок.
        graphics.fill(barX, barY, barX + BAR_WIDTH, barY + 1, 0x55FFFFFF);
        graphics.fill(barX, barY + BAR_HEIGHT - 1, barX + BAR_WIDTH, barY + BAR_HEIGHT, 0x33000000);
    }
}
