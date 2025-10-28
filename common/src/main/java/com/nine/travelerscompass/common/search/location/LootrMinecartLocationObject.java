package com.nine.travelerscompass.common.search.location;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.search.location.codec.LocationCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import java.util.Optional;
import java.util.UUID;

public record LootrMinecartLocationObject(BlockPos blockPos, int slotIndex, boolean priority, String descriptionId,
										  String contentId,
										  UUID uuid) implements ILocationObject, WithContent, WithUUID {
	
	public static ResourceLocation TYPE = ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "lootr_minecart_location_codec");
	
	public static final LocationCodec<LootrMinecartLocationObject> CODEC = new LocationCodec<>() {
		
		@Override
		public void write(FriendlyByteBuf buf, LootrMinecartLocationObject obj) {
			this.writePosSafe(buf, obj.blockPos);
			buf.writeInt(obj.slotIndex);
			buf.writeBoolean(obj.priority);
			buf.writeUtf(obj.descriptionId);
			buf.writeUtf(obj.contentId);
			buf.writeUUID(obj.uuid);
		}
		
		@Override
		public LootrMinecartLocationObject read(FriendlyByteBuf buf) {
			BlockPos blockPos = this.readPosSafe(buf);
			int slotIndex = buf.readInt();
			boolean priority = buf.readBoolean();
			String descriptionId = buf.readUtf();
			String contentId = buf.readUtf();
			UUID uuid = buf.readUUID();
			return new LootrMinecartLocationObject(blockPos, slotIndex, priority, descriptionId, contentId, uuid);
		}
	};
	
	@Override
	public ResourceLocation type() {
		return TYPE;
	}
	
	//todo update
	@Override
	public boolean isValid(ServerLevel level) {
		return level.getEntity(uuid) != null;
	}
	
	@Override
	public ILocationObject copy(
			BlockPos blockPos,
			int slotIndex,
			boolean priority,
			String descriptionId
	) {
		return new LootrMinecartLocationObject(blockPos, slotIndex, priority, descriptionId, contentId, uuid);
	}
	
	@Override
	public Optional<ILocationObject> update(ServerLevel level) {
		Entity entity = level.getEntity(uuid);
		if (entity != null && !entity.blockPosition().equals(blockPos)) {
			return Optional.of(new LootrMinecartLocationObject(
					entity.blockPosition(), slotIndex, priority, descriptionId, contentId, uuid));
		}
		return Optional.of(this);
	}
	
	@Override
	public String toString() {
		return "Lootr Container Entity [" + descriptionId + ": " + contentId + "], " + blockPos + (priority ? " [priority]" : "");
	}
	
}
