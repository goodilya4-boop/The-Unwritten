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
     * Attempts to discover knowledge through an explicit world source.
     *
     * <p>The source is recorded in the context for future source-specific
     * rules. At this layer, every explicit source is sufficient to attempt
     * discovery, including sources that expose rare or hidden knowledge.</p>
     */
    public KnowledgeDiscoveryResult discover(KnowledgeDiscoveryContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Discovery context cannot be null");
        }

        CharacterKnowledge characterKnowledge = context.characterKnowledge();
        ResourceLocation knowledgeId = context.knowledgeId();

        if (!registry.contains(knowledgeId)) {
            return KnowledgeDiscoveryResult.UNKNOWN_KNOWLEDGE;
        }

        if (!characterKnowledge.discover(knowledgeId)) {
            return KnowledgeDiscoveryResult.ALREADY_KNOWN;
        }

        return KnowledgeDiscoveryResult.SUCCESS;
    }

    /**
     * Low-level discovery operation retained for callers that already have
     * a resolved knowledge source.
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

        KnowledgeDiscoveryResult result = discover(new KnowledgeDiscoveryContext(
                characterKnowledge,
                knowledgeId,
                KnowledgeDiscoverySource.EVENT));

        return result == KnowledgeDiscoveryResult.SUCCESS;
    }
}
