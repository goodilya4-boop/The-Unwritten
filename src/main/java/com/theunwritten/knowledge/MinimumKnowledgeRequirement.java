package com.theunwritten.knowledge;

import com.theunwritten.character.CharacterData;

import java.util.Objects;

public record MinimumKnowledgeRequirement(String nodeId, int level) implements KnowledgeRequirement {
    public MinimumKnowledgeRequirement {
        Objects.requireNonNull(nodeId, "nodeId");
        if (nodeId.isBlank()) {
            throw new IllegalArgumentException("nodeId must not be blank");
        }
        if (level < 1) {
            throw new IllegalArgumentException("level must be >= 1");
        }
    }

    @Override
    public boolean isMet(CharacterData character) {
        return character.knowledge().level(nodeId) >= level;
    }

    @Override
    public String description() {
        return "Knowledge " + nodeId + " >= " + level;
    }
}
