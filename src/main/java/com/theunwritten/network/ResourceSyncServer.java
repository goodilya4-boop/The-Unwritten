package com.theunwritten.network;

import net.minecraft.server.level.ServerPlayer;

public final class ResourceSyncServer {
    private ResourceSyncServer() {
    }

    public static void send(ServerPlayer player) {
        // Network transport registration is completed in TheUnwritten.
        // This method is the single server-side entry point for resource snapshots.
        TheUnwrittenNetwork.sendToPlayer(player, ResourceSyncPacket.from(player));
    }
}
