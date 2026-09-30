package com.theunwritten.animation;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class AnimationRegistry {
    private final Map<AnimationId, AnimationDefinition> definitions = new LinkedHashMap<>();

    public void register(AnimationDefinition definition) {
        Objects.requireNonNull(definition, "definition");
        if (definitions.putIfAbsent(definition.id(), definition) != null) {
            throw new IllegalArgumentException("Duplicate animation: " + definition.id());
        }
    }

    public AnimationDefinition get(AnimationId id) {
        return definitions.get(id);
    }

    public boolean contains(AnimationId id) {
        return definitions.containsKey(id);
    }

    public Collection<AnimationDefinition> all() {
        return Collections.unmodifiableCollection(definitions.values());
    }
}
