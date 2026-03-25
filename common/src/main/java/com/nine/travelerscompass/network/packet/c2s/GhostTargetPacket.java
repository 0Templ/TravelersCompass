package com.nine.travelerscompass.network.packet.c2s;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.container.menu.CompassMenu;
import com.nine.travelerscompass.compat.NEI;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record GhostTargetPacket(int slotIndex, ItemStack stack, NEI nei) implements C2SPacket {
	
	public static final Type<GhostTargetPacket> ID = new Type<>(
			Identifier.fromNamespaceAndPath(TCCommon.MODID, "ghost_target_packet"));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, GhostTargetPacket> PACKET_CODEC = StreamCodec.ofMember(
			GhostTargetPacket::encode, GhostTargetPacket::decode);
	
	public void encode(RegistryFriendlyByteBuf buf) {
		buf.writeVarInt(slotIndex);
		ItemStack.STREAM_CODEC.encode(buf, stack);
		buf.writeEnum(nei);
		
	}
	
	public static GhostTargetPacket decode(RegistryFriendlyByteBuf buf) {
		int slotIndex = buf.readVarInt();
		ItemStack stack = ItemStack.STREAM_CODEC.decode(buf);
		NEI nei = buf.readEnum(NEI.class);
		return new GhostTargetPacket(slotIndex, stack, nei);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
	
	@Override
	public void handle(ServerPlayer player) {
		if (player.containerMenu instanceof CompassMenu menu) {
			if (nei.allowed()) {
				if (menu.slots.get(slotIndex).container instanceof CompassContainer container) {
					container.setItem(slotIndex, stack, player);
				}
			}
		}
	}
	
}

