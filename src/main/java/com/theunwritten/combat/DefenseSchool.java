package com.theunwritten.combat;

import java.util.Objects;
import java.util.Set;

public record DefenseSchool(
        String id,
        String name,
        Set<DefenseType> supportedDefense
) {
    public DefenseSchool {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(supportedDefense, "supportedDefense");
        if (id.isBlank() || name.isBlank()) {
            throw new IllegalArgumentException("School id and name must not be blank");
        }
        supportedDefense = Set.copyOf(supportedDefense);
    }

    public boolean supports(DefenseType defense) {
        return supportedDefense.contains(defense);
    }
}
