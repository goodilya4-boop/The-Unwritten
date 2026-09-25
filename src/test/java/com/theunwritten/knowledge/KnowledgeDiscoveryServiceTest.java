package com.theunwritten.knowledge;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.minecraft.resources.ResourceLocation;

class KnowledgeDiscoveryServiceTest {
    private static final ResourceLocation ELEMENTAL = KnowledgeIds.Magic.ELEMENTAL;
    private static final ResourceLocation UNKNOWN =
            ResourceLocation.fromNamespaceAndPath("the_unwritten", "magic/unknown");

    @Test
    void discoverRegistersKnownKnowledge() {
        KnowledgeDiscoveryService service =
                new KnowledgeDiscoveryService(KnowledgeRegistry.createDefault());
        CharacterKnowledge characterKnowledge = new CharacterKnowledge();

        assertTrue(service.discover(characterKnowledge, ELEMENTAL));
        assertEquals(KnowledgeState.DISCOVERED, characterKnowledge.state(ELEMENTAL));
    }

    @Test
    void discoveringSameKnowledgeTwiceDoesNotChangeState() {
        KnowledgeDiscoveryService service =
                new KnowledgeDiscoveryService(KnowledgeRegistry.createDefault());
        CharacterKnowledge characterKnowledge = new CharacterKnowledge();

        assertTrue(service.discover(characterKnowledge, ELEMENTAL));
        assertFalse(service.discover(characterKnowledge, ELEMENTAL));
        assertEquals(KnowledgeState.DISCOVERED, characterKnowledge.state(ELEMENTAL));
    }

    @Test
    void unknownKnowledgeCannotBeDiscovered() {
        KnowledgeDiscoveryService service =
                new KnowledgeDiscoveryService(KnowledgeRegistry.createDefault());
        CharacterKnowledge characterKnowledge = new CharacterKnowledge();

        assertFalse(service.discover(characterKnowledge, UNKNOWN));
        assertFalse(characterKnowledge.knows(UNKNOWN));
    }

    @Test
    void hiddenKnowledgeCanBeDiscoveredWhenExplicitlyGranted() {
        KnowledgeDiscoveryService service =
                new KnowledgeDiscoveryService(KnowledgeRegistry.createDefault());
        CharacterKnowledge characterKnowledge = new CharacterKnowledge();

        assertTrue(service.discover(characterKnowledge, KnowledgeIds.Magic.VOID));
        assertEquals(KnowledgeState.DISCOVERED,
                characterKnowledge.state(KnowledgeIds.Magic.VOID));
    }

    @Test
    void nullArgumentsAreRejected() {
        KnowledgeDiscoveryService service =
                new KnowledgeDiscoveryService(KnowledgeRegistry.createDefault());

        assertThrows(IllegalArgumentException.class,
                () -> service.discover(null, ELEMENTAL));

        assertThrows(IllegalArgumentException.class,
                () -> service.discover(new CharacterKnowledge(), null));
    }

    @Test
    void nullRegistryIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new KnowledgeDiscoveryService(null));
    }
}
