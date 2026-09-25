package com.theunwritten.knowledge;

import net.minecraft.resources.ResourceLocation;

/**
 * A requirement that a character must satisfy before knowledge can be discovered.
 */
public record KnowledgeRequirement(
        ResourceLocation knowledgeId,
        KnowledgeState minimumState) {

    public KnowledgeRequirement {
        if (knowledgeId == null) {
            throw new IllegalArgumentException("Required knowledge id cannot be null");
        }
        if (minimumState == null) {
            throw new IllegalArgumentException("Minimum knowledge state cannot be null");
        }
    }

    public boolean isSatisfiedBy(CharacterKnowledge characterKnowledge) {
        if (characterKnowledge == null) {
            throw new IllegalArgumentException("Character knowledge cannot be null");
        }

        KnowledgeState current = characterKnowledge.state(knowledgeId);
        return current != null && rank(current) >= rank(minimumState);
    }

    private static int rank(KnowledgeState state) {
        return switch (state) {
            case DISCOVERED -> 1;
            case STUDIED -> 2;
            case MASTERED -> 3;
        };
    }
}
