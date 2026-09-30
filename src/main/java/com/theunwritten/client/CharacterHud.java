package com.theunwritten.client;

import com.theunwritten.network.ResourceSyncClient;
import com.theunwritten.character.ResourceType;
import com.theunwritten.character.StatType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.RenderGuiEvent;


@EventBusSubscriber(modid = "theunwritten", bus = Bus.GAME, value = Dist.CLIENT)
public final class CharacterHud {
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
        if (minecraft.player == null || minecraft.screen != null) {
            return;
        }

        int x = LEFT;
        int y = event.getGuiGraphics().guiHeight() - BOTTOM - (BAR_HEIGHT * 3 + GAP * 2);

        drawBar(event.getGuiGraphics(), x, y, ResourceSyncClient.ratio(ResourceType.HEALTH), "♥", "Health");
        y += BAR_HEIGHT + GAP;
        drawBar(event.getGuiGraphics(), x, y, ResourceSyncClient.ratio(ResourceType.MANA), "◆", "Mana");
        y += BAR_HEIGHT + GAP;
        drawBar(event.getGuiGraphics(), x, y, ResourceSyncClient.ratio(ResourceType.STAMINA), "◇", "Stamina");
    }

    private static void drawBar(
            GuiGraphics graphics,
            int x,
            int y,
            double ratio,
            String icon,
            String label
    ) {
        int clamped = (int) Math.round(Math.max(0.0D, Math.min(1.0D, ratio)) * BAR_WIDTH);

        graphics.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, 0x99000000);
        graphics.fill(x, y, x + clamped, y + BAR_HEIGHT, 0xFFFFFFFF);
        graphics.drawString(Minecraft.getInstance().font, icon, x - 12, y - 1, 0xFFFFFFFF);
        graphics.drawString(Minecraft.getInstance().font, label, x + BAR_WIDTH + 6, y, 0xFFFFFFFF);
    }
}
