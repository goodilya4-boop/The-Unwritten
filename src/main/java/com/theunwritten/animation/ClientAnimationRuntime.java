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
    private static final AnimationStateMachine STATE_MACHINE = new AnimationStateMachine();

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
            Minecraft minecraft = Minecraft.getInstance();

            if (minecraft.player != null) {
                updateMovement(minecraft);
            }

            while (TEST_ANIMATION.consumeClick()) {
                playTestAnimation();
            }

            CONTROLLER.tick();

            if (minecraft.player != null
                    && CONTROLLER.state() == AnimationState.COMPLETED
                    && CONTROLLER.current() != null
                    && CONTROLLER.current().layer() == AnimationLayer.ACTION) {
                playCurrentMovement(minecraft);
            }
        }

        private static void updateMovement(Minecraft minecraft) {
            if (!STATE_MACHINE.update(minecraft.player)) {
                return;
            }

            playCurrentMovement(minecraft);
            minecraft.player.displayClientMessage(
                    Component.literal("Movement animation: " + CONTROLLER.current().id()),
                    true
            );
        }

        private static void playCurrentMovement(Minecraft minecraft) {
            AnimationDefinition definition = switch (STATE_MACHINE.movementState()) {
                case IDLE -> AnimationContent.IMPERIAL_IDLE;
                case WALK -> AnimationContent.MOVEMENT_WALK;
                case CROUCH -> AnimationContent.MOVEMENT_CROUCH;
                case CROUCH_WALK -> AnimationContent.MOVEMENT_CROUCH_WALK;
                case RUN -> AnimationContent.MOVEMENT_RUN;
            };

            CONTROLLER.play(definition);
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
