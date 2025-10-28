package com.nine.travelerscompass.network.packet.s2c;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface S2CPacket extends CustomPacketPayload {
	
	void handle();
	
	
}
