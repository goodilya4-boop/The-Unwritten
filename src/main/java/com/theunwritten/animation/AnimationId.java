package com.theunwritten.animation;

import java.util.Objects;

public record AnimationId(String value) {
    public AnimationId {
        Objects.requireNonNull(value, "value");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Animation id must not be blank");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
