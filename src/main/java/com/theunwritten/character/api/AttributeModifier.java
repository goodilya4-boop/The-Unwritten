package com.theunwritten.character.api;

import com.theunwritten.character.data.AttributeType;

/** A temporary or persistent contribution applied on top of a base attribute. */
public record AttributeModifier(AttributeType attribute, double amount, String source) {
    public AttributeModifier {
        if (attribute == null) {
            throw new IllegalArgumentException("Attribute cannot be null");
        }
        if (!Double.isFinite(amount)) {
            throw new IllegalArgumentException("Modifier amount must be finite");
        }
        source = source == null || source.isBlank() ? "unknown" : source;
    }
}
