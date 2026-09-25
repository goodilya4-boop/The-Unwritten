package com.theunwritten.knowledge;

import net.minecraft.resources.ResourceLocation;

/**
 * Handles discovery of knowledge that exists in the world.
 *
 * <p>The registry defines what knowledge exists. This service changes only
 * the character's knowledge state and does not automatically expose hidden
 * or rare knowledge.</p>
 */
public final class KnowledgeDiscoveryService {
    private final KnowledgeRegistry registry;

    public KnowledgeDiscoveryService(KnowledgeRegistry registry) {
        if (registry == null) {
            throw new IllegalArgumentException("Knowledge registry cannot be null");
        }
        this.registry = registry;
    }

    /**
     * Attempts to discover knowledge for a character.
     *
     * @return {@code true} when the knowledge was newly discovered
     */
    public boolean discover(CharacterKnowledge characterKnowledge, ResourceLocation knowledgeId) {
        if (characterKnowledge == null) {
            throw new IllegalArgumentException("Character knowledge cannot be null");
        }
        if (knowledgeId == null) {
            throw new IllegalArgumentException("Knowledge id cannot be null");
        }
        if (!registry.contains(knowledgeId)) {
            return false;
        }

        return characterKnowledge.discover(knowledgeId);
    }
}
