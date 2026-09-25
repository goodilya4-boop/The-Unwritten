package com.theunwritten.knowledge;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.minecraft.resources.ResourceLocation;

class KnowledgeTeacherTest {
    private static final ResourceLocation TEACHER_ID =
            ResourceLocation.fromNamespaceAndPath("the_unwritten", "teacher/test");

    @Test
    void teacherCopiesOffersAndSupportsLookup() {
        KnowledgeDiscoveryOffer elemental = new KnowledgeDiscoveryOffer(
                KnowledgeIds.Magic.ELEMENTAL, List.of());
        KnowledgeDiscoveryOffer slashing = new KnowledgeDiscoveryOffer(
                KnowledgeIds.Combat.SLASHING, List.of());

        KnowledgeTeacher teacher =
                new KnowledgeTeacher(TEACHER_ID, List.of(elemental, slashing));

        assertEquals(2, teacher.offers().size());
        assertEquals(elemental, teacher.offer(KnowledgeIds.Magic.ELEMENTAL).orElseThrow());
        assertEquals(slashing, teacher.offer(KnowledgeIds.Combat.SLASHING).orElseThrow());
        assertTrue(teacher.offer(KnowledgeIds.Magic.VOID).isEmpty());
        assertThrows(UnsupportedOperationException.class,
                () -> teacher.offers().add(elemental));
    }

    @Test
    void duplicateKnowledgeOffersAreRejected() {
        KnowledgeDiscoveryOffer first =
                new KnowledgeDiscoveryOffer(KnowledgeIds.Magic.LIGHT, List.of());
        KnowledgeDiscoveryOffer second =
                new KnowledgeDiscoveryOffer(KnowledgeIds.Magic.LIGHT, List.of());

        assertThrows(IllegalArgumentException.class,
                () -> new KnowledgeTeacher(TEACHER_ID, List.of(first, second)));
    }

    @Test
    void nullValuesAreRejected() {
        KnowledgeDiscoveryOffer offer =
                new KnowledgeDiscoveryOffer(KnowledgeIds.Magic.LIGHT, List.of());

        assertThrows(IllegalArgumentException.class,
                () -> new KnowledgeTeacher(null, List.of(offer)));
        assertThrows(IllegalArgumentException.class,
                () -> new KnowledgeTeacher(TEACHER_ID, null));
        assertThrows(IllegalArgumentException.class,
                () -> new KnowledgeTeacher(TEACHER_ID, Arrays.asList((KnowledgeDiscoveryOffer) null)));
        assertThrows(IllegalArgumentException.class,
                () -> new KnowledgeTeacher(TEACHER_ID, List.of(offer))
                        .offer(null));
    }
}
