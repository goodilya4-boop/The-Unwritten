package com.theunwritten.knowledge;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class KnowledgeDiscoveryOfferTest {
    @Test
    void offerCopiesRequirements() {
        List<KnowledgeRequirement> requirements = List.of(
                new KnowledgeRequirement(
                        KnowledgeIds.Magic.ELEMENTAL,
                        KnowledgeState.STUDIED));

        KnowledgeDiscoveryOffer offer =
                new KnowledgeDiscoveryOffer(KnowledgeIds.Magic.LIGHT, requirements);

        assertEquals(requirements, offer.requirements());
        assertThrows(UnsupportedOperationException.class,
                () -> offer.requirements().add(requirements.get(0)));
    }

    @Test
    void allRequirementsMustBeSatisfied() {
        CharacterKnowledge knowledge = new CharacterKnowledge();
        knowledge.discover(KnowledgeIds.Magic.ELEMENTAL);
        knowledge.study(KnowledgeIds.Magic.ELEMENTAL);

        KnowledgeDiscoveryOffer offer =
                new KnowledgeDiscoveryOffer(
                        KnowledgeIds.Magic.LIGHT,
                        List.of(
                                new KnowledgeRequirement(
                                        KnowledgeIds.Magic.ELEMENTAL,
                                        KnowledgeState.STUDIED),
                                new KnowledgeRequirement(
                                        KnowledgeIds.Combat.SLASHING,
                                        KnowledgeState.DISCOVERED)));

        assertFalse(offer.requirementsSatisfiedBy(knowledge));

        knowledge.discover(KnowledgeIds.Combat.SLASHING);
        assertTrue(offer.requirementsSatisfiedBy(knowledge));
    }

    @Test
    void nullValuesAreRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new KnowledgeDiscoveryOffer(null, List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> new KnowledgeDiscoveryOffer(KnowledgeIds.Magic.LIGHT, null));
    }
}
