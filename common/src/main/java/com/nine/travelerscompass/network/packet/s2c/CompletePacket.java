package com.nine.travelerscompass.network.packet.s2c;

import com.nine.travelerscompass.TCCommon;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record CompletePacket() implements S2CPacket {
	
	public static final Type<CompletePacket> ID =
			new Type<>(Identifier.fromNamespaceAndPath(TCCommon.MODID, "complete_packet"));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, CompletePacket> CODEC =
			StreamCodec.ofMember(CompletePacket::encode, CompletePacket::decode);
	
	public void encode(RegistryFriendlyByteBuf buf) {
	}
	
	public static CompletePacket decode(RegistryFriendlyByteBuf buf) {
		return new CompletePacket();
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
	@Override
	public void handle() {
		TCCommon.updateCache();
	}
	
}