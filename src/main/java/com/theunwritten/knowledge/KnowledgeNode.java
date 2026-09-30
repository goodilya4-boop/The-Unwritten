package com.theunwritten.knowledge;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public record KnowledgeNode(
        String id,
        String name,
        String parentId,
        int maxLevel,
        Set<String> prerequisites,
        List<KnowledgeRequirement> requirements,
        List<KnowledgeUnlock> unlocks
) {
    public KnowledgeNode {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(prerequisites, "prerequisites");
        Objects.requireNonNull(requirements, "requirements");
        Objects.requireNonNull(unlocks, "unlocks");

        if (id.isBlank() || name.isBlank()) {
            throw new IllegalArgumentException("Knowledge node id and name must not be blank");
        }
        if (maxLevel < 1) {
            throw new IllegalArgumentException("Knowledge node maxLevel must be >= 1");
        }
        if (parentId != null && parentId.isBlank()) {
            throw new IllegalArgumentException("Knowledge node parentId must not be blank");
        }
        if (prerequisites.contains(id)) {
            throw new IllegalArgumentException("Knowledge node cannot require itself: " + id);
        }

        prerequisites = Set.copyOf(prerequisites);
        requirements = List.copyOf(requirements);
        unlocks = List.copyOf(unlocks);
    }

    public KnowledgeNode(String id, String name, int maxLevel) {
        this(id, name, null, maxLevel, Set.of(), List.of(), List.of());
    }

    public boolean isMaxed(int level) {
        return level >= maxLevel;
    }
}
