package com.theunwritten.combat;

public enum DefenseType {
    UNARMED(DefenseCategory.UNARMED),
    SMALL_SHIELD(DefenseCategory.SHIELD),
    MEDIUM_SHIELD(DefenseCategory.SHIELD),
    LARGE_SHIELD(DefenseCategory.SHIELD),
    LIGHT_ARMOR(DefenseCategory.ARMOR),
    MEDIUM_ARMOR(DefenseCategory.ARMOR),
    HEAVY_ARMOR(DefenseCategory.ARMOR);

    private final DefenseCategory category;

    DefenseType(DefenseCategory category) {
        this.category = category;
    }

    public DefenseCategory category() {
        return category;
    }
}
