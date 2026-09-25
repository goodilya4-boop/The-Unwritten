package com.theunwritten.knowledge;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class KnowledgeRequirementTest {
    @Test
    void minimumStateAcceptsThatStateAndHigherStates() {
        CharacterKnowledge knowledge = new CharacterKnowledge();
        knowledge.discover(KnowledgeIds.Magic.ELEMENTAL);
        KnowledgeRequirement requirement =
                new KnowledgeRequirement(
                        KnowledgeIds.Magic.ELEMENTAL,
                        KnowledgeState.DISCOVERED);

        assertTrue(requirement.isSatisfiedBy(knowledge));

        knowledge.study(KnowledgeIds.Magic.ELEMENTAL);
        assertTrue(requirement.isSatisfiedBy(knowledge));

        knowledge.master(KnowledgeIds.Magic.ELEMENTAL);
        assertTrue(requirement.isSatisfiedBy(knowledge));
    }

    @Test
    void higherMinimumStateRejectsLowerState() {
        CharacterKnowledge knowledge = new CharacterKnowledge();
        knowledge.discover(KnowledgeIds.Magic.ELEMENTAL);
        KnowledgeRequirement requirement =
                new KnowledgeRequirement(
                        KnowledgeIds.Magic.ELEMENTAL,
                        KnowledgeState.STUDIED);

        assertFalse(requirement.isSatisfiedBy(knowledge));

        knowledge.study(KnowledgeIds.Magic.ELEMENTAL);
        assertTrue(requirement.isSatisfiedBy(knowledge));
    }

    @Test
    void unknownKnowledgeDoesNotSatisfyRequirement() {
        KnowledgeRequirement requirement =
                new KnowledgeRequirement(
                        KnowledgeIds.Magic.ELEMENTAL,
                        KnowledgeState.DISCOVERED);

        assertFalse(requirement.isSatisfiedBy(new CharacterKnowledge()));
    }

    @Test
    void nullValuesAreRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new KnowledgeRequirement(
                        null, KnowledgeState.DISCOVERED));
        assertThrows(IllegalArgumentException.class,
                () -> new KnowledgeRequirement(
                        KnowledgeIds.Magic.ELEMENTAL, null));
    }
}
