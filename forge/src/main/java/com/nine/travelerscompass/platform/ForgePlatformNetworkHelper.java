package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.network.ForgeNetworkHandler;
import com.nine.travelerscompass.network.packet.c2s.C2SPacket;
import com.nine.travelerscompass.network.packet.s2c.S2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class ForgePlatformNetworkHelper implements IPlatformNetworkHelper {
	
	@Override
	public void sendToServer(C2SPacket packet) {
		ForgeNetworkHandler.CHANNEL.send(packet, PacketDistributor.SERVER.noArg());
	}
	
	@Override
	public void sendToClient(ServerPlayer player, S2CPacket packet) {
		ForgeNetworkHandler.CHANNEL.send(packet, PacketDistributor.PLAYER.with(player));
	}
	
}
