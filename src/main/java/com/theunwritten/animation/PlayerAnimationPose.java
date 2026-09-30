package com.theunwritten.animation;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public final class PlayerAnimationPose {
    private PlayerAnimationPose() {
    }

    public static void apply(
            PlayerModel<? extends Player> model,
            Player player,
            MovementAnimationState state,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks
    ) {
        float movement = Mth.clamp(limbSwingAmount, 0.0F, 1.0F);

        switch (state) {
            case IDLE -> applyIdle(model, ageInTicks);
            case WALK -> applyWalk(model, limbSwing, movement, 0.22F, 0.10F);
            case CROUCH -> applyCrouch(model, ageInTicks, false);
            case CROUCH_WALK -> {
                applyCrouch(model, ageInTicks, true);
                applyWalk(model, limbSwing, movement, 0.14F, 0.06F);
            }
            case RUN -> {
                applyRun(model, limbSwing, movement);
            }
        }
    }

    private static void applyIdle(PlayerModel<? extends Player> model, float ageInTicks) {
        float breathing = Mth.sin(ageInTicks * 0.08F) * 0.025F;
        model.body.xRot += breathing;
        model.rightArm.xRot -= breathing * 0.75F;
        model.leftArm.xRot -= breathing * 0.75F;
    }

    private static void applyWalk(
            PlayerModel<? extends Player> model,
            float limbSwing,
            float movement,
            float armAmplitude,
            float legAmplitude
    ) {
        float phase = limbSwing * 0.72F;
        float gait = Mth.sin(phase);
        float counterGait = Mth.sin(phase + Mth.PI);

        model.rightArm.xRot += gait * armAmplitude * movement;
        model.leftArm.xRot += counterGait * armAmplitude * movement;
        model.rightLeg.xRot += counterGait * legAmplitude * movement;
        model.leftLeg.xRot += gait * legAmplitude * movement;

        float sway = Mth.sin(phase * 0.5F) * 0.035F * movement;
        model.body.yRot += sway;
        model.body.zRot += sway * 0.35F;
    }

    private static void applyCrouch(
            PlayerModel<? extends Player> model,
            float ageInTicks,
            boolean moving
    ) {
        float breathing = Mth.sin(ageInTicks * 0.08F) * 0.018F;

        model.body.xRot += 0.10F + breathing;
        model.rightArm.xRot += 0.08F;
        model.leftArm.xRot += 0.08F;

        if (!moving) {
            model.rightLeg.xRot -= 0.05F;
            model.leftLeg.xRot -= 0.05F;
        }
    }

    private static void applyRun(
            PlayerModel<? extends Player> model,
            float limbSwing,
            float movement
    ) {
        float phase = limbSwing * 0.92F;
        float gait = Mth.sin(phase);
        float counterGait = Mth.sin(phase + Mth.PI);

        model.rightArm.xRot += gait * 0.40F * movement;
        model.leftArm.xRot += counterGait * 0.40F * movement;
        model.rightLeg.xRot += counterGait * 0.24F * movement;
        model.leftLeg.xRot += gait * 0.24F * movement;

        model.body.xRot += 0.08F * movement;
        model.body.yRot += Mth.sin(phase * 0.5F) * 0.055F * movement;
    }
}
