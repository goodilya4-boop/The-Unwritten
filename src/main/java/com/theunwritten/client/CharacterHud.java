package com.theunwritten.client;

import com.theunwritten.TheUnwritten;
import com.theunwritten.character.ResourceType;
import com.theunwritten.network.ResourceSyncClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.client.renderer.texture.NativeImage;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    private static final int RESOURCE_MAX_WIDTH = 150;
    private static final int RESOURCE_MAX_HEIGHT = 20;
    private static final int RESOURCE_GAP = 8;
    private static final int LEVEL_MAX_SIZE = 38;
    private static final int XP_MAX_WIDTH = 220;
    private static final int XP_MAX_HEIGHT = 12;
    private static final int SIDE_MAX_SIZE = 26;

    private static final Map<ResourceLocation, TextureSize> TEXTURE_SIZES = new HashMap<>();

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

        TextureSize resourceSize = getTextureSize(minecraft, HEALTH);
        int resourceWidth = scaledWidth(resourceSize, RESOURCE_MAX_WIDTH, RESOURCE_MAX_HEIGHT);
        int resourceHeight = scaledHeight(resourceSize, RESOURCE_MAX_WIDTH, RESOURCE_MAX_HEIGHT);

        TextureSize levelSize = getTextureSize(minecraft, LEVEL);
        int levelWidth = scaledWidth(levelSize, LEVEL_MAX_SIZE, LEVEL_MAX_SIZE);
        int levelHeight = scaledHeight(levelSize, LEVEL_MAX_SIZE, LEVEL_MAX_SIZE);

        int topY = height - 68;
        int bottomY = topY + resourceHeight + 7;

        int leftBarX = centerX - levelWidth / 2 - RESOURCE_GAP - resourceWidth;
        int rightBarX = centerX + levelWidth / 2 + RESOURCE_GAP;

        // Top: energy / mana.
        drawResourceBar(graphics, minecraft, leftBarX, topY, resourceWidth, resourceHeight,
                ENERGY, ResourceType.FOCUS, 0xFF3F9DFF);
        drawResourceBar(graphics, minecraft, rightBarX, topY, resourceWidth, resourceHeight,
                MANA, ResourceType.MANA, 0xFFD2A63A);

        // Bottom: health / stamina.
        drawResourceBar(graphics, minecraft, leftBarX, bottomY, resourceWidth, resourceHeight,
                HEALTH, ResourceType.HEALTH, 0xFFE04444);
        drawResourceBar(graphics, minecraft, rightBarX, bottomY, resourceWidth, resourceHeight,
                STAMINA, ResourceType.STAMINA, 0xFF4CAF50);

        // Level diamond in the center.
        int levelX = centerX - levelWidth / 2;
        int levelY = topY + (resourceHeight - levelHeight) / 2;
        drawTexture(graphics, minecraft, LEVEL, levelX, levelY, levelWidth, levelHeight);

        String levelText = Integer.toString(minecraft.player.experienceLevel);
        int levelTextX = centerX - minecraft.font.width(levelText) / 2;
        int levelTextY = levelY + levelHeight / 2 - 4;
        graphics.drawString(minecraft.font, levelText, levelTextX, levelTextY, 0xFFFFFFFF, true);

        // Experience below the complete resource cluster.
        TextureSize xpSize = getTextureSize(minecraft, EXPERIENCE);
        int xpWidth = scaledWidth(xpSize, XP_MAX_WIDTH, XP_MAX_HEIGHT);
        int xpHeight = scaledHeight(xpSize, XP_MAX_WIDTH, XP_MAX_HEIGHT);
        int xpX = centerX - xpWidth / 2;
        int xpY = bottomY + resourceHeight + 5;
        drawExperienceBar(graphics, minecraft, xpX, xpY, xpWidth, xpHeight);

        // Armor / hunger indicators flank the resource cluster.
        TextureSize armorSize = getTextureSize(minecraft, ARMOR);
        TextureSize hungerSize = getTextureSize(minecraft, HUNGER);

        int armorWidth = scaledWidth(armorSize, SIDE_MAX_SIZE, SIDE_MAX_SIZE);
        int armorHeight = scaledHeight(armorSize, SIDE_MAX_SIZE, SIDE_MAX_SIZE);
        int hungerWidth = scaledWidth(hungerSize, SIDE_MAX_SIZE, SIDE_MAX_SIZE);
        int hungerHeight = scaledHeight(hungerSize, SIDE_MAX_SIZE, SIDE_MAX_SIZE);

        int armorY = topY + (levelHeight - armorHeight) / 2;
        int hungerY = topY + (levelHeight - hungerHeight) / 2;
        int armorX = leftBarX - armorWidth - 8;
        int hungerX = rightBarX + resourceWidth + 8;

        drawSideIndicator(graphics, minecraft, armorX, armorY, armorWidth, armorHeight,
                ARMOR, minecraft.player.getArmorValue());
        drawSideIndicator(graphics, minecraft, hungerX, hungerY, hungerWidth, hungerHeight,
                HUNGER, minecraft.player.getFoodData().getFoodLevel());
    }

    private static void drawResourceBar(
            GuiGraphics graphics,
            Minecraft minecraft,
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

        drawTexture(graphics, minecraft, texture, x, y, width, height);
    }

    private static void drawExperienceBar(
            GuiGraphics graphics,
            Minecraft minecraft,
            int x,
            int y,
            int width,
            int height
    ) {
        double progress = Math.max(0.0D, Math.min(1.0D, minecraft.player.experienceProgress));
        int fillWidth = (int) Math.round(progress * width);

        if (fillWidth > 0) {
            graphics.fill(x, y, x + fillWidth, y + height, 0xFF5FBF45);
        }

        drawTexture(graphics, minecraft, EXPERIENCE, x, y, width, height);
    }

    private static void drawSideIndicator(
            GuiGraphics graphics,
            Minecraft minecraft,
            int x,
            int y,
            int width,
            int height,
            ResourceLocation texture,
            int value
    ) {
        drawTexture(graphics, minecraft, texture, x, y, width, height);

        String text = Integer.toString(value);
        int textX = x + width / 2 - minecraft.font.width(text) / 2;
        int textY = y + height / 2 - 4;
        graphics.drawString(minecraft.font, text, textX, textY, 0xFFFFFFFF, true);
    }

    private static void drawTexture(
            GuiGraphics graphics,
            Minecraft minecraft,
            ResourceLocation texture,
            int x,
            int y,
            int width,
            int height
    ) {
        TextureSize size = getTextureSize(minecraft, texture);
        graphics.blit(texture, x, y, 0, 0, width, height, size.width(), size.height());
    }

    private static TextureSize getTextureSize(Minecraft minecraft, ResourceLocation texture) {
        return TEXTURE_SIZES.computeIfAbsent(texture, key -> {
            ResourceManager manager = minecraft.getResourceManager();
            try {
                Resource resource = manager.getResource(key).orElseThrow();
                try (NativeImage image = NativeImage.read(resource.open())) {
                    return new TextureSize(image.getWidth(), image.getHeight());
                }
            } catch (IOException | RuntimeException exception) {
                return new TextureSize(1, 1);
            }
        });
    }

    private static int scaledWidth(TextureSize size, int maxWidth, int maxHeight) {
        double scale = Math.min(
                maxWidth / (double) size.width(),
                maxHeight / (double) size.height()
        );
        return Math.max(1, (int) Math.round(size.width() * scale));
    }

    private static int scaledHeight(TextureSize size, int maxWidth, int maxHeight) {
        double scale = Math.min(
                maxWidth / (double) size.width(),
                maxHeight / (double) size.height()
        );
        return Math.max(1, (int) Math.round(size.height() * scale));
    }

    private record TextureSize(int width, int height) {
    }
}
