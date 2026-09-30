package com.theunwritten.animation;

import com.mojang.blaze3d.platform.InputConstants;
import com.theunwritten.TheUnwritten;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(
        modid = TheUnwritten.MODID,
        value = Dist.CLIENT,
        bus = EventBusSubscriber.Bus.MOD
)
public final class ClientAnimationRuntime {
    private static final AnimationController CONTROLLER = new AnimationController();

    private static final KeyMapping TEST_ANIMATION = new KeyMapping(
            "key.theunwritten.test_animation",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "key.categories.theunwritten"
    );

    private ClientAnimationRuntime() {
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TEST_ANIMATION);
    }

    @EventBusSubscriber(
            modid = TheUnwritten.MODID,
            value = Dist.CLIENT,
            bus = EventBusSubscriber.Bus.GAME
    )
    public static final class ClientEvents {
        private ClientEvents() {
        }

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            while (TEST_ANIMATION.consumeClick()) {
                playTestAnimation();
            }

            CONTROLLER.tick();
        }

        private static void playTestAnimation() {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player == null) {
                return;
            }

            boolean started = CONTROLLER.play(AnimationContent.IMPERIAL_LIGHT_ATTACK);
            if (!started) {
                minecraft.player.displayClientMessage(
                        Component.literal("Animation blocked by current animation."),
                        true
                );
                return;
            }

            minecraft.player.displayClientMessage(
                    Component.literal("Animation started: " + CONTROLLER.current().id()),
                    true
            );
        }
    }
}
