package com.theunwritten.character.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.theunwritten.character.attribute.AttributeModifier;
import com.theunwritten.character.attribute.AttributeModifierOperation;
import com.theunwritten.character.data.AttributeType;
import com.theunwritten.character.data.Attributes;

import net.minecraft.resources.ResourceLocation;

class CharacterAttributeAccessTest {
    private static final ResourceLocation SOURCE = ResourceLocation.fromNamespaceAndPath("the_unwritten", "test");

    @Test
    void additionModifiersAreAppliedToBase() {
        Attributes attributes = new Attributes(20.0D);
        CharacterAttributeAccess access = new CharacterAttributeAccess(attributes);

        access.addModifier(new AttributeModifier("add", AttributeType.STR, 5.0D,
                AttributeModifierOperation.ADDITION, SOURCE));

        assertEquals(25.0D, access.get(AttributeType.STR), 1.0E-9);
    }

    @Test
    void multiplyBaseUsesOriginalBase() {
        Attributes attributes = new Attributes(20.0D);
        CharacterAttributeAccess access = new CharacterAttributeAccess(attributes);

        access.addModifier(new AttributeModifier("base", AttributeType.STR, 0.25D,
                AttributeModifierOperation.MULTIPLY_BASE, SOURCE));

        assertEquals(25.0D, access.get(AttributeType.STR), 1.0E-9);
    }

    @Test
    void multiplyTotalIsAppliedAfterAdditionsAndBaseMultipliers() {
        Attributes attributes = new Attributes(20.0D);
        CharacterAttributeAccess access = new CharacterAttributeAccess(attributes);

        access.addModifier(new AttributeModifier("add", AttributeType.STR, 5.0D,
                AttributeModifierOperation.ADDITION, SOURCE));
        access.addModifier(new AttributeModifier("base", AttributeType.STR, 0.25D,
                AttributeModifierOperation.MULTIPLY_BASE, SOURCE));
        access.addModifier(new AttributeModifier("total", AttributeType.STR, 0.10D,
                AttributeModifierOperation.MULTIPLY_TOTAL, SOURCE));

        assertEquals(33.0D, access.get(AttributeType.STR), 1.0E-9);
    }

    @Test
    void modifiersFromSourceCanBeRemovedTogether() {
        Attributes attributes = new Attributes(20.0D);
        CharacterAttributeAccess access = new CharacterAttributeAccess(attributes);

        access.addModifier(new AttributeModifier("one", AttributeType.STR, 5.0D,
                AttributeModifierOperation.ADDITION, SOURCE));
        access.addModifier(new AttributeModifier("two", AttributeType.DEX, 7.0D,
                AttributeModifierOperation.ADDITION, SOURCE));

        assertEquals(2, access.removeModifiersFromSource(SOURCE));
        assertEquals(20.0D, access.get(AttributeType.STR), 1.0E-9);
        assertEquals(20.0D, access.get(AttributeType.DEX), 1.0E-9);
    }

    @Test
    void addingSameModifierIdentityReplacesPreviousValue() {
        Attributes attributes = new Attributes(20.0D);
        CharacterAttributeAccess access = new CharacterAttributeAccess(attributes);

        access.addModifier(new AttributeModifier("slot_bonus", AttributeType.STR, 5.0D,
                AttributeModifierOperation.ADDITION, SOURCE));
        access.addModifier(new AttributeModifier("slot_bonus", AttributeType.STR, 8.0D,
                AttributeModifierOperation.ADDITION, SOURCE));

        assertEquals(28.0D, access.get(AttributeType.STR), 1.0E-9);
    }

    @Test
    void sameModifierIdCanExistInDifferentSources() {
        Attributes attributes = new Attributes(20.0D);
        CharacterAttributeAccess access = new CharacterAttributeAccess(attributes);

        ResourceLocation otherSource = ResourceLocation.fromNamespaceAndPath("the_unwritten", "other");

        access.addModifier(new AttributeModifier("bonus", AttributeType.STR, 5.0D,
                AttributeModifierOperation.ADDITION, SOURCE));
        access.addModifier(new AttributeModifier("bonus", AttributeType.STR, 7.0D,
                AttributeModifierOperation.ADDITION, otherSource));

        assertEquals(32.0D, access.get(AttributeType.STR), 1.0E-9);
    }
}
