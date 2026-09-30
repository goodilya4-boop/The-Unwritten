package com.theunwritten.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.theunwritten.TheUnwritten;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

@EventBusSubscriber(
        modid = TheUnwritten.MODID,
        value = Dist.CLIENT,
        bus = EventBusSubscriber.Bus.MOD
)
public final class MovementAnimationLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final PlayerModel<AbstractClientPlayer> model;

    private MovementAnimationLayer(PlayerRenderer renderer) {
        super(renderer);
        this.model = renderer.getModel();
    }

    @SubscribeEvent
    public static void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
        for (var skin : event.getSkins()) {
            if (event.getSkin(skin) instanceof PlayerRenderer renderer) {
                renderer.addLayer(new MovementAnimationLayer(renderer));
            }
        }
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
        public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
            hideMovementParts(event.getRenderer().getModel());
        }
    }

    private static void hideMovementParts(PlayerModel<?> model) {
        model.rightArm.visible = false;
        model.leftArm.visible = false;
        model.rightLeg.visible = false;
        model.leftLeg.visible = false;
        model.rightSleeve.visible = false;
        model.leftSleeve.visible = false;
        model.rightPants.visible = false;
        model.leftPants.visible = false;
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        MovementAnimationState state = resolveState(player);
        if (state == MovementAnimationState.IDLE) {
            restoreMovementParts();
            return;
        }

        model.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        applyPose(state, limbSwing + partialTick, limbSwingAmount);

        VertexConsumer vertexConsumer =
                buffer.getBuffer(RenderType.entityCutoutNoCull(player.getSkinTextureLocation()));

        poseStack.pushPose();

        model.rightArm.visible = true;
        model.leftArm.visible = true;
        model.rightLeg.visible = true;
        model.leftLeg.visible = true;

        model.rightArm.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        model.leftArm.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        model.rightLeg.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        model.leftLeg.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
        restoreMovementParts();
    }

    private void restoreMovementParts() {
        model.rightArm.visible = false;
        model.leftArm.visible = false;
        model.rightLeg.visible = false;
        model.leftLeg.visible = false;
        model.rightSleeve.visible = false;
        model.leftSleeve.visible = false;
        model.rightPants.visible = false;
        model.leftPants.visible = false;
    }

    private static MovementAnimationState resolveState(AbstractClientPlayer player) {
        boolean crouching = player.isCrouching();
        boolean moving = player.getDeltaMovement().horizontalDistanceSqr() > 0.0001D;
        boolean running = moving && player.isSprinting();

        if (crouching) {
            return moving ? MovementAnimationState.CROUCH_WALK : MovementAnimationState.CROUCH;
        }

        if (running) {
            return MovementAnimationState.RUN;
        }

        return moving ? MovementAnimationState.WALK : MovementAnimationState.IDLE;
    }

    private void applyPose(
            MovementAnimationState state,
            float phase,
            float limbSwingAmount
    ) {
        float amount = Mth.clamp(limbSwingAmount, 0.0F, 1.0F);

        if (state == MovementAnimationState.CROUCH) {
            model.rightLeg.xRot = -0.35F;
            model.leftLeg.xRot = -0.35F;
            model.rightArm.xRot = 0.25F;
            model.leftArm.xRot = 0.25F;
            return;
        }

        float speed = state == MovementAnimationState.RUN ? 1.35F : 1.0F;
        float legAmplitude = switch (state) {
            case WALK -> 0.65F;
            case RUN -> 1.0F;
            case CROUCH_WALK -> 0.45F;
            default -> 0.0F;
        };
        float armAmplitude = switch (state) {
            case WALK -> 0.35F;
            case RUN -> 0.65F;
            case CROUCH_WALK -> 0.20F;
            default -> 0.0F;
        };

        float cycle = Mth.cos(phase * speed);
        float opposite = Mth.cos(phase * speed + Mth.PI);

        model.rightLeg.xRot += cycle * legAmplitude * amount;
        model.leftLeg.xRot += opposite * legAmplitude * amount;
        model.rightArm.xRot += opposite * armAmplitude * amount;
        model.leftArm.xRot += cycle * armAmplitude * amount;

        if (state == MovementAnimationState.CROUCH_WALK) {
            model.rightLeg.xRot -= 0.20F;
            model.leftLeg.xRot -= 0.20F;
            model.rightArm.xRot += 0.15F;
            model.leftArm.xRot += 0.15F;
        }

        if (state == MovementAnimationState.RUN) {
            model.rightArm.xRot += 0.20F;
            model.leftArm.xRot += 0.20F;
        }
    }
}
