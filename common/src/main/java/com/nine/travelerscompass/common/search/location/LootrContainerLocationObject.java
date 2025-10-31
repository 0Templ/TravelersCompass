package com.nine.travelerscompass.common.search.location;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.search.location.codec.LocationCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record LootrContainerLocationObject(BlockPos blockPos, int slotIndex, boolean priority, int containerSlotIndex,
										   String descriptionId, String contentId,
										   ResourceLocation blockId) implements ILocationObject, WithContent {
	
	public static ResourceLocation TYPE = ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "lootr_container_location_codec");
	
	public static final LocationCodec<LootrContainerLocationObject> CODEC = new LocationCodec<>() {
		
		@Override
		public void write(FriendlyByteBuf buf, LootrContainerLocationObject obj) {
			this.writePosSafe(buf, obj.blockPos);
			buf.writeInt(obj.slotIndex);
			buf.writeBoolean(obj.priority);
			buf.writeInt(obj.containerSlotIndex);
			buf.writeUtf(obj.descriptionId);
			buf.writeUtf(obj.contentId);
			buf.writeResourceLocation(obj.blockId);
		}
		
		@Override
		public LootrContainerLocationObject read(FriendlyByteBuf buf) {
			BlockPos blockPos = this.readPosSafe(buf);
			int slotIndex = buf.readInt();
			boolean priority = buf.readBoolean();
			int containerSlotIndex = buf.readInt();
			String descriptionId = buf.readUtf();
			String containerId = buf.readUtf();
			ResourceLocation blockId = buf.readResourceLocation();
			return new LootrContainerLocationObject(blockPos, slotIndex, priority, containerSlotIndex, descriptionId, containerId, blockId);
		}
	};
	
	@Override
	public ResourceLocation type() {
		return TYPE;
	}
	
	@Override
	public boolean isValid(ServerLevel level) {
		boolean loaded = level.isLoaded(blockPos);
		if (!loaded) {
			return true;
		}
		Block block = level.getBlockState(blockPos).getBlock();
		var written = BuiltInRegistries.BLOCK.get(blockId);
		return written.isPresent() && written.get().value().equals(block);
	}
	
	@Override
	public ILocationObject copy(
			BlockPos blockPos,
			int slotIndex,
			boolean priority,
			String descriptionId
	) {
		return new LootrContainerLocationObject(blockPos, slotIndex, priority, containerSlotIndex, descriptionId, contentId, blockId);
	}
	
	@Override
	public Optional<ILocationObject> update(ServerLevel level) {
		return Optional.of(this);
	}
	
	@Override
	public String toString() {
		return "Lootr Chest Container [" + descriptionId + ": " + contentId + "], " + blockPos + (priority ? " [priority]" : "");
	}
	
}
