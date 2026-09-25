package com.theunwritten.knowledge;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.resources.ResourceLocation;

/**
 * Knowledge source representing a teacher and the knowledge they can expose.
 *
 * <p>This is a domain model only. It is intentionally independent from
 * Minecraft entities, dialogue, GUI, and world logic.</p>
 */
public record KnowledgeTeacher(
        ResourceLocation id,
        List<KnowledgeDiscoveryOffer> offers) {

    public KnowledgeTeacher {
        if (id == null) {
            throw new IllegalArgumentException("Teacher id cannot be null");
        }
        if (offers == null) {
            throw new IllegalArgumentException("Teacher offers cannot be null");
        }

        List<KnowledgeDiscoveryOffer> copiedOffers = new ArrayList<>(offers.size());
        for (KnowledgeDiscoveryOffer offer : offers) {
            if (offer == null) {
                throw new IllegalArgumentException("Teacher offer cannot be null");
            }
            copiedOffers.add(offer);
        }
        offers = List.copyOf(copiedOffers);

        for (int i = 0; i < offers.size(); i++) {
            KnowledgeDiscoveryOffer offer = offers.get(i);
            for (int j = i + 1; j < offers.size(); j++) {
                if (offer.knowledgeId().equals(offers.get(j).knowledgeId())) {
                    throw new IllegalArgumentException(
                            "Teacher cannot offer the same knowledge more than once: "
                                    + offer.knowledgeId());
                }
            }
        }
    }

    public Optional<KnowledgeDiscoveryOffer> offer(ResourceLocation knowledgeId) {
        if (knowledgeId == null) {
            throw new IllegalArgumentException("Knowledge id cannot be null");
        }

        return offers.stream()
                .filter(candidate -> candidate.knowledgeId().equals(knowledgeId))
                .findFirst();
    }
}
