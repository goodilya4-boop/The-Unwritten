package com.theunwritten.combat;

public enum WeaponType {
    SWORD(WeaponCategory.CUTTING),
    DAGGER(WeaponCategory.CUTTING),
    SABER(WeaponCategory.CUTTING),
    KATANA(WeaponCategory.CUTTING),
    SCIMITAR(WeaponCategory.CUTTING),

    SPEAR(WeaponCategory.PIERCING),
    PIKE(WeaponCategory.PIERCING),
    RAPIER(WeaponCategory.PIERCING),
    ESTOC(WeaponCategory.PIERCING),

    MACE(WeaponCategory.BLUNT),
    HAMMER(WeaponCategory.BLUNT),
    FLAIL(WeaponCategory.BLUNT),
    WAR_STAFF(WeaponCategory.BLUNT),

    AXE(WeaponCategory.CHOPPING),
    BATTLE_AXE(WeaponCategory.CHOPPING),
    HALBERD(WeaponCategory.CHOPPING),
    GLAIVE(WeaponCategory.CHOPPING),

    BOW(WeaponCategory.RANGED),
    CROSSBOW(WeaponCategory.RANGED),
    THROWING_KNIFE(WeaponCategory.RANGED),
    JAVELIN(WeaponCategory.RANGED),
    THROWING_AXE(WeaponCategory.RANGED);

    private final WeaponCategory category;

    WeaponType(WeaponCategory category) {
        this.category = category;
    }

    public WeaponCategory category() {
        return category;
    }
}
