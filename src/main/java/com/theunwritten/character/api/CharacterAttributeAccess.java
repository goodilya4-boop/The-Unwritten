package com.theunwritten.character.api;

import com.theunwritten.character.data.Attributes;
import com.theunwritten.character.data.AttributeType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** Default AttributeAccess implementation backed by the character's persistent Attributes. */
public final class CharacterAttributeAccess implements AttributeAccess {
    private final Attributes base;
    private final Map<AttributeType, List<AttributeModifier>> modifiers = new EnumMap<>(AttributeType.class);

    public CharacterAttributeAccess(Attributes base) {
        this.base = base;
    }

    @Override
    public double get(AttributeType attribute) {
        return getBase(attribute) + getModifierTotal(attribute);
    }

    @Override
    public double getBase(AttributeType attribute) {
        return base.get(attribute);
    }

    @Override
    public double getModifierTotal(AttributeType attribute) {
        return modifiers.getOrDefault(attribute, List.of()).stream()
                .mapToDouble(AttributeModifier::amount)
                .sum();
    }

    @Override
    public void setBase(AttributeType attribute, double value) {
        base.set(attribute, value);
    }

    @Override
    public void add(AttributeType attribute, double amount) {
        base.add(attribute, amount);
    }

    @Override
    public void addModifier(AttributeModifier modifier) {
        modifiers.computeIfAbsent(modifier.attribute(), ignored -> new ArrayList<>()).add(modifier);
    }

    @Override
    public boolean removeModifier(AttributeModifier modifier) {
        List<AttributeModifier> list = modifiers.get(modifier.attribute());
        if (list == null) return false;
        boolean removed = list.remove(modifier);
        if (list.isEmpty()) modifiers.remove(modifier.attribute());
        return removed;
    }

    @Override
    public int removeModifiersFromSource(String source) {
        if (source == null) return 0;
        int removed = 0;
        for (AttributeType type : AttributeType.values()) {
            List<AttributeModifier> list = modifiers.get(type);
            if (list == null) continue;
            int before = list.size();
            list.removeIf(modifier -> source.equals(modifier.source()));
            removed += before - list.size();
            if (list.isEmpty()) modifiers.remove(type);
        }
        return removed;
    }

    @Override
    public void clearModifiers() {
        modifiers.clear();
    }
}
