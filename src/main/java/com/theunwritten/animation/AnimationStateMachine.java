package com.theunwritten.animation;

import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public final class AnimationStateMachine {
    private MovementAnimationState movementState = MovementAnimationState.IDLE;

    public MovementAnimationState movementState() {
        return movementState;
    }

    public boolean update(Player player) {
        Objects.requireNonNull(player, "player");

        MovementAnimationState next = resolveMovementState(player);
        if (next == movementState) {
            return false;
        }

        movementState = next;
        return true;
    }

    public static MovementAnimationState resolveMovementState(Player player) {
        Objects.requireNonNull(player, "player");

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
}
