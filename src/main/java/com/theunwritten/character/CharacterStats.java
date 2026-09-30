package com.theunwritten.character;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class CharacterStats {
    private CharacterStats() {
    }

    public static Map<StatType, Double> calculate(Attributes attributes) {
        return calculate(attributes, List.of());
    }

    public static Map<StatType, Double> calculate(
            Attributes attributes,
            List<StatModifier> modifiers
    ) {
        Objects.requireNonNull(attributes, "attributes");
        Objects.requireNonNull(modifiers, "modifiers");

        double s = attributes.get(AttributeType.STR);
        double d = attributes.get(AttributeType.DEX);
        double e = attributes.get(AttributeType.END);
        double i = attributes.get(AttributeType.INT);
        double w = attributes.get(AttributeType.WIL);
        double p = attributes.get(AttributeType.PER);

        EnumMap<StatType, Double> stats = new EnumMap<>(StatType.class);

        stats.put(StatType.MAX_HEALTH, 100.0D + e * 9.0D + s * 2.0D);
        stats.put(StatType.HEALTH_REGEN, 0.5D + e * 0.04D + w * 0.02D);
        stats.put(StatType.MAX_STAMINA, 100.0D + e * 5.0D + d * 2.0D);
        stats.put(StatType.STAMINA_REGEN, 4.0D + e * 0.08D + d * 0.04D);
        stats.put(StatType.MAX_MANA, 100.0D + w * 5.0D + i * 3.0D);
        stats.put(StatType.MANA_REGEN, 2.0D + w * 0.04D + i * 0.05D);
        stats.put(StatType.MAX_FOCUS, 100.0D + w * 4.0D + i * 2.0D);
        stats.put(StatType.FOCUS_REGEN, 3.0D + w * 0.05D + p * 0.03D);

        stats.put(StatType.PHYSICAL_POWER, s + e * 0.25D);
        stats.put(StatType.PHYSICAL_DEFENSE, e * 1.5D + s * 0.5D);
        stats.put(StatType.MOVEMENT_SPEED, 1.0D + d / 200.0D);
        stats.put(StatType.ATTACK_SPEED, 1.0D + d / 200.0D);
        stats.put(StatType.ACCURACY, d * 0.6D + p * 0.4D);
        stats.put(StatType.EVASION, d * 0.55D + p * 0.45D);
        stats.put(StatType.CRITICAL_CHANCE, d * 0.0015D + p * 0.001D);
        stats.put(StatType.CRITICAL_POWER, 1.5D + d / 200.0D + p / 500.0D);
        stats.put(StatType.REACTION, d * 0.5D + p * 0.3D + w * 0.2D);
        stats.put(StatType.CONTROL, d * 0.35D + w * 0.35D + i * 0.30D);

        stats.put(StatType.MAGIC_POWER, i * 0.6D + w * 0.4D);
        stats.put(StatType.MAGIC_DEFENSE, w * 1.5D + i * 0.5D);
        stats.put(StatType.SPELL_CAST_SPEED, 1.0D + i / 250.0D + w / 400.0D);
        stats.put(StatType.MAGIC_PENETRATION, i * 0.4D + p * 0.2D);
        stats.put(StatType.MENTAL_RESISTANCE, w * 1.2D + i * 0.5D);
        stats.put(StatType.CONCENTRATION, w * 0.5D + p * 0.3D + i * 0.2D);
        stats.put(StatType.SENSE_RANGE, p * 1.5D + i * 0.2D);
        stats.put(StatType.DETECTION, p * 0.7D + i * 0.3D);
        stats.put(StatType.TENACITY, e * 0.4D + w * 0.6D);
        stats.put(StatType.LUCK, p * 0.5D + w * 0.25D + i * 0.25D);

        applyModifiers(stats, modifiers);
        return Map.copyOf(stats);
    }

    private static void applyModifiers(
            EnumMap<StatType, Double> stats,
            List<StatModifier> modifiers
    ) {
        for (StatType stat : StatType.values()) {
            double base = stats.get(stat);
            double addition = 0.0D;
            double multiplyBase = 0.0D;
            double multiplyTotal = 0.0D;

            for (StatModifier modifier : modifiers) {
                if (modifier.stat() != stat) {
                    continue;
                }

                switch (modifier.operation()) {
                    case ADDITION -> addition += modifier.amount();
                    case MULTIPLY_BASE -> multiplyBase += modifier.amount();
                    case MULTIPLY_TOTAL -> multiplyTotal += modifier.amount();
                }
            }

            double value = base + addition;
            value += base * multiplyBase;
            value *= 1.0D + multiplyTotal;
            stats.put(stat, value);
        }
    }
}
