package com.theunwritten.character;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.Arrays;
import java.util.Map;

public final class CharacterResources implements INBTSerializable<CompoundTag> {
    public static final double DEFAULT_VALUE = 20.0D;
    private static final String NBT_PREFIX = "resource_";

    private final double[] current;

    public CharacterResources() {
        this(DEFAULT_VALUE);
    }

    public CharacterResources(double defaultValue) {
        validate(defaultValue);
        this.current = new double[ResourceType.values().length];
        Arrays.fill(this.current, defaultValue);
    }

    public double current(ResourceType type) {
        return current[type.ordinal()];
    }

    public void set(ResourceType type, double value) {
        validate(value);
        current[type.ordinal()] = value;
    }

    public void add(ResourceType type, double amount) {
        if (!Double.isFinite(amount)) {
            throw new IllegalArgumentException("Resource amount must be finite");
        }
        set(type, current(type) + amount);
    }

    public double max(ResourceType type, Map<StatType, Double> stats) {
        return Math.max(0.0D, stats.getOrDefault(type.maxStat(), DEFAULT_VALUE));
    }

    public double ratio(ResourceType type, Map<StatType, Double> stats) {
        double maximum = max(type, stats);
        return maximum <= 0.0D ? 0.0D : current(type) / maximum;
    }

    public void clampToMax(Map<StatType, Double> stats) {
        for (ResourceType type : ResourceType.values()) {
            set(type, Math.min(current(type), max(type, stats)));
        }
    }

    public void regenerate(Map<StatType, Double> stats) {
        for (ResourceType type : ResourceType.values()) {
            double maximum = max(type, stats);
            double regeneration = Math.max(0.0D, stats.getOrDefault(type.regenStat(), 0.0D));
            set(type, Math.min(maximum, current(type) + regeneration / 20.0D));
        }
    }

    public void fill(Map<StatType, Double> stats) {
        for (ResourceType type : ResourceType.values()) {
            set(type, max(type, stats));
        }
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        for (ResourceType type : ResourceType.values()) {
            tag.putDouble(NBT_PREFIX + type.name(), current(type));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        for (ResourceType type : ResourceType.values()) {
            String key = NBT_PREFIX + type.name();
            if (tag.contains(key)) {
                set(type, tag.getDouble(key));
            }
        }
    }

    private static void validate(double value) {
        if (!Double.isFinite(value) || value < 0.0D) {
            throw new IllegalArgumentException("Resource value must be finite and >= 0: " + value);
        }
    }
}
