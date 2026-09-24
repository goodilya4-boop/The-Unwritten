package com.theunwritten.character.data;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

/**
 * Soft development limits for fundamental attributes.
 *
 * Potential is not a hard cap. Progression systems may allow current values
 * to exceed potential at increased cost.
 */
public final class Potential implements INBTSerializable<CompoundTag> {
    private final double[] values = new double[AttributeType.values().length];

    public Potential() {
        this(100.0D);
    }

    public Potential(double initialValue) {
        for (AttributeType type : AttributeType.values()) {
            values[type.ordinal()] = Math.max(0.0D, initialValue);
        }
    }

    public double get(AttributeType type) {
        return values[type.ordinal()];
    }

    public void set(AttributeType type, double value) {
        if (!Double.isFinite(value) || value < 0.0D) {
            throw new IllegalArgumentException("Potential must be finite and non-negative");
        }
        values[type.ordinal()] = value;
    }

    public Potential copy() {
        Potential copy = new Potential(0.0D);
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
