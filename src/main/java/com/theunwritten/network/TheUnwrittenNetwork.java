package com.theunwritten.network;

import com.theunwritten.TheUnwritten;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class TheUnwrittenNetwork {
    private static final String VERSION = "1";
    public static final CustomPacketPayload.Type<ResourceSyncPacket> RESOURCE_SYNC_TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(TheUnwritten.MODID, "resource_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ResourceSyncPacket> RESOURCE_SYNC_CODEC =
            new StreamCodec<>() {
                @Override
                public ResourceSyncPacket decode(RegistryFriendlyByteBuf buf) {
                    return new ResourceSyncPacket(buf);
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, ResourceSyncPacket packet) {
                    packet.write(buf);
                }
            };

    private TheUnwrittenNetwork() {
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(VERSION);
        registrar.playToClient(RESOURCE_SYNC_TYPE, RESOURCE_SYNC_CODEC, ResourceSyncPacket::handle);
    }

    public static void sendToPlayer(net.minecraft.server.level.ServerPlayer player, ResourceSyncPacket packet) {
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player, packet);
    }
}
