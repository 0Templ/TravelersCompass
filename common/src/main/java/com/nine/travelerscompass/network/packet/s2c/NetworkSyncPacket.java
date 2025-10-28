package com.nine.travelerscompass.network.packet.s2c;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.data.DataStorage;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;


/**
 * Synchronizes DataStorage network IDs between server and client.
 * Ensures both sides use matching [id] -> [DataStorage] mappings.
 */
public record NetworkSyncPacket(Map<Byte, String> map) implements S2CPacket {
	
	public static final Type<NetworkSyncPacket> ID =
			new Type<>(ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "network_sync_packet"));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, NetworkSyncPacket> CODEC =
			StreamCodec.ofMember(NetworkSyncPacket::encode, NetworkSyncPacket::decode);
	
	public void encode(RegistryFriendlyByteBuf buf) {
		buf.writeVarInt(map.size());
		for (var el : map.entrySet()) {
			buf.writeByte(el.getKey());
			buf.writeUtf(el.getValue());
		}
	}
	
	public static NetworkSyncPacket decode(RegistryFriendlyByteBuf buf) {
		Map<Byte, String> map = new HashMap<>();
		int size = buf.readVarInt();
		for (int i = 0; i < size; i++) {
			map.put(buf.readByte(), buf.readUtf());
		}
		return new NetworkSyncPacket(map);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
	@Override
	public void handle() {
		for (var entry : map.entrySet()) {
			DataStorage<?> current = CompassProperties.NETWORK_REGISTRY.get(entry.getKey());
			DataStorage<?> correct = CompassProperties.REGISTRY.get(entry.getValue());
			
			if (current != null && correct != null && !current.id().equals(entry.getValue())) {
				CompassProperties.NETWORK_REGISTRY.put(entry.getKey(), correct);
			}
		}
	}
	
}