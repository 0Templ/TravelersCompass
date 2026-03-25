package com.nine.travelerscompass.network.packet.s2c;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.client.ClientCache;
import com.nine.travelerscompass.client.utils.SearchProgress;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.UUID;

public record SearchProgressSyncPacket(SearchProgress searchProgress, UUID uuid) implements S2CPacket {
	
	public static final Type<SearchProgressSyncPacket> ID =
			new Type<>(Identifier.fromNamespaceAndPath(TCCommon.MODID, "search_progress_packet"));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, SearchProgressSyncPacket> CODEC =
			StreamCodec.ofMember(SearchProgressSyncPacket::encode, SearchProgressSyncPacket::decode);
	
	public void encode(RegistryFriendlyByteBuf buf) {
		buf.writeVarInt(searchProgress.progress);
		buf.writeVarInt(searchProgress.total);
		buf.writeUUID(uuid);
	}
	
	public static SearchProgressSyncPacket decode(RegistryFriendlyByteBuf buf) {
		var searchProgress = new SearchProgress(buf.readVarInt(), buf.readVarInt());
		return new SearchProgressSyncPacket(searchProgress, buf.readUUID());
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
	@Override
	public void handle() {
		if (searchProgress.progress < 0) {
			ClientCache.PROGRESS_DATA_CACHE.remove(uuid);
		} else {
			ClientCache.PROGRESS_DATA_CACHE.put(uuid, searchProgress);
		}
	}
	
}