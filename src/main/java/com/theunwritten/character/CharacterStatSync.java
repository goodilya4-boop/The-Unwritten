package com.theunwritten.character;

import com.theunwritten.TheUnwritten;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Map;

public final class CharacterStatSync {
    private static final ResourceLocation MAX_HEALTH_ID =
            ResourceLocation.fromNamespaceAndPath(TheUnwritten.MODID, "character_max_health");
    private static final ResourceLocation MOVEMENT_SPEED_ID =
            ResourceLocation.fromNamespaceAndPath(TheUnwritten.MODID, "character_movement_speed");
    private static final ResourceLocation ATTACK_SPEED_ID =
            ResourceLocation.fromNamespaceAndPath(TheUnwritten.MODID, "character_attack_speed");
    private static final ResourceLocation ATTACK_DAMAGE_ID =
            ResourceLocation.fromNamespaceAndPath(TheUnwritten.MODID, "character_physical_power");

    private static final double VANILLA_MAX_HEALTH = 20.0D;
    private static final double PHYSICAL_POWER_DAMAGE_SCALE = 0.1D;

    private CharacterStatSync() {
    }

    public static void apply(ServerPlayer player) {
        CharacterData data = player.getData(CharacterAttachments.CHARACTER_DATA);
        Map<StatType, Double> stats = data.stats();

        double oldMaxHealth = player.getMaxHealth();

        applyMaxHealth(player, stats.get(StatType.MAX_HEALTH));
        applyMultiplier(player, Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED_ID,
                stats.get(StatType.MOVEMENT_SPEED));
        applyMultiplier(player, Attributes.ATTACK_SPEED, ATTACK_SPEED_ID,
                stats.get(StatType.ATTACK_SPEED));
        applyPhysicalPower(player, stats.get(StatType.PHYSICAL_POWER));

        double newMaxHealth = player.getMaxHealth();
        if (newMaxHealth < oldMaxHealth && player.getHealth() > newMaxHealth) {
            player.setHealth((float) newMaxHealth);
        }
    }

    private static void applyMaxHealth(ServerPlayer player, double targetMaxHealth) {
        double modifierAmount = targetMaxHealth - VANILLA_MAX_HEALTH;

        player.getAttribute(Attributes.MAX_HEALTH).addOrUpdateTransientModifier(
                new AttributeModifier(
                        MAX_HEALTH_ID,
                        modifierAmount,
                        AttributeModifier.Operation.ADD_VALUE
                )
        );
    }

    private static void applyMultiplier(
            ServerPlayer player,
            net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute,
            ResourceLocation id,
            double multiplier
    ) {
        player.getAttribute(attribute).addOrUpdateTransientModifier(
                new AttributeModifier(
                        id,
                        multiplier - 1.0D,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                )
        );
    }

    private static void applyPhysicalPower(ServerPlayer player, double physicalPower) {
        player.getAttribute(Attributes.ATTACK_DAMAGE).addOrUpdateTransientModifier(
                new AttributeModifier(
                        ATTACK_DAMAGE_ID,
                        physicalPower * PHYSICAL_POWER_DAMAGE_SCALE,
                        AttributeModifier.Operation.ADD_VALUE
                )
        );
    }
}
