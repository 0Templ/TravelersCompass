package com.nine.travelerscompass.network.packet.c2s;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.search.SearchManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;
import java.util.UUID;

public record WideSearchPacket(UUID uuid) implements C2SPacket {
	
	public static final Type<WideSearchPacket> ID = new Type<>(
			ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "wide_search_packet"));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, WideSearchPacket> PACKET_CODEC = StreamCodec.ofMember(
			WideSearchPacket::encode, WideSearchPacket::decode);
	
	public void encode(RegistryFriendlyByteBuf buf) {
		buf.writeUUID(uuid);
	}
	
	public static WideSearchPacket decode(RegistryFriendlyByteBuf buf) {
		return new WideSearchPacket(buf.readUUID());
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
	@Override
	public void handle(ServerPlayer player) {
		ItemStack stack = player.getMainHandItem();
		if (stack.getItem() instanceof TravelersCompassItem &&
				Objects.equals(CompassComponents.COMPASS_UUID.get(stack), uuid)) {
			SearchManager.startWideSearch(stack, player, CompassContainer.container(stack));
		}
	}
	
}

