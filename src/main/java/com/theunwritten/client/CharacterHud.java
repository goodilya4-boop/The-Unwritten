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
    private static final ResourceLocation HEALTH = texture("health_empty.png");
    private static final ResourceLocation MANA = texture("mana_empty.png");
    private static final ResourceLocation STAMINA = texture("stamina_empty.png");
    private static final ResourceLocation ENERGY = texture("energy_empty.png");
    private static final ResourceLocation EXPERIENCE = texture("experience_empty.png");
    private static final ResourceLocation LEVEL = texture("level_diamond_empty.png");
    private static final ResourceLocation ARMOR = texture("armor_indicator_empty.png");
    private static final ResourceLocation HUNGER = texture("hunger_indicator_empty.png");

    private static final int BAR_WIDTH = 84;
    private static final int BAR_HEIGHT = 12;
    private static final int BAR_GAP = 8;
    private static final int LEVEL_SIZE = 24;
    private static final int XP_WIDTH = 184;
    private static final int XP_HEIGHT = 8;
    private static final int SIDE_SIZE = 20;

    private CharacterHud() {
    }

    private static ResourceLocation texture(String name) {
        return ResourceLocation.fromNamespaceAndPath(
                TheUnwritten.MODID,
                "textures/gui/" + name
        );
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

        // Same bottom HUD area as vanilla health, hunger and experience.
        int topY = height - 62;
        int bottomY = topY + BAR_HEIGHT + 7;

        int leftBarX = centerX - LEVEL_SIZE / 2 - BAR_GAP - BAR_WIDTH;
        int rightBarX = centerX + LEVEL_SIZE / 2 + BAR_GAP;

        // Top: mana / energy.
        drawResourceBar(graphics, leftBarX, topY, BAR_WIDTH, BAR_HEIGHT,
                MANA, ResourceType.MANA, 0xFF4D75D1);
        drawResourceBar(graphics, rightBarX, topY, BAR_WIDTH, BAR_HEIGHT,
                ENERGY, ResourceType.FOCUS, 0xFFD0A13A);

        // Bottom: health / stamina.
        drawResourceBar(graphics, leftBarX, bottomY, BAR_WIDTH, BAR_HEIGHT,
                HEALTH, ResourceType.HEALTH, 0xFFB83A3A);
        drawResourceBar(graphics, rightBarX, bottomY, BAR_WIDTH, BAR_HEIGHT,
                STAMINA, ResourceType.STAMINA, 0xFFB88A3A);

        // Level diamond in the center.
        int levelX = centerX - LEVEL_SIZE / 2;
        int levelY = topY - 1;
        graphics.blit(LEVEL, levelX, levelY, 0, 0, LEVEL_SIZE, LEVEL_SIZE, LEVEL_SIZE, LEVEL_SIZE);

        String levelText = Integer.toString(minecraft.player.experienceLevel);
        int levelTextX = centerX - minecraft.font.width(levelText) / 2;
        graphics.drawString(minecraft.font, levelText, levelTextX, levelY + 8, 0xFFFFFFFF, true);

        // Experience below the whole resource cluster.
        int xpX = centerX - XP_WIDTH / 2;
        int xpY = bottomY + BAR_HEIGHT + 5;
        drawExperienceBar(graphics, minecraft, xpX, xpY);

        // Armor / hunger indicators flank the resource cluster.
        int sideY = topY + (LEVEL_SIZE - SIDE_SIZE) / 2;
        int armorX = leftBarX - SIDE_SIZE - 7;
        int hungerX = rightBarX + BAR_WIDTH + 7;

        drawSideIndicator(graphics, minecraft, armorX, sideY, ARMOR, minecraft.player.getArmorValue());
        drawSideIndicator(graphics, minecraft, hungerX, sideY, HUNGER, minecraft.player.getFoodData().getFoodLevel());
    }

    private static void drawResourceBar(
            GuiGraphics graphics,
            int x,
            int y,
            int width,
            int height,
            ResourceLocation texture,
            ResourceType type,
            int fillColor
    ) {
        double ratio = Math.max(0.0D, Math.min(1.0D, ResourceSyncClient.ratio(type)));
        int fillWidth = (int) Math.round(ratio * width);

        if (fillWidth > 0) {
            graphics.fill(x, y, x + fillWidth, y + height, fillColor);
        }

        graphics.blit(texture, x, y, 0, 0, width, height, width, height);
    }

    private static void drawExperienceBar(
            GuiGraphics graphics,
            Minecraft minecraft,
            int x,
            int y
    ) {
        double progress = Math.max(0.0D, Math.min(1.0D, minecraft.player.experienceProgress));
        int fillWidth = (int) Math.round(progress * XP_WIDTH);

        if (fillWidth > 0) {
            graphics.fill(x, y, x + fillWidth, y + XP_HEIGHT, 0xFF5FBF45);
        }

        graphics.blit(EXPERIENCE, x, y, 0, 0, XP_WIDTH, XP_HEIGHT, XP_WIDTH, XP_HEIGHT);
    }

    private static void drawSideIndicator(
            GuiGraphics graphics,
            Minecraft minecraft,
            int x,
            int y,
            ResourceLocation texture,
            int value
    ) {
        graphics.blit(texture, x, y, 0, 0, SIDE_SIZE, SIDE_SIZE, SIDE_SIZE, SIDE_SIZE);

        String text = Integer.toString(value);
        int textX = x + SIDE_SIZE / 2 - minecraft.font.width(text) / 2;
        graphics.drawString(minecraft.font, text, textX, y + 6, 0xFFFFFFFF, true);
    }
}
