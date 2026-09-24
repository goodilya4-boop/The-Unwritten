package com.theunwritten.character.data;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

/** Current values of consumable character resources. */
public final class CharacterResources implements INBTSerializable<CompoundTag> {
    public enum Type { HEALTH, STAMINA, MANA, FOCUS }

    private final double[] current = new double[Type.values().length];
    private final double[] maximum = new double[Type.values().length];

    public CharacterResources() {
        for (Type type : Type.values()) {
            current[type.ordinal()] = 100.0D;
            maximum[type.ordinal()] = 100.0D;
        }
    }

    public double getCurrent(Type type) {
        return current[type.ordinal()];
    }

    public double getMaximum(Type type) {
        return maximum[type.ordinal()];
    }

    public void setMaximum(Type type, double value) {
        if (!Double.isFinite(value) || value < 0.0D) {
            throw new IllegalArgumentException("Resource maximum must be finite and non-negative");
        }
        int index = type.ordinal();
        maximum[index] = value;
        current[index] = Math.min(current[index], value);
    }

    public void setCurrent(Type type, double value) {
        int index = type.ordinal();
        current[index] = Math.max(0.0D, Math.min(value, maximum[index]));
    }

    public void restoreToMaximum(Type type) {
        current[type.ordinal()] = maximum[type.ordinal()];
    }

    public void applyStats(CharacterStats stats) {
        setMaximum(Type.HEALTH, stats.maxHealth());
        setMaximum(Type.STAMINA, stats.maxStamina());
        setMaximum(Type.MANA, stats.maxMana());
        setMaximum(Type.FOCUS, stats.maxFocus());
    }

    @Override
    public CompoundTag serializeNBT(net.minecraft.core.HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        for (Type type : Type.values()) {
            String key = type.name();
            tag.putDouble(key + "_Current", getCurrent(type));
            tag.putDouble(key + "_Maximum", getMaximum(type));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(net.minecraft.core.HolderLookup.Provider provider, CompoundTag tag) {
        for (Type type : Type.values()) {
            String key = type.name();
            if (tag.contains(key + "_Maximum")) {
                maximum[type.ordinal()] = Math.max(0.0D, tag.getDouble(key + "_Maximum"));
            }
            if (tag.contains(key + "_Current")) {
                setCurrent(type, tag.getDouble(key + "_Current"));
            }
        }
    }
}
