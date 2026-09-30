package com.theunwritten.character;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.Arrays;

public final class Attributes implements INBTSerializable<CompoundTag> {
    public static final double DEFAULT_VALUE = 20.0D;
    public static final double MIN_VALUE = 0.0D;

    private static final String NBT_PREFIX = "attribute_";
    private final double[] values;

    public Attributes() {
        this(DEFAULT_VALUE);
    }

    public Attributes(double defaultValue) {
        validateValue(defaultValue);
        this.values = new double[AttributeType.values().length];
        Arrays.fill(this.values, defaultValue);
    }

    private Attributes(double[] values) {
        if (values.length != AttributeType.values().length) {
            throw new IllegalArgumentException("Invalid attribute array length: " + values.length);
        }
        this.values = values.clone();
        for (double value : this.values) {
            validateValue(value);
        }
    }

    public double get(AttributeType type) {
        return values[type.ordinal()];
    }

    public void set(AttributeType type, double value) {
        validateValue(value);
        values[type.ordinal()] = value;
    }

    public void add(AttributeType type, double amount) {
        if (!Double.isFinite(amount)) {
            throw new IllegalArgumentException("Attribute amount must be finite");
        }
        set(type, get(type) + amount);
    }

    public Attributes copy() {
        return new Attributes(values);
    }

    public static Attributes of(
            double str,
            double dex,
            double end,
            double intel,
            double wil,
            double per
    ) {
        Attributes attributes = new Attributes(0.0D);
        attributes.set(AttributeType.STR, str);
        attributes.set(AttributeType.DEX, dex);
        attributes.set(AttributeType.END, end);
        attributes.set(AttributeType.INT, intel);
        attributes.set(AttributeType.WIL, wil);
        attributes.set(AttributeType.PER, per);
        return attributes;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        for (AttributeType type : AttributeType.values()) {
            tag.putDouble(NBT_PREFIX + type.name(), get(type));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        for (AttributeType type : AttributeType.values()) {
            String key = NBT_PREFIX + type.name();
            if (tag.contains(key)) {
                set(type, tag.getDouble(key));
            }
        }
    }

    private static void validateValue(double value) {
        if (!Double.isFinite(value) || value < MIN_VALUE) {
            throw new IllegalArgumentException(
                    "Attribute value must be finite and >= " + MIN_VALUE + ": " + value
            );
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Attributes that)) return false;
        return Arrays.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public String toString() {
        return "Attributes{" +
                "STR=" + get(AttributeType.STR) +
                ", DEX=" + get(AttributeType.DEX) +
                ", END=" + get(AttributeType.END) +
                ", INT=" + get(AttributeType.INT) +
                ", WIL=" + get(AttributeType.WIL) +
                ", PER=" + get(AttributeType.PER) +
                '}';
    }
}
