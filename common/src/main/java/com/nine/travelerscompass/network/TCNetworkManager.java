package com.nine.travelerscompass.network;

import com.nine.travelerscompass.network.packet.PacketHolder;
import com.nine.travelerscompass.network.packet.c2s.*;
import com.nine.travelerscompass.network.packet.s2c.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.ArrayList;
import java.util.List;

public class TCNetworkManager {
	
	public static final List<PacketHolder<? extends C2SPacket>> C2S_PACKETS = new ArrayList<>();
	
	public static final List<PacketHolder<? extends S2CPacket>> S2C_PACKETS = new ArrayList<>();
	
	
	public static void init() {
		addC2SPacket(CompassDataPacket.class, CompassDataPacket.ID, CompassDataPacket.PACKET_CODEC);
		addC2SPacket(GhostTargetPacket.class, GhostTargetPacket.ID, GhostTargetPacket.PACKET_CODEC);
		addC2SPacket(WideSearchPacket.class, WideSearchPacket.ID, WideSearchPacket.PACKET_CODEC);
		addC2SPacket(PausePacket.class, PausePacket.ID, PausePacket.PACKET_CODEC);
		
		
		addS2CPacket(ConfigSyncPacket.class, ConfigSyncPacket.ID, ConfigSyncPacket.CODEC);
		addS2CPacket(NetworkSyncPacket.class, NetworkSyncPacket.ID, NetworkSyncPacket.CODEC);
		addS2CPacket(CompletePacket.class, CompletePacket.ID, CompletePacket.CODEC);
		addS2CPacket(SearchProgressSyncPacket.class, SearchProgressSyncPacket.ID, SearchProgressSyncPacket.CODEC);
		addS2CPacket(HudDataPacket.class, HudDataPacket.ID, HudDataPacket.CODEC);
	}
	
	public static <T extends C2SPacket> void addC2SPacket(Class<T> clazz, CustomPacketPayload.Type<T> id, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		C2S_PACKETS.add(new PacketHolder<>(clazz, id, codec));
	}
	
	public static <T extends S2CPacket> void addS2CPacket(Class<T> clazz, CustomPacketPayload.Type<T> id, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
		S2C_PACKETS.add(new PacketHolder<>(clazz, id, codec));
	}
	
	
}
