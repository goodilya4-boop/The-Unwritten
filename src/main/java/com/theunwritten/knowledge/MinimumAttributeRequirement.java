package com.theunwritten.knowledge;

import com.theunwritten.character.AttributeType;
import com.theunwritten.character.CharacterData;

import java.util.Objects;

public record MinimumAttributeRequirement(AttributeType attribute, double value)
        implements KnowledgeRequirement {

    public MinimumAttributeRequirement {
        Objects.requireNonNull(attribute, "attribute");
        if (!Double.isFinite(value) || value < 0.0D) {
            throw new IllegalArgumentException("Requirement value must be finite and >= 0");
        }
    }

    @Override
    public boolean isMet(CharacterData character) {
        return character.attributes().get(attribute) >= value;
    }

    @Override
    public String description() {
        return attribute.name() + " >= " + value;
    }
}
