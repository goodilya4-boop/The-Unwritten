package com.theunwritten.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

@EventBusSubscriber(
        modid = com.theunwritten.TheUnwritten.MODID,
        value = Dist.CLIENT,
        bus = EventBusSubscriber.Bus.MOD
)
public final class MovementAnimationLayer extends RenderLayer<LivingEntity, PlayerModel<LivingEntity>> {
    private final PlayerModel<LivingEntity> model;

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

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            LivingEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        if (!(entity instanceof Player player)) {
            return;
        }

        MovementAnimationState state = resolveState(player);
        if (state == MovementAnimationState.IDLE) {
            return;
        }

        model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        float phase = limbSwing + partialTick * 0.25F;
        applyPose(state, phase, limbSwingAmount);

        ResourceLocation texture = player instanceof net.minecraft.client.player.AbstractClientPlayer clientPlayer
                ? clientPlayer.getSkinTextureLocation()
                : null;

        if (texture == null) {
            return;
        }

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(texture));

        poseStack.pushPose();
        model.rightArm.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        model.leftArm.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        model.rightLeg.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        model.leftLeg.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    private static MovementAnimationState resolveState(Player player) {
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

    private static void applyPose(
            MovementAnimationState state,
            float phase,
            float limbSwingAmount
    ) {
        float amount = Mth.clamp(limbSwingAmount, 0.0F, 1.0F);
        float legSwing;
        float armSwing;

        switch (state) {
            case WALK -> {
                legSwing = 0.65F;
                armSwing = 0.35F;
            }
            case RUN -> {
                legSwing = 1.0F;
                armSwing = 0.65F;
            }
            case CROUCH_WALK -> {
                legSwing = 0.45F;
                armSwing = 0.20F;
            }
            case CROUCH -> {
                legSwing = 0.0F;
                armSwing = 0.0F;
            }
            default -> {
                legSwing = 0.0F;
                armSwing = 0.0F;
            }
        }

        if (state == MovementAnimationState.CROUCH) {
            model.rightLeg.xRot = -0.35F;
            model.leftLeg.xRot = -0.35F;
            model.rightArm.xRot = 0.25F;
            model.leftArm.xRot = 0.25F;
            return;
        }

        float cycle = Mth.cos(phase * (state == MovementAnimationState.RUN ? 1.35F : 1.0F));
        float opposite = Mth.cos(phase * (state == MovementAnimationState.RUN ? 1.35F : 1.0F) + Mth.PI);

        model.rightLeg.xRot += cycle * legSwing * amount;
        model.leftLeg.xRot += opposite * legSwing * amount;
        model.rightArm.xRot += opposite * armSwing * amount;
        model.leftArm.xRot += cycle * armSwing * amount;

        if (state == MovementAnimationState.CROUCH_WALK) {
            model.body.xRot = 0.15F;
            model.rightLeg.xRot -= 0.20F;
            model.leftLeg.xRot -= 0.20F;
        }

        if (state == MovementAnimationState.RUN) {
            model.body.xRot = 0.12F;
            model.rightArm.xRot += 0.20F;
            model.leftArm.xRot += 0.20F;
        }
    }
}
