package com.theunwritten.network;

import com.theunwritten.character.CharacterAttachments;
import com.theunwritten.character.CharacterData;
import com.theunwritten.character.ResourceType;
import com.theunwritten.character.StatType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Map;

public final class ResourceSyncPacket implements CustomPacketPayload {
    private final double[] current;
    private final double[] maximum;

    public ResourceSyncPacket(double[] current, double[] maximum) {
        this.current = current.clone();
        this.maximum = maximum.clone();
    }

    public ResourceSyncPacket(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        this.current = new double[size];
        this.maximum = new double[size];
        for (int i = 0; i < size; i++) {
            current[i] = buf.readDouble();
            maximum[i] = buf.readDouble();
        }
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(current.length);
        for (int i = 0; i < current.length; i++) {
            buf.writeDouble(current[i]);
            buf.writeDouble(maximum[i]);
        }
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof net.minecraft.client.player.LocalPlayer player) {
                ResourceSyncClient.apply(player, current, maximum);
            }
        });
    }

    public static ResourceSyncPacket from(ServerPlayer player) {
        CharacterData data = player.getData(CharacterAttachments.CHARACTER_DATA);
        Map<StatType, Double> stats = data.stats();
        double[] current = new double[ResourceType.values().length];
        double[] maximum = new double[ResourceType.values().length];
        for (ResourceType type : ResourceType.values()) {
            current[type.ordinal()] = data.resources().current(type);
            maximum[type.ordinal()] = data.resources().max(type, stats);
        }
        return new ResourceSyncPacket(current, maximum);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TheUnwrittenNetwork.RESOURCE_SYNC_TYPE;
    }
}
