package com.theunwritten.character;

public enum ResourceType {
    HEALTH(StatType.MAX_HEALTH, StatType.HEALTH_REGEN),
    MANA(StatType.MAX_MANA, StatType.MANA_REGEN),
    STAMINA(StatType.MAX_STAMINA, StatType.STAMINA_REGEN),
    FOCUS(StatType.MAX_FOCUS, StatType.FOCUS_REGEN);

    private final StatType maxStat;
    private final StatType regenStat;

    ResourceType(StatType maxStat, StatType regenStat) {
        this.maxStat = maxStat;
        this.regenStat = regenStat;
    }

    public StatType maxStat() {
        return maxStat;
    }

    public StatType regenStat() {
        return regenStat;
    }
}
