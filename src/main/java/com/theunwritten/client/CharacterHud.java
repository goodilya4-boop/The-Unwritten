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
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = TheUnwritten.MODID, bus = Bus.GAME, value = Dist.CLIENT)
public final class CharacterHud {
    private static final int BAR_WIDTH = 150;
    private static final int BAR_HEIGHT = 16;
    private static final int BAR_GAP = 8;
    private static final int LEVEL_SIZE = 46;
    private static final int XP_WIDTH = 230;
    private static final int XP_HEIGHT = 7;

    // The Unwritten palette: dark iron UI with muted, readable resource accents.
    private static final int PANEL_FILL = 0xF0141820;
    private static final int PANEL_EDGE = 0xFF4A5360;
    private static final int PANEL_SHADOW = 0xFF202731;
    private static final int EMPTY_FILL = 0xFF2A313B;
    private static final int TEXT_PRIMARY = 0xFFF0F0EA;
    private static final int TEXT_SECONDARY = 0xFFB8BEC6;

    private static final int ENERGY_COLOR = 0xFF4C86C6;
    private static final int MANA_COLOR = 0xFFC09A55;
    private static final int HEALTH_COLOR = 0xFFB84B4B;
    private static final int STAMINA_COLOR = 0xFF5B9A62;
    private static final int XP_COLOR = 0xFF9B8548;

    private static final int ARMOR_COLOR = 0xFFB7C1CC;
    private static final int HUNGER_COLOR = 0xFFC08B4A;

    private CharacterHud() {
    }

    @SubscribeEvent
    public static void hideVanillaLayers(RenderGuiLayerEvent.Pre event) {
        ResourceLocation name = event.getName();
        if (name.equals(VanillaGuiLayers.PLAYER_HEALTH)
                || name.equals(VanillaGuiLayers.ARMOR_LEVEL)
                || name.equals(VanillaGuiLayers.FOOD_LEVEL)
                || name.equals(VanillaGuiLayers.EXPERIENCE_BAR)
                || name.equals(VanillaGuiLayers.EXPERIENCE_LEVEL)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void render(RenderGuiEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.screen != null) {
            return;
        }

        GuiGraphics graphics = event.getGuiGraphics();
        int width = graphics.guiWidth();
        int height = graphics.guiHeight();
        int centerX = width / 2;

        int topY = height - 62;
        int bottomY = topY + BAR_HEIGHT + 8;

        int leftX = centerX - LEVEL_SIZE / 2 - BAR_GAP - BAR_WIDTH;
        int rightX = centerX + LEVEL_SIZE / 2 + BAR_GAP;

        drawResourceBar(graphics, minecraft, leftX, topY, BAR_WIDTH, BAR_HEIGHT,
                ResourceType.FOCUS, "✦", ENERGY_COLOR);
        drawResourceBar(graphics, minecraft, rightX, topY, BAR_WIDTH, BAR_HEIGHT,
                ResourceType.MANA, "◆", MANA_COLOR);

        drawResourceBar(graphics, minecraft, leftX, bottomY, BAR_WIDTH, BAR_HEIGHT,
                ResourceType.HEALTH, "♥", HEALTH_COLOR);
        drawResourceBar(graphics, minecraft, rightX, bottomY, BAR_WIDTH, BAR_HEIGHT,
                ResourceType.STAMINA, "✦", STAMINA_COLOR);

        int levelX = centerX - LEVEL_SIZE / 2;
        int levelY = topY + (BAR_HEIGHT + 8) / 2 - LEVEL_SIZE / 2;
        drawLevel(graphics, minecraft, levelX, levelY);

        int xpX = centerX - XP_WIDTH / 2;
        int xpY = height - 22;
        drawExperienceBar(graphics, minecraft, xpX, xpY);

        int sideY = topY + 1;
        int armorX = leftX - 22;
        int hungerX = rightX + BAR_WIDTH + 8;

        drawSideIndicator(graphics, armorX, sideY, minecraft.player.getArmorValue(),
                ARMOR_COLOR, true);
        drawSideIndicator(graphics, hungerX, sideY, minecraft.player.getFoodData().getFoodLevel(),
                HUNGER_COLOR, false);
    }

    private static void drawResourceBar(
            GuiGraphics graphics,
            Minecraft minecraft,
            int x,
            int y,
            int width,
            int height,
            ResourceType type,
            String icon,
            int fillColor
    ) {
        double ratio = Math.max(0.0D, Math.min(1.0D, ResourceSyncClient.ratio(type)));
        int fillWidth = (int) Math.round((width - 8) * ratio);

        drawBeveledPanel(graphics, x, y, width, height);

        if (fillWidth > 0) {
            graphics.fill(x + 4, y + 4, x + 4 + fillWidth, y + height - 3, fillColor);
            graphics.fill(x + 4, y + 4, x + 4 + fillWidth, y + 5, 0x45FFFFFF);
        } else {
            graphics.fill(x + 4, y + 4, x + width - 4, y + height - 3, EMPTY_FILL);
        }

        graphics.drawString(minecraft.font, icon, x + 7, y + 3, TEXT_PRIMARY, true);

        String value = formatResourceValue(type);
        int valueX = x + width - minecraft.font.width(value) - 7;
        graphics.drawString(minecraft.font, value, valueX, y + 3, TEXT_SECONDARY, true);
    }

    private static String formatResourceValue(ResourceType type) {
        double current = ResourceSyncClient.current(type);
        double max = ResourceSyncClient.maximum(type);
        return formatNumber(current) + " / " + formatNumber(max);
    }

    private static String formatNumber(double value) {
        if (Math.abs(value - Math.rint(value)) < 0.05D) {
            return Integer.toString((int) Math.rint(value));
        }
        return String.format(java.util.Locale.ROOT, "%.1f", value);
    }

    private static void drawLevel(
            GuiGraphics graphics,
            Minecraft minecraft,
            int x,
            int y
    ) {
        int centerX = x + LEVEL_SIZE / 2;
        int centerY = y + LEVEL_SIZE / 2;

        // A true pixel-art diamond: widest at the center, tapering evenly toward both points.
        int[] widths = {6, 10, 14, 18, 22, 26, 30, 34, 38, 42, 38, 34, 30, 26, 22, 18, 14, 10, 6};
        int rowHeight = 2;

        for (int i = 0; i < widths.length; i++) {
            int rowY = y + i * rowHeight;
            int rowX = centerX - widths[i] / 2;
            int fill = (i == 0 || i == widths.length - 1)
                    ? PANEL_EDGE
                    : 0xFF151B23;

            graphics.fill(rowX, rowY, rowX + widths[i], rowY + rowHeight, fill);

            if (i > 0 && i < widths.length - 1) {
                graphics.fill(rowX, rowY, rowX + widths[i], rowY + 1, PANEL_EDGE);
            }
        }

        String level = Integer.toString(minecraft.player.experienceLevel);
        int levelX = centerX - minecraft.font.width(level) / 2;
        graphics.drawString(minecraft.font, level, levelX, centerY - 6, TEXT_PRIMARY, true);

        graphics.drawString(
                minecraft.font,
                "LVL",
                centerX - minecraft.font.width("LVL") / 2,
                centerY + 5,
                TEXT_SECONDARY,
                true
        );
    }

    private static void drawExperienceBar(
            GuiGraphics graphics,
            Minecraft minecraft,
            int x,
            int y
    ) {
        double progress = Math.max(0.0D, Math.min(1.0D, minecraft.player.experienceProgress));
        int fillWidth = (int) Math.round((XP_WIDTH - 6) * progress);

        drawBeveledPanel(graphics, x, y, XP_WIDTH, XP_HEIGHT);

        graphics.fill(x + 3, y + 2, x + XP_WIDTH - 3, y + XP_HEIGHT - 2, EMPTY_FILL);

        if (fillWidth > 0) {
            graphics.fill(x + 3, y + 2, x + 3 + fillWidth, y + XP_HEIGHT - 2, XP_COLOR);
            graphics.fill(x + 3, y + 2, x + 3 + fillWidth, y + 3, 0x55FFFFFF);
        }
    }

    private static void drawSideIndicator(
            GuiGraphics graphics,
            int x,
            int y,
            int value,
            int color,
            boolean armor
    ) {
        int points = Math.max(0, Math.min(20, value));

        // Compact two-row indicator: no panel, no title, only the actual status.
        if (armor) {
            drawShieldIcon(graphics, x + 1, y + 2, color);
        } else {
            drawHungerIcon(graphics, x + 1, y + 2, color);
        }

        for (int i = 0; i < 10; i++) {
            int row = i / 5;
            int column = i % 5;
            int px = x + 2 + column * 4;
            int py = y + 12 + row * 5;

            boolean full = points >= i * 2 + 2;
            boolean half = points == i * 2 + 1;
            drawTinyPip(graphics, px, py, full, half, color);
        }
    }

    private static void drawShieldIcon(GuiGraphics graphics, int x, int y, int color) {
        graphics.fill(x + 2, y, x + 5, y + 2, color);
        graphics.fill(x + 1, y + 2, x + 6, y + 5, color);
        graphics.fill(x + 2, y + 5, x + 5, y + 7, color);
        graphics.fill(x + 3, y + 7, x + 4, y + 8, color);
    }

    private static void drawHungerIcon(GuiGraphics graphics, int x, int y, int color) {
        graphics.fill(x + 1, y + 2, x + 6, y + 5, color);
        graphics.fill(x + 2, y + 1, x + 5, y + 6, color);
        graphics.fill(x + 1, y + 3, x + 2, y + 5, color);
    }

    private static void drawTinyPip(
            GuiGraphics graphics,
            int x,
            int y,
            boolean full,
            boolean half,
            int color
    ) {
        int empty = 0xFF303741;
        graphics.fill(x, y, x + 3, y + 3, empty);

        if (full) {
            graphics.fill(x, y, x + 3, y + 3, color);
        } else if (half) {
            graphics.fill(x, y, x + 2, y + 3, color);
        }
    }

    private static void drawBeveledPanel(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            int height
    ) {
        graphics.fill(x + 2, y, x + width - 2, y + height, PANEL_FILL);
        graphics.fill(x, y + 2, x + width, y + height - 2, PANEL_FILL);

        // Thin 1px light edge + restrained lower shadow instead of a heavy frame.
        graphics.fill(x + 3, y, x + width - 3, y + 1, PANEL_EDGE);
        graphics.fill(x + 1, y + 2, x + 2, y + height - 3, PANEL_EDGE);
        graphics.fill(x + width - 2, y + 2, x + width - 1, y + height - 3, PANEL_SHADOW);
        graphics.fill(x + 3, y + height - 1, x + width - 3, y + height, PANEL_SHADOW);

        graphics.fill(x + 1, y + 1, x + 3, y + 2, PANEL_EDGE);
        graphics.fill(x + width - 3, y + 1, x + width - 1, y + 2, PANEL_SHADOW);
    }
}
