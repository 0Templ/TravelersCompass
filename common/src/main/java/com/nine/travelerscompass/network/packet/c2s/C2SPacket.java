package com.nine.travelerscompass.network.packet.c2s;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public interface C2SPacket extends CustomPacketPayload {
	
	void handle(ServerPlayer player);
	
	
}
