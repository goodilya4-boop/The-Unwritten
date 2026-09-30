package com.theunwritten.combat;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public record WeaponSchool(
        String id,
        String name,
        Set<WeaponType> supportedWeapons
) {
    public WeaponSchool {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(supportedWeapons, "supportedWeapons");
        if (id.isBlank() || name.isBlank()) {
            throw new IllegalArgumentException("School id and name must not be blank");
        }
        supportedWeapons = Set.copyOf(supportedWeapons);
    }

    public boolean supports(WeaponType weapon) {
        return supportedWeapons.contains(weapon);
    }
}
