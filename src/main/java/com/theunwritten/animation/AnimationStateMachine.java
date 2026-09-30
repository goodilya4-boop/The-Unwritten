package com.theunwritten.animation;

import net.minecraft.client.player.LocalPlayer;

import java.util.Objects;

public final class AnimationStateMachine {
    private MovementAnimationState movementState = MovementAnimationState.IDLE;

    public MovementAnimationState movementState() {
        return movementState;
    }

    public boolean update(LocalPlayer player) {
        Objects.requireNonNull(player, "player");

        MovementAnimationState next = resolveMovementState(player);
        if (next == movementState) {
            return false;
        }

        movementState = next;
        return true;
    }

    private static MovementAnimationState resolveMovementState(LocalPlayer player) {
        boolean crouching = player.isCrouching();
        boolean moving = player.getDeltaMovement().horizontalDistanceSqr() > 0.0001D;

        if (crouching) {
            return moving ? MovementAnimationState.CROUCH_WALK : MovementAnimationState.CROUCH;
        }

        return moving ? MovementAnimationState.WALK : MovementAnimationState.IDLE;
    }
}
