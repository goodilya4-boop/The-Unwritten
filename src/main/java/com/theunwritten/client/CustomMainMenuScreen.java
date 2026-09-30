package com.theunwritten.client;

import com.theunwritten.TheUnwritten;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
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

        int buttonWidth = 220;
        int buttonHeight = 20;
        int x = (this.width - buttonWidth) / 2;

        int startY = this.height / 2 - 20;
        int gap = 25;

        // Одиночная игра
        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Одиночная игра"),
                        button -> {
                            this.minecraft.setScreen(
                                    new SelectWorldScreen(this)
                            );
                        }
                ).bounds(x, startY, buttonWidth, buttonHeight).build()
        );

        // Сетевая игра
        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Сетевая игра"),
                        button -> {
                            this.minecraft.setScreen(
                                    new JoinMultiplayerScreen(this)
                            );
                        }
                ).bounds(x, startY + gap, buttonWidth, buttonHeight).build()
        );

        // Настройки
        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Настройки"),
                        button -> {
                            this.minecraft.setScreen(
                                    new OptionsScreen(this, this.minecraft.options)
                            );
                        }
                ).bounds(x, startY + gap * 2, buttonWidth, buttonHeight).build()
        );

        // Выход
        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Выход"),
                        button -> {
                            this.minecraft.stop();
                        }
                ).bounds(x, startY + gap * 3, buttonWidth, buttonHeight).build()
        );
    }

    @Override
    public void render(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        // Полностью перекрываем предыдущий кадр
        guiGraphics.fill(
                0,
                0,
                this.width,
                this.height,
                0xFF000000
        );

        // Наш фон
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

        // Кнопки
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void renderBlurredBackground(float partialTick) {
        // Отключаем стандартный Minecraft blur
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}