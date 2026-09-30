package com.theunwritten.character;

import java.util.Objects;

public record StatModifier(
        String id,
        StatType stat,
        double amount,
        StatModifierOperation operation
) {
    public StatModifier {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(stat, "stat");
        Objects.requireNonNull(operation, "operation");

        if (id.isBlank()) {
            throw new IllegalArgumentException("Modifier id must not be blank");
        }
        if (!Double.isFinite(amount)) {
            throw new IllegalArgumentException("Modifier amount must be finite");
        }
    }

    public static StatModifier addition(String id, StatType stat, double amount) {
        return new StatModifier(id, stat, amount, StatModifierOperation.ADDITION);
    }
}
