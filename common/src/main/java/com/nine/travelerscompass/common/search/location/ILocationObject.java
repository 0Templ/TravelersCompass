package com.nine.travelerscompass.common.search.location;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;

import java.util.Optional;

public interface ILocationObject {

	BlockPos blockPos();

	int slotIndex();

	boolean priority();

	String descriptionId();

	boolean isValid(ServerLevel level);

	Identifier type();

	ILocationObject withPriority(boolean value);

	ILocationObject withBlockPos(BlockPos pos);

	Optional<ILocationObject> update(ServerLevel level);

}
