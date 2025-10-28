package com.nine.travelerscompass.network;

import com.nine.travelerscompass.network.packet.PacketHolder;
import com.nine.travelerscompass.network.packet.c2s.C2SPacket;
import com.nine.travelerscompass.network.packet.s2c.S2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class FabricNetworkHandler {
	
	public static void init() {
		TCNetworkManager.init();
		for (var holder : TCNetworkManager.C2S_PACKETS) {
			registerC2SPacket(holder);
		}
		for (var holder : TCNetworkManager.S2C_PACKETS) {
			registerS2CPacket(holder);
		}
	}
	
	public static void clientSetup() {
		for (var holder : TCNetworkManager.S2C_PACKETS) {
			registerS2CPacketReceiver(holder);
		}
	}
	
	public static <T extends C2SPacket> void registerC2SPacket(PacketHolder<T> holder) {
		PayloadTypeRegistry.playC2S().register(holder.id(), holder.codec());
		ServerPlayNetworking.registerGlobalReceiver(holder.id(), (packet, ctx) ->
				ctx.server().execute(() -> {
					if (ctx.player() != null) packet.handle(ctx.player());
				})
		);
	}
	
	public static <T extends S2CPacket> void registerS2CPacket(PacketHolder<T> holder) {
		PayloadTypeRegistry.playS2C().register(holder.id(), holder.codec());
	}
	
	public static <T extends S2CPacket> void registerS2CPacketReceiver(PacketHolder<T> holder) {
		ClientPlayNetworking.registerGlobalReceiver(holder.id(), (packet, ctx) -> {
			ctx.client().execute(() -> {
				if (ctx.player() != null) packet.handle();
			});
		});
	}
	
}
