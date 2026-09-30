package com.theunwritten.animation;

import java.util.Objects;

public final class AnimationApi {
    private AnimationApi() {
    }

    public static AnimationId id(String value) {
        return new AnimationId(value);
    }

    public static AnimationDefinition definition(
            String id,
            int durationTicks,
            boolean loop,
            AnimationLayer layer
    ) {
        return new AnimationDefinition(
                new AnimationId(id),
                durationTicks,
                loop,
                layer
        );
    }

    public static AnimationDefinition definition(
            String id,
            int durationTicks,
            boolean loop,
            AnimationLayer layer,
            int priority,
            boolean interruptible
    ) {
        return new AnimationDefinition(
                new AnimationId(id),
                durationTicks,
                loop,
                layer,
                priority,
                interruptible
        );
    }

    public static AnimationSet set(String id) {
        return new AnimationSet(id);
    }

    public static void require(AnimationRegistry registry, AnimationId id) {
        Objects.requireNonNull(registry, "registry");
        Objects.requireNonNull(id, "id");
        if (!registry.contains(id)) {
            throw new IllegalArgumentException("Unknown animation: " + id);
        }
    }
}
