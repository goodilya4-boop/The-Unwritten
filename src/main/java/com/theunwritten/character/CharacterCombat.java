package com.theunwritten.character;

import net.minecraft.server.level.ServerPlayer;

import java.util.concurrent.ThreadLocalRandom;

public final class CharacterCombat {
    private CharacterCombat() {
    }

    public static double calculateBaseDamage(ServerPlayer attacker) {
        return attacker.getAttributeValue(
                net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE);
    }

    public static double calculateDamage(ServerPlayer attacker) {
        CharacterData data = attacker.getData(CharacterAttachments.CHARACTER_DATA);
        double baseDamage = calculateBaseDamage(attacker);
        double criticalChance = data.stats().get(StatType.CRITICAL_CHANCE);
        double criticalPower = data.stats().get(StatType.CRITICAL_POWER);

        if (ThreadLocalRandom.current().nextDouble() < criticalChance) {
            return baseDamage * criticalPower;
        }

        return baseDamage;
    }
}
