package com.theunwritten.knowledge;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;\nimport java.util.Optional;

import org.junit.jupiter.api.Test;

import net.minecraft.resources.ResourceLocation;

class KnowledgeRegistryTest {

    @Test
    void registerAndGetReturnsDefinition() {
        KnowledgeRegistry registry = new KnowledgeRegistry();
        KnowledgeDefinition definition = new KnowledgeDefinition(
                ResourceLocation.fromNamespaceAndPath("the_unwritten", "test/knowledge"),
                KnowledgeDomain.MAGIC,
                KnowledgeTier.SCHOOL,
                null,
                false,
                false,
                false);

        assertSame(definition, registry.register(definition));
        assertTrue(registry.contains(definition.id()));
        assertEquals(Optional.of(definition), registry.get(definition.id()));
        assertEquals(1, registry.size());
    }

    @Test
    void duplicateRegistrationIsRejected() {
        KnowledgeRegistry registry = new KnowledgeRegistry();
        ResourceLocation id =
                ResourceLocation.fromNamespaceAndPath("the_unwritten", "test/duplicate");

        KnowledgeDefinition definition = new KnowledgeDefinition(
                id, KnowledgeDomain.MAGIC, KnowledgeTier.SCHOOL,
                null, false, false, false);

        registry.register(definition);

        assertThrows(IllegalArgumentException.class, () -> registry.register(
                new KnowledgeDefinition(
                        id, KnowledgeDomain.COMBAT, KnowledgeTier.SCHOOL,
                        null, false, false, false)));
    }

    @Test
    void parentMustBeRegisteredFirst() {
        KnowledgeRegistry registry = new KnowledgeRegistry();
        ResourceLocation parent =
                ResourceLocation.fromNamespaceAndPath("the_unwritten", "test/parent");

        KnowledgeDefinition child = new KnowledgeDefinition(
                ResourceLocation.fromNamespaceAndPath("the_unwritten", "test/child"),
                KnowledgeDomain.MAGIC,
                KnowledgeTier.SCHOOL,
                parent,
                false,
                false,
                false);

        assertThrows(IllegalArgumentException.class, () -> registry.register(child));
    }

    @Test
    void defaultRegistryContainsKnownFoundationsAndSchools() {
        KnowledgeRegistry registry = KnowledgeRegistry.createDefault();

        assertTrue(registry.contains(KnowledgeIds.Magic.FOUNDATION));
        assertTrue(registry.contains(KnowledgeIds.Combat.FOUNDATION));
        assertTrue(registry.contains(KnowledgeIds.Engineering.FOUNDATION));
        assertTrue(registry.contains(KnowledgeIds.Magic.ELEMENTAL));
        assertTrue(registry.contains(KnowledgeIds.Combat.SLASHING));
        assertTrue(registry.contains(KnowledgeIds.Engineering.MATERIAL));
    }

    @Test
    void defaultRegistryPreservesParentHierarchy() {
        KnowledgeRegistry registry = KnowledgeRegistry.createDefault();

        assertEquals(
                KnowledgeIds.Magic.FOUNDATION,
                registry.get(KnowledgeIds.Magic.ELEMENTAL).orElseThrow().parent());

        assertEquals(
                KnowledgeIds.Combat.FOUNDATION,
                registry.get(KnowledgeIds.Combat.SLASHING).orElseThrow().parent());

        assertEquals(
                KnowledgeIds.Engineering.FOUNDATION,
                registry.get(KnowledgeIds.Engineering.MATERIAL).orElseThrow().parent());
    }

    @Test
    void canQueryDefinitionsByDomainAndTier() {
        KnowledgeRegistry registry = KnowledgeRegistry.createDefault();

        List<KnowledgeDefinition> magic = registry.byDomain(KnowledgeDomain.MAGIC);
        List<KnowledgeDefinition> schools = registry.byTier(KnowledgeTier.SCHOOL);

        assertTrue(magic.stream().anyMatch(
                definition -> definition.id().equals(KnowledgeIds.Magic.ELEMENTAL)));
        assertTrue(schools.stream().anyMatch(
                definition -> definition.id().equals(KnowledgeIds.Combat.SLASHING)));
        assertTrue(schools.stream().anyMatch(
                definition -> definition.id().equals(KnowledgeIds.Engineering.MATERIAL)));
    }

    @Test
    void rareKnowledgeIsHiddenByDefault() {
        KnowledgeRegistry registry = KnowledgeRegistry.createDefault();

        KnowledgeDefinition voidKnowledge =
                registry.get(KnowledgeIds.Magic.VOID).orElseThrow();

        assertTrue(voidKnowledge.rare());
        assertTrue(voidKnowledge.hiddenByDefault());
    }
}
