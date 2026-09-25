package com.theunwritten.knowledge;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.minecraft.resources.ResourceLocation;

class KnowledgeDiscoveryServiceTest {
    private static final ResourceLocation ELEMENTAL = KnowledgeIds.Magic.ELEMENTAL;
    private static final ResourceLocation UNKNOWN =
            ResourceLocation.fromNamespaceAndPath("the_unwritten", "magic/unknown");

    @Test
    void discoverThroughContextRegistersKnownKnowledge() {
        KnowledgeDiscoveryService service =
                new KnowledgeDiscoveryService(KnowledgeRegistry.createDefault());
        CharacterKnowledge characterKnowledge = new CharacterKnowledge();

        KnowledgeDiscoveryResult result = service.discover(new KnowledgeDiscoveryContext(
                characterKnowledge,
                ELEMENTAL,
                KnowledgeDiscoverySource.TEACHER));

        assertEquals(KnowledgeDiscoveryResult.SUCCESS, result);
        assertEquals(KnowledgeState.DISCOVERED, characterKnowledge.state(ELEMENTAL));
    }

    @Test
    void discoveringSameKnowledgeTwiceReturnsAlreadyKnown() {
        KnowledgeDiscoveryService service =
                new KnowledgeDiscoveryService(KnowledgeRegistry.createDefault());
        CharacterKnowledge characterKnowledge = new CharacterKnowledge();
        KnowledgeDiscoveryContext context = new KnowledgeDiscoveryContext(
                characterKnowledge,
                ELEMENTAL,
                KnowledgeDiscoverySource.TEACHER);

        assertEquals(KnowledgeDiscoveryResult.SUCCESS, service.discover(context));
        assertEquals(KnowledgeDiscoveryResult.ALREADY_KNOWN, service.discover(context));
        assertEquals(KnowledgeState.DISCOVERED, characterKnowledge.state(ELEMENTAL));
    }

    @Test
    void unknownKnowledgeCannotBeDiscovered() {
        KnowledgeDiscoveryService service =
                new KnowledgeDiscoveryService(KnowledgeRegistry.createDefault());
        CharacterKnowledge characterKnowledge = new CharacterKnowledge();

        KnowledgeDiscoveryResult result = service.discover(new KnowledgeDiscoveryContext(
                characterKnowledge,
                UNKNOWN,
                KnowledgeDiscoverySource.BOOK));

        assertEquals(KnowledgeDiscoveryResult.UNKNOWN_KNOWLEDGE, result);
        assertFalse(characterKnowledge.knows(UNKNOWN));
    }

    @Test
    void hiddenKnowledgeCanBeDiscoveredThroughExplicitSource() {
        KnowledgeDiscoveryService service =
                new KnowledgeDiscoveryService(KnowledgeRegistry.createDefault());
        CharacterKnowledge characterKnowledge = new CharacterKnowledge();

        KnowledgeDiscoveryResult result = service.discover(new KnowledgeDiscoveryContext(
                characterKnowledge,
                KnowledgeIds.Magic.VOID,
                KnowledgeDiscoverySource.PHENOMENON));

        assertEquals(KnowledgeDiscoveryResult.SUCCESS, result);
        assertEquals(KnowledgeState.DISCOVERED,
                characterKnowledge.state(KnowledgeIds.Magic.VOID));
    }

    @Test
    void lowLevelDiscoveryRemainsAvailable() {
        KnowledgeDiscoveryService service =
                new KnowledgeDiscoveryService(KnowledgeRegistry.createDefault());
        CharacterKnowledge characterKnowledge = new CharacterKnowledge();

        assertTrue(service.discover(characterKnowledge, ELEMENTAL));
        assertFalse(service.discover(characterKnowledge, ELEMENTAL));
    }

    @Test
    void nullContextIsRejected() {
        KnowledgeDiscoveryService service =
                new KnowledgeDiscoveryService(KnowledgeRegistry.createDefault());

        assertThrows(IllegalArgumentException.class, () -> service.discover(null));
    }

    @Test
    void nullContextValuesAreRejected() {
        CharacterKnowledge knowledge = new CharacterKnowledge();

        assertThrows(IllegalArgumentException.class,
                () -> new KnowledgeDiscoveryContext(null, ELEMENTAL,
                        KnowledgeDiscoverySource.TEACHER));
        assertThrows(IllegalArgumentException.class,
                () -> new KnowledgeDiscoveryContext(knowledge, null,
                        KnowledgeDiscoverySource.TEACHER));
        assertThrows(IllegalArgumentException.class,
                () -> new KnowledgeDiscoveryContext(knowledge, ELEMENTAL, null));
    }

    @Test
    void nullRegistryIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new KnowledgeDiscoveryService(null));
    }
}
