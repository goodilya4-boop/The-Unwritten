package com.theunwritten.animation;

import java.util.Objects;

public record AnimationDefinition(
        AnimationId id,
        int durationTicks,
        boolean loop,
        AnimationLayer layer,
        int priority,
        boolean interruptible
) {
    public AnimationDefinition {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(layer, "layer");
        if (durationTicks < 1) {
            throw new IllegalArgumentException("Animation duration must be >= 1 tick");
        }
        if (priority < 0) {
            throw new IllegalArgumentException("Animation priority must be >= 0");
        }
    }

    public AnimationDefinition(AnimationId id, int durationTicks, boolean loop, AnimationLayer layer) {
        this(id, durationTicks, loop, layer, 0, true);
    }
}
