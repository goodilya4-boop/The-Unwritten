package com.theunwritten.combat;

import java.util.Set;

public final class ImperialFencing {
    public static final String ID = "imperial_fencing";

    public static final WeaponSchool SCHOOL = new WeaponSchool(
            ID,
            "Imperial Fencing",
            Set.of(
                    WeaponType.SWORD,
                    WeaponType.DAGGER,
                    WeaponType.SABER,
                    WeaponType.RAPIER,
                    WeaponType.ESTOC
            )
    );

    private ImperialFencing() {
    }
}
