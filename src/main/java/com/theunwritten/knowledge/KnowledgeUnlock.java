package com.theunwritten.knowledge;

import java.util.Objects;

public record KnowledgeUnlock(String id) {
    public KnowledgeUnlock {
        Objects.requireNonNull(id, "id");
        if (id.isBlank()) {
            throw new IllegalArgumentException("Unlock id must not be blank");
        }
    }
}
