package com.theunwritten.combat;

import java.util.Objects;

public final class CombatProfile {
    private WeaponSchool activeWeaponSchool;
    private DefenseSchool activeDefenseSchool;
    private WeaponType activeWeapon;
    private DefenseType activeDefense = DefenseType.UNARMED;
    private CombatState state = CombatState.INACTIVE;

    public WeaponSchool activeWeaponSchool() { return activeWeaponSchool; }
    public DefenseSchool activeDefenseSchool() { return activeDefenseSchool; }
    public WeaponType activeWeapon() { return activeWeapon; }
    public DefenseType activeDefense() { return activeDefense; }
    public CombatState state() { return state; }

    public void setWeaponSchool(WeaponSchool school) {
        activeWeaponSchool = Objects.requireNonNull(school, "school");
    }

    public void setDefenseSchool(DefenseSchool school) {
        activeDefenseSchool = Objects.requireNonNull(school, "school");
    }

    public void setWeapon(WeaponType weapon) {
        if (activeWeaponSchool != null && !activeWeaponSchool.supports(weapon)) {
            throw new IllegalArgumentException("Weapon is not supported by the active weapon school");
        }
        activeWeapon = Objects.requireNonNull(weapon, "weapon");
    }

    public void setDefense(DefenseType defense) {
        if (activeDefenseSchool != null && !activeDefenseSchool.supports(defense)) {
            throw new IllegalArgumentException("Defense is not supported by the active defense school");
        }
        activeDefense = Objects.requireNonNull(defense, "defense");
    }

    public void setState(CombatState state) {
        this.state = Objects.requireNonNull(state, "state");
    }
}
