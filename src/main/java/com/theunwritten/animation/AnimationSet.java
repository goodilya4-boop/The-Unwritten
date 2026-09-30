package com.theunwritten.animation;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class AnimationSet {
    private final String id;
    private final Map<String, AnimationId> animations = new LinkedHashMap<>();

    public AnimationSet(String id) {
        Objects.requireNonNull(id, "id");
        if (id.isBlank()) {
            throw new IllegalArgumentException("Animation set id must not be blank");
        }
        this.id = id;
    }

    public String id() {
        return id;
    }

    public void put(String action, AnimationId animation) {
        Objects.requireNonNull(action, "action");
        Objects.requireNonNull(animation, "animation");
        if (action.isBlank()) {
            throw new IllegalArgumentException("Animation action must not be blank");
        }
        animations.put(action, animation);
    }

    public AnimationId get(String action) {
        return animations.get(action);
    }

    public Map<String, AnimationId> animations() {
        return Collections.unmodifiableMap(animations);
    }
}
