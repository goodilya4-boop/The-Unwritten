package com.theunwritten.character.data;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

/**
 * Current values of the six fundamental attributes.
 *
 * This class is intentionally independent from derived stats. It is one of
 * the primary sources of truth for character development.
 */
public final class Attributes implements INBTSerializable<CompoundTag> {
    public static final double MIN_VALUE = 0.0D;
    public static final double BASE_SCALE_MAX = 100.0D;

    private final double[] values = new double[AttributeType.values().length];

    public Attributes() {
        this(20.0D);
    }

    public Attributes(double initialValue) {
        for (AttributeType type : AttributeType.values()) {
            values[type.ordinal()] = Math.max(MIN_VALUE, initialValue);
        }
    }

    public double get(AttributeType type) {
        return values[type.ordinal()];
    }

    public void set(AttributeType type, double value) {
        if (!Double.isFinite(value) || value < MIN_VALUE) {
            throw new IllegalArgumentException("Attribute value must be finite and non-negative");
        }
        values[type.ordinal()] = value;
    }

    public void add(AttributeType type, double amount) {
        set(type, get(type) + amount);
    }

    public Attributes copy() {
        Attributes copy = new Attributes(0.0D);
        for (AttributeType type : AttributeType.values()) {
            copy.set(type, get(type));
        }
        return copy;
    }

    @Override
    public CompoundTag serializeNBT(net.minecraft.core.HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        for (AttributeType type : AttributeType.values()) {
            tag.putDouble(type.name(), get(type));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(net.minecraft.core.HolderLookup.Provider provider, CompoundTag tag) {
        for (AttributeType type : AttributeType.values()) {
            if (tag.contains(type.name())) {
                set(type, tag.getDouble(type.name()));
            }
        }
    }
}
