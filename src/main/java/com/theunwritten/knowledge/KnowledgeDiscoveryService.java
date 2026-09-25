package com.theunwritten.knowledge;

import net.minecraft.resources.ResourceLocation;

/**
 * Handles discovery of knowledge that exists in the world.
 *
 * <p>The registry defines what knowledge exists. Discovery offers define
 * what a source exposes and which requirements must be satisfied.</p>
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
     * <p>This overload represents a source with no additional requirements.</p>
     */
    public KnowledgeDiscoveryResult discover(KnowledgeDiscoveryContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Discovery context cannot be null");
        }

        return discover(
                context,
                new KnowledgeDiscoveryOffer(context.knowledgeId(), java.util.List.of()));
    }

    /**
     * Attempts to discover knowledge exposed by a specific source offer.
     */
    public KnowledgeDiscoveryResult discover(
            KnowledgeDiscoveryContext context,
            KnowledgeDiscoveryOffer offer) {

        if (context == null) {
            throw new IllegalArgumentException("Discovery context cannot be null");
        }
        if (offer == null) {
            throw new IllegalArgumentException("Discovery offer cannot be null");
        }
        if (!context.knowledgeId().equals(offer.knowledgeId())) {
            throw new IllegalArgumentException(
                    "Discovery context and offer knowledge ids must match");
        }

        CharacterKnowledge characterKnowledge = context.characterKnowledge();
        ResourceLocation knowledgeId = context.knowledgeId();

        if (!registry.contains(knowledgeId)) {
            return KnowledgeDiscoveryResult.UNKNOWN_KNOWLEDGE;
        }

        if (!offer.requirementsSatisfiedBy(characterKnowledge)) {
            return KnowledgeDiscoveryResult.REQUIREMENTS_NOT_MET;
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
