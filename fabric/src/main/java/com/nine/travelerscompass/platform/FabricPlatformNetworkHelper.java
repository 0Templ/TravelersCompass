package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.network.packet.c2s.C2SPacket;
import com.nine.travelerscompass.network.packet.s2c.S2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class FabricPlatformNetworkHelper implements IPlatformNetworkHelper {

    @Override
    public void sendToServer(C2SPacket packet){
        ClientPlayNetworking.send(packet);
    }

    @Override
    public void sendToClient(ServerPlayer player, S2CPacket packet){
        ServerPlayNetworking.send(player, packet);
    }

}
