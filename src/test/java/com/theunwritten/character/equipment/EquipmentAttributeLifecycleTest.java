package com.theunwritten.character.equipment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.theunwritten.character.attribute.AttributeModifier;
import com.theunwritten.character.attribute.AttributeModifierOperation;
import com.theunwritten.character.data.AttributeType;
import com.theunwritten.character.data.CharacterData;
import com.theunwritten.item.data.ItemAttributeModifiers;

import net.minecraft.resources.ResourceLocation;

class EquipmentAttributeLifecycleTest {
    private static final ResourceLocation SOURCE =
            ResourceLocation.fromNamespaceAndPath("the_unwritten", "equipment/test");

    @Test
    void reconciliationRemovesOldSlotModifiersBeforeApplyingNewOnes() {
        CharacterData character = new CharacterData();

        EquipmentAttributeLifecycle.applyDeclaredModifiers(
                character,
                new ItemAttributeModifiers(List.of(
                        new AttributeModifier(
                                "bonus",
                                AttributeType.STR,
                                5.0D,
                                AttributeModifierOperation.ADDITION,
                                SOURCE))),
                SOURCE);

        assertEquals(25.0D, character.attributeAccess().get(AttributeType.STR), 1.0E-9);

        EquipmentAttributeLifecycle.applyDeclaredModifiers(
                character,
                new ItemAttributeModifiers(List.of(
                        new AttributeModifier(
                                "replacement",
                                AttributeType.DEX,
                                7.0D,
                                AttributeModifierOperation.ADDITION,
                                SOURCE))),
                SOURCE);

        assertEquals(20.0D, character.attributeAccess().get(AttributeType.STR), 1.0E-9);
        assertEquals(27.0D, character.attributeAccess().get(AttributeType.DEX), 1.0E-9);
    }

    @Test
    void repeatedReconciliationDoesNotDuplicateModifiers() {
        CharacterData character = new CharacterData();
        ItemAttributeModifiers declared = new ItemAttributeModifiers(List.of(
                new AttributeModifier(
                        "bonus",
                        AttributeType.STR,
                        5.0D,
                        AttributeModifierOperation.ADDITION,
                        SOURCE)));

        EquipmentAttributeLifecycle.applyDeclaredModifiers(character, declared, SOURCE);
        EquipmentAttributeLifecycle.applyDeclaredModifiers(character, declared, SOURCE);

        assertEquals(25.0D, character.attributeAccess().get(AttributeType.STR), 1.0E-9);
    }

    @Test
    void emptyEquipmentRemovesPreviousModifiers() {
        CharacterData character = new CharacterData();

        EquipmentAttributeLifecycle.applyDeclaredModifiers(
                character,
                new ItemAttributeModifiers(List.of(
                        new AttributeModifier(
                                "bonus",
                                AttributeType.WIL,
                                9.0D,
                                AttributeModifierOperation.ADDITION,
                                SOURCE))),
                SOURCE);

        EquipmentAttributeLifecycle.applyDeclaredModifiers(
                character,
                ItemAttributeModifiers.empty(),
                SOURCE);

        assertEquals(20.0D, character.attributeAccess().get(AttributeType.WIL), 1.0E-9);
    }
}
