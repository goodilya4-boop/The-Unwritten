package com.theunwritten.knowledge;

import java.util.List;

import net.minecraft.resources.ResourceLocation;

/**
 * Describes knowledge exposed by a discovery source and the requirements
 * needed to discover it.
 */
public record KnowledgeDiscoveryOffer(
        ResourceLocation knowledgeId,
        List<KnowledgeRequirement> requirements) {

    public KnowledgeDiscoveryOffer {
        if (knowledgeId == null) {
            throw new IllegalArgumentException("Offered knowledge id cannot be null");
        }
        if (requirements == null) {
            throw new IllegalArgumentException("Discovery requirements cannot be null");
        }

        requirements = List.copyOf(requirements);
    }

    public boolean requirementsSatisfiedBy(CharacterKnowledge characterKnowledge) {
        if (characterKnowledge == null) {
            throw new IllegalArgumentException("Character knowledge cannot be null");
        }

        return requirements.stream()
                .allMatch(requirement -> requirement.isSatisfiedBy(characterKnowledge));
    }
}
