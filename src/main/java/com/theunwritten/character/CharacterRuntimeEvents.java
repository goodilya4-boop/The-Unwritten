package com.theunwritten.character;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Map;

public final class CharacterRuntimeEvents {
    private CharacterRuntimeEvents() {
    }

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (player.tickCount % 20 == 0) {
            CharacterData data = player.getData(CharacterAttachments.CHARACTER_DATA);
            Map<StatType, Double> stats = data.stats();

            // Vanilla health remains the authoritative damage state for now.
            // Mirror it into the character resource before applying regeneration.
            data.resources().set(
                    ResourceType.HEALTH,
                    Math.max(0.0D, Math.min(player.getHealth(), data.resources().max(ResourceType.HEALTH, stats)))
            );

            data.resources().regenerate(stats);
            CharacterStatSync.apply(player);
            com.theunwritten.network.ResourceSyncServer.send(player);

            double resourceHealth = data.resources().current(ResourceType.HEALTH);
            if (player.getHealth() > 0.0F && Math.abs(player.getHealth() - resourceHealth) > 0.001D) {
                player.setHealth((float) resourceHealth);
            }
        }
    }

    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CharacterStatSync.apply(player);
            initializeHealthResource(player);
            com.theunwritten.network.ResourceSyncServer.send(player);
        }
    }

    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CharacterData data = player.getData(CharacterAttachments.CHARACTER_DATA);
            CharacterStatSync.apply(player);
            Map<StatType, Double> stats = data.stats();
            data.resources().set(ResourceType.HEALTH, player.getHealth());
            data.resources().clampToMax(stats);
        }
    }

    private static void initializeHealthResource(ServerPlayer player) {
        CharacterData data = player.getData(CharacterAttachments.CHARACTER_DATA);
        Map<StatType, Double> stats = data.stats();
        data.resources().set(
                ResourceType.HEALTH,
                Math.max(0.0D, Math.min(player.getHealth(), data.resources().max(ResourceType.HEALTH, stats)))
        );
        data.resources().clampToMax(stats);
    }
}
