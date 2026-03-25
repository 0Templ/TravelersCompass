package com.nine.travelerscompass.common.search.location;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.search.location.codec.LocationCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record SpawnerLocationObject(BlockPos blockPos, int slotIndex, boolean priority, String descriptionId,
									String contentId,
									Identifier blockId) implements ILocationObject, WithContent {
	
	public static Identifier TYPE = Identifier.fromNamespaceAndPath(TCCommon.MODID, "spawner_location_codec");
	
	public static final LocationCodec<SpawnerLocationObject> CODEC = new LocationCodec<>() {
		
		@Override
		public void write(FriendlyByteBuf buf, SpawnerLocationObject obj) {
			this.writePosSafe(buf, obj.blockPos);
			buf.writeInt(obj.slotIndex);
			buf.writeBoolean(obj.priority);
			buf.writeUtf(obj.descriptionId);
			buf.writeUtf(obj.contentId);
			buf.writeIdentifier(obj.blockId);
		}
		
		@Override
		public SpawnerLocationObject read(FriendlyByteBuf buf) {
			BlockPos blockPos = this.readPosSafe(buf);
			int slotIndex = buf.readInt();
			boolean priority = buf.readBoolean();
			String descriptionId = buf.readUtf();
			String containerId = buf.readUtf();
			Identifier blockId = buf.readIdentifier();
			return new SpawnerLocationObject(blockPos, slotIndex, priority, descriptionId, containerId, blockId);
		}
	};
	
	@Override
	public Identifier type() {
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
	public ILocationObject withPriority(boolean value) {
		if (value == priority) return this;
		return new SpawnerLocationObject(blockPos, slotIndex, value, descriptionId, contentId, blockId);
	}

	@Override
	public ILocationObject withBlockPos(BlockPos pos) {
		if (pos.equals(blockPos)) return this;
		return new SpawnerLocationObject(pos, slotIndex, priority, descriptionId, contentId, blockId);
	}
	
	@Override
	public Optional<ILocationObject> update(ServerLevel level) {
		return Optional.of(this);
	}
	
	
	@Override
	public String toString() {
		return "Spawner Location: [" + descriptionId + "--" + contentId + blockPos + (priority ? " [priority]" : "");
	}
	
}
