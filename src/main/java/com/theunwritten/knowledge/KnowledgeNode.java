package com.theunwritten.knowledge;

import java.util.Objects;

public record KnowledgeNode(
        String id,
        String name,
        String parentId,
        int maxLevel
) {
    public KnowledgeNode {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        if (id.isBlank() || name.isBlank()) {
            throw new IllegalArgumentException("Knowledge node id and name must not be blank");
        }
        if (maxLevel < 1) {
            throw new IllegalArgumentException("Knowledge node maxLevel must be >= 1");
        }
        if (parentId != null && parentId.isBlank()) {
            throw new IllegalArgumentException("Knowledge node parentId must not be blank");
        }
    }

    public KnowledgeNode(String id, String name, int maxLevel) {
        this(id, name, null, maxLevel);
    }
}
