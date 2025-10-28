package com.nine.travelerscompass.network;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.network.packet.PacketHolder;
import com.nine.travelerscompass.network.packet.c2s.C2SPacket;
import com.nine.travelerscompass.network.packet.s2c.S2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NeoForgeNetworkHandler {
	
	public static void init(RegisterPayloadHandlersEvent event) {
		TCNetworkManager.init();
		PayloadRegistrar registrar = event.registrar(TCCommon.MODID).versioned(String.valueOf(TCCommon.NETWORK_PROTOCOL_VERSION));
		
		for (var raw : TCNetworkManager.C2S_PACKETS) {
			@SuppressWarnings("unchecked")
			var holder = (PacketHolder<C2SPacket>) raw;
			registrar.playToServer(holder.id(), holder.codec(),
					((packet, context) -> context.enqueueWork(() -> packet.handle((ServerPlayer) context.player())))
			);
		}
		for (var raw : TCNetworkManager.S2C_PACKETS) {
			@SuppressWarnings("unchecked")
			var holder = (PacketHolder<S2CPacket>) raw;
			registrar.playToClient(holder.id(), holder.codec(),
					((packet, context) -> context.enqueueWork(packet::handle))
			);
		}
	}
	
}
