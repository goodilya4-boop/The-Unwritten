package com.theunwritten.knowledge;

import net.minecraft.resources.ResourceLocation;

/**
 * Describes a single attempt to discover knowledge through a world source.
 */
public record KnowledgeDiscoveryContext(
        CharacterKnowledge characterKnowledge,
        ResourceLocation knowledgeId,
        KnowledgeDiscoverySource source) {

    public KnowledgeDiscoveryContext {
        if (characterKnowledge == null) {
            throw new IllegalArgumentException("Character knowledge cannot be null");
        }
        if (knowledgeId == null) {
            throw new IllegalArgumentException("Knowledge id cannot be null");
        }
        if (source == null) {
            throw new IllegalArgumentException("Discovery source cannot be null");
        }
    }
}
