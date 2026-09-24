package com.theunwritten.character.api;

import com.theunwritten.character.data.AttributeType;

/** Public API used by gameplay systems to read and modify character attributes. */
public interface AttributeAccess {
    double get(AttributeType attribute);

    double getBase(AttributeType attribute);

    double getModifierTotal(AttributeType attribute);

    void setBase(AttributeType attribute, double value);

    void add(AttributeType attribute, double amount);

    void addModifier(AttributeModifier modifier);

    boolean removeModifier(AttributeModifier modifier);

    int removeModifiersFromSource(String source);

    void clearModifiers();
}
