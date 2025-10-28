package com.nine.travelerscompass.common.search.location;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.search.location.codec.LocationCodec;
import com.nine.travelerscompass.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;
import java.util.Optional;

public record ContainerLocationObject(BlockPos blockPos, int slotIndex, boolean priority, int containerSlotIndex,
									  String descriptionId, String contentId,
									  ResourceLocation blockId) implements ILocationObject, WithContent {
	
	public static ResourceLocation TYPE = ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "container_location_codec");
	
	public static final LocationCodec<ContainerLocationObject> CODEC = new LocationCodec<>() {
		
		@Override
		public void write(FriendlyByteBuf buf, ContainerLocationObject obj) {
			this.writePosSafe(buf, obj.blockPos);
			buf.writeInt(obj.slotIndex);
			buf.writeBoolean(obj.priority);
			buf.writeInt(obj.containerSlotIndex);
			buf.writeUtf(obj.descriptionId);
			buf.writeUtf(obj.contentId);
			buf.writeResourceLocation(obj.blockId);
		}
		
		@Override
		public ContainerLocationObject read(FriendlyByteBuf buf) {
			BlockPos blockPos = this.readPosSafe(buf);
			int slotIndex = buf.readInt();
			boolean priority = buf.readBoolean();
			int containerSlotIndex = buf.readInt();
			String descriptionId = buf.readUtf();
			String containerId = buf.readUtf();
			ResourceLocation blockId = buf.readResourceLocation();
			return new ContainerLocationObject(blockPos, slotIndex, priority, containerSlotIndex, descriptionId, containerId, blockId);
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
		boolean sameBlock = BuiltInRegistries.BLOCK.get(blockId).equals(block);
		if (sameBlock) {
			BlockEntity blockEntity = level.getBlockEntity(blockPos);
			if (blockEntity != null) {
				Item item = Platform.PLATFORM.getContainerItemByIndex(containerSlotIndex, blockEntity);
				return Objects.equals(item.getDescriptionId(), contentId);
			}
		}
		return sameBlock;
	}
	
	@Override
	public ILocationObject copy(
			BlockPos blockPos,
			int slotIndex,
			boolean priority,
			String descriptionId
	) {
		return new ContainerLocationObject(blockPos, slotIndex, priority, containerSlotIndex, descriptionId, contentId, blockId);
	}
	
	
	@Override
	public Optional<ILocationObject> update(ServerLevel level) {
		return Optional.of(this);
	}
	
	@Override
	public String toString() {
		return "Block Container Entity [" + descriptionId + ": " + contentId + "], " + blockPos + (priority ? " [priority]" : "");
	}
	
}
