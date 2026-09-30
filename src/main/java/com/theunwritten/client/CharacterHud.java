package com.theunwritten.client;

import com.theunwritten.TheUnwritten;
import com.theunwritten.character.CharacterAttachments;
import com.theunwritten.character.CharacterData;
import com.theunwritten.character.ResourceType;
import com.theunwritten.character.StatType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public final class CharacterHud {
    private static final ResourceLocation HEART = ResourceLocation.fromNamespaceAndPath(
            TheUnwritten.MODID, "textures/gui/hud/heart.png");
    private static final ResourceLocation MANA = ResourceLocation.fromNamespaceAndPath(
            TheUnwritten.MODID, "textures/gui/hud/mana.png");
    private static final ResourceLocation STAMINA = ResourceLocation.fromNamespaceAndPath(
            TheUnwritten.MODID, "textures/gui/hud/stamina.png");

    private static final int BAR_WIDTH = 120;
    private static final int BAR_HEIGHT = 8;
    private static final int GAP = 5;
    private static final int LEFT = 12;
    private static final int BOTTOM = 12;

    private CharacterHud() {
    }

    @SubscribeEvent
    public static void render(RenderGuiEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.screen instanceof InventoryScreen) {
            return;
        }

        CharacterData data = minecraft.player.getData(CharacterAttachments.CHARACTER_DATA);
        Map<StatType, Double> stats = data.stats();

        int x = LEFT;
        int y = event.getGuiGraphics().guiHeight() - BOTTOM
                - (BAR_HEIGHT * 3 + GAP * 2);

        drawBar(event.getGuiGraphics(), x, y, data.resources().ratio(ResourceType.HEALTH, stats),
                HEART, "Health");
        y += BAR_HEIGHT + GAP;
        drawBar(event.getGuiGraphics(), x, y, data.resources().ratio(ResourceType.MANA, stats),
                MANA, "Mana");
        y += BAR_HEIGHT + GAP;
        drawBar(event.getGuiGraphics(), x, y, data.resources().ratio(ResourceType.STAMINA, stats),
                STAMINA, "Stamina");
    }

    private static void drawBar(
            GuiGraphics graphics,
            int x,
            int y,
            double ratio,
            ResourceLocation icon,
            String label
    ) {
        int clamped = (int) Math.round(Math.max(0.0D, Math.min(1.0D, ratio)) * BAR_WIDTH);

        graphics.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, 0x99000000);
        graphics.fill(x, y, x + clamped, y + BAR_HEIGHT, 0xFFFFFFFF);
        graphics.blit(icon, x - 12, y - 1, 0, 0, 10, 10, 10, 10);

        graphics.drawString(Minecraft.getInstance().font, label, x + BAR_WIDTH + 6, y, 0xFFFFFFFF);
    }
}
