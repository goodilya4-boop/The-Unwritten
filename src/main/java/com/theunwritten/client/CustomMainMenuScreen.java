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

        int buttonWidth = 140;
        int buttonHeight = 25;
        int x = (this.width - buttonWidth) / 2;

        int startY = this.height / 2 - 20;
        int gap = 30;

        // Одиночная игра
        this.addRenderableWidget(
                new CustomButton(
                        x,
                        startY,
                        buttonWidth,
                        buttonHeight,
                        Component.literal("Одиночная игра"),
                        button -> {
                            this.minecraft.setScreen(
                                    new SelectWorldScreen(this)
                            );
                        }
                )
        );

        // Сетевая игра
        this.addRenderableWidget(
                new CustomButton(
                        x,
                        startY + gap,
                        buttonWidth,
                        buttonHeight,
                        Component.literal("Сетевая игра"),
                        button -> {
                            this.minecraft.setScreen(
                                    new JoinMultiplayerScreen(this)
                            );
                        }
                )
        );

        // Настройки
        this.addRenderableWidget(
                new CustomButton(
                        x,
                        startY + gap * 2,
                        buttonWidth,
                        buttonHeight,
                        Component.literal("Настройки"),
                        button -> {
                            this.minecraft.setScreen(
                                    new OptionsScreen(this, this.minecraft.options)
                            );
                        }
                )
        );

        // Выход
        this.addRenderableWidget(
                new CustomButton(
                        x,
                        startY + gap * 3,
                        buttonWidth,
                        buttonHeight,
                        Component.literal("Выход"),
                        button -> {
                            this.minecraft.stop();
                        }
                )
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