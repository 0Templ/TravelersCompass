package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.network.packet.c2s.C2SPacket;
import com.nine.travelerscompass.network.packet.s2c.S2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

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
