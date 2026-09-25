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
    void discoveryFailsWhenOfferRequirementsAreNotMet() {
        KnowledgeDiscoveryService service =
                new KnowledgeDiscoveryService(KnowledgeRegistry.createDefault());
        CharacterKnowledge characterKnowledge = new CharacterKnowledge();
        KnowledgeDiscoveryOffer offer = new KnowledgeDiscoveryOffer(
                KnowledgeIds.Magic.LIGHT,
                java.util.List.of(new KnowledgeRequirement(
                        KnowledgeIds.Magic.ELEMENTAL,
                        KnowledgeState.STUDIED)));

        KnowledgeDiscoveryResult result = service.discover(
                new KnowledgeDiscoveryContext(
                        characterKnowledge,
                        KnowledgeIds.Magic.LIGHT,
                        KnowledgeDiscoverySource.TEACHER),
                offer);

        assertEquals(KnowledgeDiscoveryResult.REQUIREMENTS_NOT_MET, result);
        assertFalse(characterKnowledge.knows(KnowledgeIds.Magic.LIGHT));
    }

    @Test
    void discoverySucceedsWhenAllOfferRequirementsAreMet() {
        KnowledgeDiscoveryService service =
                new KnowledgeDiscoveryService(KnowledgeRegistry.createDefault());
        CharacterKnowledge characterKnowledge = new CharacterKnowledge();
        characterKnowledge.discover(KnowledgeIds.Magic.ELEMENTAL);
        characterKnowledge.study(KnowledgeIds.Magic.ELEMENTAL);

        KnowledgeDiscoveryOffer offer = new KnowledgeDiscoveryOffer(
                KnowledgeIds.Magic.LIGHT,
                java.util.List.of(new KnowledgeRequirement(
                        KnowledgeIds.Magic.ELEMENTAL,
                        KnowledgeState.STUDIED)));

        KnowledgeDiscoveryResult result = service.discover(
                new KnowledgeDiscoveryContext(
                        characterKnowledge,
                        KnowledgeIds.Magic.LIGHT,
                        KnowledgeDiscoverySource.TEACHER),
                offer);

        assertEquals(KnowledgeDiscoveryResult.SUCCESS, result);
        assertEquals(KnowledgeState.DISCOVERED,
                characterKnowledge.state(KnowledgeIds.Magic.LIGHT));
    }

    @Test
    void mismatchedContextAndOfferAreRejected() {
        KnowledgeDiscoveryService service =
                new KnowledgeDiscoveryService(KnowledgeRegistry.createDefault());
        CharacterKnowledge characterKnowledge = new CharacterKnowledge();
        KnowledgeDiscoveryOffer offer = new KnowledgeDiscoveryOffer(
                KnowledgeIds.Magic.LIGHT,
                java.util.List.of());

        assertThrows(IllegalArgumentException.class, () -> service.discover(
                new KnowledgeDiscoveryContext(
                        characterKnowledge,
                        KnowledgeIds.Magic.ELEMENTAL,
                        KnowledgeDiscoverySource.TEACHER),
                offer));
    }

    @Test
    void nullOfferIsRejected() {
        KnowledgeDiscoveryService service =
                new KnowledgeDiscoveryService(KnowledgeRegistry.createDefault());
        CharacterKnowledge characterKnowledge = new CharacterKnowledge();
        KnowledgeDiscoveryContext context = new KnowledgeDiscoveryContext(
                characterKnowledge,
                KnowledgeIds.Magic.LIGHT,
                KnowledgeDiscoverySource.TEACHER);

        assertThrows(IllegalArgumentException.class,
                () -> service.discover(context, null));
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
