package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.network.packet.c2s.C2SPacket;
import com.nine.travelerscompass.network.packet.s2c.S2CPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class NeoForgePlatformNetworkHelper implements IPlatformNetworkHelper {

    @Override
    public void sendToServer(C2SPacket packet){
        PacketDistributor.sendToServer(packet);
    }

    @Override
    public void sendToClient(ServerPlayer player, S2CPacket packet){
        PacketDistributor.sendToPlayer(player, packet);

    }

}
