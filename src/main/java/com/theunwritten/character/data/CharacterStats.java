package com.theunwritten.character.data;

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
        return calculate(attributes, 0.0D, 0.0D, 0.0D);
    }

    public static CharacterStats calculate(
            Attributes attributes,
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
}
