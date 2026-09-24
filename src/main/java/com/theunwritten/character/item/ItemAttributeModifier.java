package com.theunwritten.character.item;

import com.theunwritten.character.api.AttributeModifier;
import com.theunwritten.character.data.AttributeType;

/** Attribute contribution supplied by an equipped item. */
public record ItemAttributeModifier(AttributeType attribute, double amount) {
    public AttributeModifier asCharacterModifier(String itemId) {
        return new AttributeModifier(attribute, amount, itemId);
    }
}
