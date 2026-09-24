package com.theunwritten.character.data;

import com.theunwritten.character.api.AttributeAccess;

/**
 * Derived character statistics. Values are recalculated from source data and
 * are therefore not persisted as independent state.
 */
public record CharacterStats(
        double maxHealth,
        double maxStamina,
        double maxMana,
        double maxFocus,
        double physicalPower,
        double movementMultiplier,
        double accuracy,
        double control) {

    public static CharacterStats calculate(Attributes attributes) {
        return calculate(new BaseAttributeAccess(attributes), 0.0D, 0.0D, 0.0D);
    }

    public static CharacterStats calculate(AttributeAccess attributes) {
        return calculate(attributes, 0.0D, 0.0D, 0.0D);
    }

    public static CharacterStats calculate(
            AttributeAccess attributes,
            double equipmentPhysicalPower,
            double techniquePhysicalPower,
            double magicalProgression) {
        double str = attributes.get(AttributeType.STR);
        double dex = attributes.get(AttributeType.DEX);
        double end = attributes.get(AttributeType.END);
        double intelligence = attributes.get(AttributeType.INT);
        double will = attributes.get(AttributeType.WIL);
        double perception = attributes.get(AttributeType.PER);

        return new CharacterStats(
                100.0D + end * 9.0D + str * 2.0D,
                100.0D + end * 5.0D + dex * 2.0D,
                100.0D + will * 5.0D + intelligence * 3.0D + magicalProgression,
                100.0D + will * 4.0D + intelligence * 2.0D,
                str + end * 0.25D + equipmentPhysicalPower + techniquePhysicalPower,
                1.0D + dex / 200.0D,
                dex * 0.6D + perception * 0.4D,
                dex * 0.35D + will * 0.35D + intelligence * 0.30D);
    }

    private static final class BaseAttributeAccess implements AttributeAccess {
        private final Attributes attributes;

        private BaseAttributeAccess(Attributes attributes) {
            this.attributes = attributes;
        }

        @Override public double get(AttributeType attribute) { return attributes.get(attribute); }
        @Override public double getBase(AttributeType attribute) { return attributes.get(attribute); }
        @Override public double getModifierTotal(AttributeType attribute) { return 0.0D; }
        @Override public void setBase(AttributeType attribute, double value) { attributes.set(attribute, value); }
        @Override public void add(AttributeType attribute, double amount) { attributes.add(attribute, amount); }
        @Override public void addModifier(com.theunwritten.character.attribute.AttributeModifier modifier) {
            throw new UnsupportedOperationException("BaseAttributeAccess does not support modifiers");
        }
        @Override public boolean removeModifier(com.theunwritten.character.attribute.AttributeModifier modifier) { return false; }
        @Override public int removeModifiersFromSource(net.minecraft.resources.ResourceLocation source) { return 0; }
        @Override public void clearModifiers() {}
    }
}
