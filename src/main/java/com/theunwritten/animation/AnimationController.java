package com.theunwritten.animation;

import java.util.Objects;

public final class AnimationController {
    private AnimationDefinition current;
    private int elapsedTicks;
    private AnimationState state = AnimationState.IDLE;

    public AnimationDefinition current() {
        return current;
    }

    public int elapsedTicks() {
        return elapsedTicks;
    }

    public AnimationState state() {
        return state;
    }

    public boolean play(AnimationDefinition definition) {
        Objects.requireNonNull(definition, "definition");

        if (current != null
                && state == AnimationState.PLAYING
                && !current.interruptible()
                && definition.priority() < current.priority()) {
            return false;
        }

        current = definition;
        elapsedTicks = 0;
        state = AnimationState.PLAYING;
        return true;
    }

    public void tick() {
        if (current == null || state != AnimationState.PLAYING) {
            return;
        }

        elapsedTicks++;

        if (elapsedTicks >= current.durationTicks()) {
            if (current.loop()) {
                elapsedTicks = 0;
            } else {
                state = AnimationState.COMPLETED;
            }
        }
    }

    public void stop() {
        if (current != null) {
            state = AnimationState.INTERRUPTED;
        }
    }

    public void clear() {
        current = null;
        elapsedTicks = 0;
        state = AnimationState.IDLE;
    }

    public boolean isPlaying(AnimationId id) {
        return current != null
                && current.id().equals(id)
                && state == AnimationState.PLAYING;
    }
}
