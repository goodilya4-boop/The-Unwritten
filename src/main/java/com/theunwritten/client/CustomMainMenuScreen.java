package com.theunwritten.client;

import com.theunwritten.TheUnwritten;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CustomMainMenuScreen extends Screen {

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(
                    TheUnwritten.MODID,
                    "textures/gui/main_menu.png"
            );

    public CustomMainMenuScreen() {
        super(Component.empty());
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blit(
                BACKGROUND,
                0,
                0,
                0,
                0,
                this.width,
                this.height,
                this.width,
                this.height
        );

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
