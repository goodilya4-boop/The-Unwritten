package com.theunwritten.client;

import com.theunwritten.TheUnwritten;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CustomButton extends Button {

    private static final ResourceLocation NORMAL =
            ResourceLocation.fromNamespaceAndPath(
                    TheUnwritten.MODID,
                    "textures/gui/button.png"
            );

    private static final ResourceLocation HOVER =
            ResourceLocation.fromNamespaceAndPath(
                    TheUnwritten.MODID,
                    "textures/gui/button_hover.png"
            );

    private static final ResourceLocation PRESSED =
            ResourceLocation.fromNamespaceAndPath(
                    TheUnwritten.MODID,
                    "textures/gui/button_pressed.png"
            );

    public CustomButton(
            int x,
            int y,
            int width,
            int height,
            Component message,
            OnPress onPress
    ) {
        super(
                x,
                y,
                width,
                height,
                message,
                onPress,
                DEFAULT_NARRATION
        );
    }

    @Override
    protected void renderWidget(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        ResourceLocation texture;

        if (this.isFocused() && this.active) {
            texture = PRESSED;
        } else if (this.isHovered()) {
            texture = HOVER;
        } else {
            texture = NORMAL;
        }

        guiGraphics.blit(
                texture,
                this.getX(),
                this.getY(),
                0,
                0,
                this.width,
                this.height,
                this.width,
                this.height
        );

        // Текст кнопки
        guiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                this.getMessage(),
                this.getX() + this.width / 2,
                this.getY() + (this.height - 8) / 2,
                0xFFFFFFFF
        );
    }
}