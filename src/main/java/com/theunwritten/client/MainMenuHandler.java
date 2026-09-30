package com.theunwritten.client;

import net.minecraft.client.gui.screens.TitleScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(
        modid = "theunwritten",
        value = Dist.CLIENT
)
public final class MainMenuHandler {

    private static boolean replacing = false;

    private MainMenuHandler() {
    }

    @SubscribeEvent
    public static void onScreenOpening(ScreenEvent.Opening event) {

        if (event.getNewScreen() instanceof TitleScreen
                && !replacing) {

            replacing = true;

            event.setNewScreen(
                    new CustomMainMenuScreen()
            );

        } else {
            replacing = false;
        }
    }
}