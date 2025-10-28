package com.nine.travelerscompass.common.search.location;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.util.Optional;

public interface ILocationObject {
	
	BlockPos blockPos();
	
	int slotIndex();
	
	boolean priority();
	
	String descriptionId();
	
	boolean isValid(ServerLevel level);
	
	ResourceLocation type();
	
	ILocationObject copy(
			BlockPos pos,
			int slotIndex,
			boolean priority,
			String descriptionId
	);
	
	default ILocationObject withPriority(boolean value) {
		if (value == priority()) return this;
		return copy(blockPos(), slotIndex(), value, descriptionId());
	}
	
	default ILocationObject withBlockPos(BlockPos pos) {
		if (pos == blockPos()) return this;
		return copy(pos, slotIndex(), priority(), descriptionId());
	}
	
	Optional<ILocationObject> update(ServerLevel level);
	
}
