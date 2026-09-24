package com.theunwritten.character.api;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.theunwritten.character.attribute.AttributeModifier;
import com.theunwritten.character.data.AttributeType;
import com.theunwritten.character.data.Attributes;

/** Default AttributeAccess implementation backed by persistent base attributes. */
public final class CharacterAttributeAccess implements AttributeAccess {
    private final Attributes base;
    private final Map<AttributeType, List<AttributeModifier>> modifiers = new EnumMap<>(AttributeType.class);

    public CharacterAttributeAccess(Attributes base) {
        this.base = Objects.requireNonNull(base, "base");
    }

    @Override
    public double get(AttributeType attribute) {
        Objects.requireNonNull(attribute, "attribute");
        double baseValue = getBase(attribute);
        double additions = 0.0D;
        double multiplyBase = 0.0D;
        double multiplyTotal = 0.0D;
        for (AttributeModifier modifier : modifiers.getOrDefault(attribute, List.of())) {
            switch (modifier.operation()) {
                case ADDITION -> additions += modifier.amount();
                case MULTIPLY_BASE -> multiplyBase += modifier.amount();
                case MULTIPLY_TOTAL -> multiplyTotal += modifier.amount();
            }
        }
        double value = baseValue + additions + baseValue * multiplyBase;
        return value * (1.0D + multiplyTotal);
    }

    @Override
    public double getBase(AttributeType attribute) {
        Objects.requireNonNull(attribute, "attribute");
        return base.get(attribute);
    }

    @Override
    public double getModifierTotal(AttributeType attribute) {
        return get(attribute) - getBase(attribute);
    }

    @Override
    public void setBase(AttributeType attribute, double value) {
        Objects.requireNonNull(attribute, "attribute");
        base.set(attribute, value);
    }

    @Override
    public void add(AttributeType attribute, double amount) {
        Objects.requireNonNull(attribute, "attribute");
        base.add(attribute, amount);
    }

    @Override
    public void addModifier(AttributeModifier modifier) {
        modifier = Objects.requireNonNull(modifier, "modifier");
        modifiers.computeIfAbsent(modifier.attribute(), ignored -> new ArrayList<>()).add(modifier);
    }

    @Override
    public boolean removeModifier(AttributeModifier modifier) {
        Objects.requireNonNull(modifier, "modifier");
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
            list.removeIf(modifier -> source.equals(modifier.source().toString()));
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
