package com.theunwritten.knowledge;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

import net.minecraft.resources.ResourceLocation;

class CharacterKnowledgeTest {

    private static final ResourceLocation FIRE =
            ResourceLocation.fromNamespaceAndPath("the_unwritten", "magic/fire");

    private static final ResourceLocation LIGHT =
            ResourceLocation.fromNamespaceAndPath("the_unwritten", "magic/light");

    @Test
    void knowledgeIsUnknownInitially() {
        CharacterKnowledge knowledge = new CharacterKnowledge();

        assertFalse(knowledge.knows(FIRE));
        assertEquals(0, knowledge.discoveredCount());
        assertNull(knowledge.state(FIRE));
    }

    @Test
    void discoverReturnsTrueOnlyForNewKnowledge() {
        CharacterKnowledge knowledge = new CharacterKnowledge();

        assertTrue(knowledge.discover(FIRE));
        assertFalse(knowledge.discover(FIRE));

        assertTrue(knowledge.knows(FIRE));
        assertEquals(KnowledgeState.DISCOVERED, knowledge.state(FIRE));
        assertEquals(1, knowledge.discoveredCount());
    }

    @Test
    void knowledgeProgressesInOrder() {
        CharacterKnowledge knowledge = new CharacterKnowledge();

        assertFalse(knowledge.study(FIRE));
        assertFalse(knowledge.master(FIRE));

        assertTrue(knowledge.discover(FIRE));
        assertTrue(knowledge.study(FIRE));
        assertTrue(knowledge.master(FIRE));

        assertTrue(knowledge.isMastered(FIRE));
        assertEquals(KnowledgeState.MASTERED, knowledge.state(FIRE));
    }

    @Test
    void knowledgeCannotSkipStates() {
        CharacterKnowledge knowledge = new CharacterKnowledge();

        knowledge.discover(FIRE);

        assertFalse(knowledge.master(FIRE));
        assertEquals(KnowledgeState.DISCOVERED, knowledge.state(FIRE));

        knowledge.study(FIRE);

        assertFalse(knowledge.discover(FIRE));
        assertEquals(KnowledgeState.STUDIED, knowledge.state(FIRE));
    }

    @Test
    void knownMapIsReadOnly() {
        CharacterKnowledge knowledge = new CharacterKnowledge();
        knowledge.discover(FIRE);

        Map<ResourceLocation, KnowledgeState> known = knowledge.known();

        assertThrows(UnsupportedOperationException.class,
                () -> known.put(LIGHT, KnowledgeState.DISCOVERED));
    }

    @Test
    void copyCreatesIndependentKnowledgeState() {
        CharacterKnowledge original = new CharacterKnowledge();
        original.discover(FIRE);
        original.study(FIRE);

        CharacterKnowledge copy = original.copy();
        copy.master(FIRE);
        copy.discover(LIGHT);

        assertEquals(KnowledgeState.STUDIED, original.state(FIRE));
        assertFalse(original.knows(LIGHT));
        assertEquals(KnowledgeState.MASTERED, copy.state(FIRE));
        assertEquals(KnowledgeState.DISCOVERED, copy.state(LIGHT));
    }
}
