package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.network.packet.c2s.C2SPacket;
import com.nine.travelerscompass.network.packet.s2c.S2CPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public interface IPlatformNetworkHelper {

    void sendToServer(C2SPacket packet);

    void sendToClient(ServerPlayer player, S2CPacket packet);


}
