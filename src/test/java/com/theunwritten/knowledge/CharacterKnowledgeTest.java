package com.theunwritten.knowledge;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

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
    }

    @Test
    void discoverReturnsTrueOnlyForNewKnowledge() {
        CharacterKnowledge knowledge = new CharacterKnowledge();

        assertTrue(knowledge.discover(FIRE));
        assertFalse(knowledge.discover(FIRE));

        assertTrue(knowledge.knows(FIRE));
        assertEquals(1, knowledge.discoveredCount());
    }

    @Test
    void discoveredSetIsReadOnly() {
        CharacterKnowledge knowledge = new CharacterKnowledge();
        knowledge.discover(FIRE);

        Set<ResourceLocation> discovered = knowledge.discovered();

        assertThrows(UnsupportedOperationException.class, () -> discovered.add(LIGHT));
        assertEquals(1, knowledge.discoveredCount());
    }

    @Test
    void copyCreatesIndependentKnowledgeState() {
        CharacterKnowledge original = new CharacterKnowledge();
        original.discover(FIRE);

        CharacterKnowledge copy = original.copy();
        copy.discover(LIGHT);

        assertTrue(original.knows(FIRE));
        assertFalse(original.knows(LIGHT));
        assertTrue(copy.knows(FIRE));
        assertTrue(copy.knows(LIGHT));
    }
}
