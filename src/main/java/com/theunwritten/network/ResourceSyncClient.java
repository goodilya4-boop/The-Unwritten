package com.theunwritten.network;

import com.theunwritten.character.CharacterResources;
import com.theunwritten.character.ResourceType;
import net.minecraft.client.player.LocalPlayer;

public final class ResourceSyncClient {
    private static CharacterResources resources = new CharacterResources();
    private static double[] maximum = new double[ResourceType.values().length];

    private ResourceSyncClient() {
    }

    public static void apply(LocalPlayer player, double[] current, double[] max) {
        for (ResourceType type : ResourceType.values()) {
            resources.set(type, current[type.ordinal()]);
            maximum[type.ordinal()] = max[type.ordinal()];
        }
    }

    public static double current(ResourceType type) {
        return resources.current(type);
    }

    public static double maximum(ResourceType type) {
        return maximum[type.ordinal()];
    }

    public static double ratio(ResourceType type) {
        double max = maximum(type);
        return max <= 0.0D ? 0.0D : Math.max(0.0D, Math.min(1.0D, current(type) / max));
    }

    public static void clear() {
        resources = new CharacterResources();
        maximum = new double[ResourceType.values().length];
    }
}
