package com.nine.travelerscompass.common.search.location;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.search.location.codec.LocationCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record BlockLocationObject(BlockPos blockPos, int slotIndex, boolean priority, String descriptionId,
								  Identifier blockId) implements ILocationObject {
	
	public static Identifier TYPE = Identifier.fromNamespaceAndPath(TCCommon.MODID, "block_location_codec");
	
	public static final LocationCodec<BlockLocationObject> CODEC = new LocationCodec<>() {
		
		@Override
		public void write(FriendlyByteBuf buf, BlockLocationObject obj) {
			this.writePosSafe(buf, obj.blockPos);
			buf.writeInt(obj.slotIndex);
			buf.writeBoolean(obj.priority);
			buf.writeUtf(obj.descriptionId);
			buf.writeIdentifier(obj.blockId);
		}
		
		@Override
		public BlockLocationObject read(FriendlyByteBuf buf) {
			BlockPos blockPos = this.readPosSafe(buf);
			int slotIndex = buf.readInt();
			boolean priority = buf.readBoolean();
			String descriptionId = buf.readUtf();
			Identifier blockId = buf.readIdentifier();
			return new BlockLocationObject(blockPos, slotIndex, priority, descriptionId, blockId);
		}
	};
	
	@Override
	public Identifier type() {
		return TYPE;
	}
	
	@Override
	public ILocationObject withPriority(boolean value) {
		if (value == priority) return this;
		return new BlockLocationObject(blockPos, slotIndex, value, descriptionId, blockId);
	}

	@Override
	public ILocationObject withBlockPos(BlockPos pos) {
		if (pos.equals(blockPos)) return this;
		return new BlockLocationObject(pos, slotIndex, priority, descriptionId, blockId);
	}
	
	@Override
	public boolean isValid(ServerLevel level) {
		if (!level.isLoaded(blockPos)) return true;
		Block block = level.getBlockState(blockPos).getBlock();
		return BuiltInRegistries.BLOCK
				.get(blockId)
				.map(Holder.Reference::value)
				.map(refBlock -> refBlock == block)
				.orElse(false);
	}
	
	@Override
	public Optional<ILocationObject> update(ServerLevel level) {
		return Optional.of(this);
	}
	
	@Override
	public String toString() {
		return "Block Location: [" + descriptionId + "], " + blockPos + (priority ? " [priority]" : "");
	}
	
}
