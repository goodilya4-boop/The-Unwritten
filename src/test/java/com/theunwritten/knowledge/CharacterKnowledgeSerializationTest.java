package com.theunwritten.knowledge;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

class CharacterKnowledgeSerializationTest {

    private static final ResourceLocation FIRE =
            ResourceLocation.fromNamespaceAndPath("the_unwritten", "magic/fire");

    private static final ResourceLocation LIGHT =
            ResourceLocation.fromNamespaceAndPath("the_unwritten", "magic/light");

    @Test
    void serializeAndDeserializeRoundTripPreservesKnowledgeStates() {
        CharacterKnowledge original = new CharacterKnowledge();
        original.discover(FIRE);
        original.study(FIRE);
        original.discover(LIGHT);

        CompoundTag tag = original.serializeNBT(null);

        CharacterKnowledge restored = new CharacterKnowledge();
        restored.deserializeNBT(null, tag);

        assertEquals(KnowledgeState.STUDIED, restored.state(FIRE));
        assertEquals(KnowledgeState.DISCOVERED, restored.state(LIGHT));
        assertEquals(2, restored.discoveredCount());
    }

    @Test
    void deserializeClearsExistingKnowledgeBeforeLoading() {
        CharacterKnowledge knowledge = new CharacterKnowledge();
        knowledge.discover(FIRE);

        CompoundTag empty = new CompoundTag();
        knowledge.deserializeNBT(null, empty);

        assertFalse(knowledge.knows(FIRE));
        assertEquals(0, knowledge.discoveredCount());
    }

    @Test
    void deserializeMissingKnowledgeListLeavesKnowledgeEmpty() {
        CharacterKnowledge knowledge = new CharacterKnowledge();

        CompoundTag tag = new CompoundTag();
        knowledge.deserializeNBT(null, tag);

        assertEquals(0, knowledge.discoveredCount());
        assertFalse(knowledge.knows(FIRE));
    }
}
