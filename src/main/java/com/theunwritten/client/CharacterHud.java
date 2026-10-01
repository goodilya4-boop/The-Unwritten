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
    private static final int BAR_HEIGHT = 18;
    private static final int BAR_GAP = 8;
    private static final int LEVEL_WIDTH = 42;
    private static final int LEVEL_HEIGHT = 42;
    private static final int XP_WIDTH = 230;
    private static final int XP_HEIGHT = 10;
    private static final int SIDE_WIDTH = 28;
    private static final int SIDE_HEIGHT = 58;

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

        int leftX = centerX - LEVEL_WIDTH / 2 - BAR_GAP - BAR_WIDTH;
        int rightX = centerX + LEVEL_WIDTH / 2 + BAR_GAP;

        drawResourceBar(graphics, minecraft, leftX, topY, BAR_WIDTH, BAR_HEIGHT,
                ResourceType.FOCUS, "ЭНЕРГИЯ", "✦", 0xFF318DFF);
        drawResourceBar(graphics, minecraft, rightX, topY, BAR_WIDTH, BAR_HEIGHT,
                ResourceType.MANA, "МАНА", "◆", 0xFFE0AA35);

        drawResourceBar(graphics, minecraft, leftX, bottomY, BAR_WIDTH, BAR_HEIGHT,
                ResourceType.HEALTH, "ЗДОРОВЬЕ", "♥", 0xFFE43F3F);
        drawResourceBar(graphics, minecraft, rightX, bottomY, BAR_WIDTH, BAR_HEIGHT,
                ResourceType.STAMINA, "ВЫНОСЛИВОСТЬ", "✦", 0xFF49B957);

        int levelX = centerX - LEVEL_WIDTH / 2;
        int levelY = topY + (BAR_HEIGHT + 8) / 2 - LEVEL_HEIGHT / 2;
        drawLevel(graphics, minecraft, levelX, levelY);

        int xpX = centerX - XP_WIDTH / 2;
        int xpY = height - 25;
        drawExperienceBar(graphics, minecraft, xpX, xpY);

        int sideY = topY - 4;
        int armorX = leftX - SIDE_WIDTH - 8;
        int hungerX = rightX + BAR_WIDTH + 8;

        drawSidePanel(graphics, minecraft, armorX, sideY, "БРОНЯ",
                minecraft.player.getArmorValue(), 0xFFBFC9D6, true);
        drawSidePanel(graphics, minecraft, hungerX, sideY, "ГОЛОД",
                minecraft.player.getFoodData().getFoodLevel(), 0xFFE5A33D, false);
    }

    private static void drawResourceBar(
            GuiGraphics graphics,
            Minecraft minecraft,
            int x,
            int y,
            int width,
            int height,
            ResourceType type,
            String label,
            String icon,
            int fillColor
    ) {
        double ratio = Math.max(0.0D, Math.min(1.0D, ResourceSyncClient.ratio(type)));
        int fillWidth = (int) Math.round((width - 8) * ratio);

        drawBeveledPanel(graphics, x, y, width, height, 0xFF10151D, 0xFF667080);

        graphics.fill(x + 4, y + 4, x + 4 + fillWidth, y + height - 4, fillColor);
        if (fillWidth > 0) {
            graphics.fill(x + 4, y + 4, x + 4 + fillWidth, y + 6, 0x55FFFFFF);
        }

        graphics.drawString(minecraft.font, icon, x + 7, y + 3, 0xFFFFFFFF, true);
        graphics.drawString(minecraft.font, label, x + 25, y + 3, 0xFFFFFFFF, true);

        String value = formatResourceValue(type);
        int valueX = x + width - minecraft.font.width(value) - 8;
        graphics.drawString(minecraft.font, value, valueX, y + 3, 0xFFE7E7E7, true);
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
        int centerX = x + LEVEL_WIDTH / 2;
        int centerY = y + LEVEL_HEIGHT / 2;
        int[] widths = {8, 16, 24, 32, 38, 32, 24, 16, 8};

        for (int i = 0; i < widths.length; i++) {
            int rowY = y + i * 5 - 1;
            int rowX = centerX - widths[i] / 2;
            graphics.fill(rowX, rowY, rowX + widths[i], rowY + 5, 0xFF121821);
            if (i > 0 && i < widths.length - 1) {
                graphics.fill(rowX, rowY, rowX + widths[i], rowY + 1, 0xFF657181);
            }
        }

        String level = Integer.toString(minecraft.player.experienceLevel);
        int levelX = centerX - minecraft.font.width(level) / 2;
        graphics.drawString(minecraft.font, level, levelX, centerY - 6, 0xFFFFFFFF, true);
        graphics.drawString(minecraft.font, "УРОВЕНЬ",
                centerX - minecraft.font.width("УРОВЕНЬ") / 2,
                centerY + 5,
                0xFFBFC5CC,
                true);
    }

    private static void drawExperienceBar(
            GuiGraphics graphics,
            Minecraft minecraft,
            int x,
            int y
    ) {
        double progress = Math.max(0.0D, Math.min(1.0D, minecraft.player.experienceProgress));
        int fillWidth = (int) Math.round((XP_WIDTH - 8) * progress);

        drawBeveledPanel(graphics, x, y, XP_WIDTH, XP_HEIGHT, 0xFF10151D, 0xFF667080);

        if (fillWidth > 0) {
            graphics.fill(x + 4, y + 3, x + 4 + fillWidth, y + XP_HEIGHT - 3, 0xFF58B84A);
            graphics.fill(x + 4, y + 3, x + 4 + fillWidth, y + 4, 0x77FFFFFF);
        }

        graphics.drawString(minecraft.font, "ОПЫТ", x + 8, y + 1, 0xFFFFFFFF, true);
    }

    private static void drawSidePanel(
            GuiGraphics graphics,
            Minecraft minecraft,
            int x,
            int y,
            String title,
            int value,
            int iconColor,
            boolean armor
    ) {
        drawBeveledPanel(graphics, x, y, SIDE_WIDTH, SIDE_HEIGHT, 0xFF10151D, 0xFF667080);

        int titleX = x + SIDE_WIDTH / 2 - minecraft.font.width(title) / 2;
        graphics.drawString(minecraft.font, title, titleX, y + 3, 0xFFFFFFFF, true);

        int points = Math.max(0, Math.min(20, value));
        for (int i = 0; i < 10; i++) {
            int row = i / 2;
            int column = i % 2;
            int px = x + 6 + column * 9;
            int py = y + 17 + row * 7;

            boolean filled = points >= i * 2 + 1;
            boolean half = points == i * 2 + 1;

            if (armor) {
                drawShieldPip(graphics, px, py, filled, half, iconColor);
            } else {
                drawHungerPip(graphics, px, py, filled, half, iconColor);
            }
        }
    }

    private static void drawShieldPip(
            GuiGraphics graphics,
            int x,
            int y,
            boolean filled,
            boolean half,
            int color
    ) {
        int c = filled ? color : 0xFF343B45;
        graphics.fill(x + 2, y, x + 5, y + 2, c);
        graphics.fill(x + 1, y + 2, x + 6, y + 5, c);
        graphics.fill(x + 2, y + 5, x + 5, y + 7, c);
        if (half) {
            graphics.fill(x + 1, y + 2, x + 4, y + 5, color);
        }
    }

    private static void drawHungerPip(
            GuiGraphics graphics,
            int x,
            int y,
            boolean filled,
            boolean half,
            int color
    ) {
        int c = filled ? color : 0xFF343B45;
        graphics.fill(x + 1, y + 1, x + 6, y + 5, c);
        graphics.fill(x + 2, y, x + 5, y + 6, c);
        if (half) {
            graphics.fill(x + 1, y + 1, x + 4, y + 5, color);
        }
    }

    private static void drawBeveledPanel(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            int height,
            int fillColor,
            int borderColor
    ) {
        graphics.fill(x + 3, y, x + width - 3, y + height, fillColor);
        graphics.fill(x, y + 3, x + width, y + height - 3, fillColor);

        graphics.fill(x + 3, y, x + width - 3, y + 2, borderColor);
        graphics.fill(x + 3, y + height - 2, x + width - 3, y + height, 0xFF252C36);
        graphics.fill(x, y + 3, x + 2, y + height - 3, borderColor);
        graphics.fill(x + width - 2, y + 3, x + width, y + height - 3, 0xFF252C36);

        graphics.fill(x + 1, y + 2, x + 3, y + 4, borderColor);
        graphics.fill(x + width - 3, y + 2, x + width - 1, y + 4, borderColor);
        graphics.fill(x + 1, y + height - 4, x + 3, y + height - 2, 0xFF252C36);
        graphics.fill(x + width - 3, y + height - 4, x + width - 1, y + height - 2, 0xFF252C36);
    }
}
