package com.nine.travelerscompass.common.search.location.codec;

import com.nine.travelerscompass.common.search.location.ILocationObject;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public interface LocationCodec<T extends ILocationObject> {
	
	void write(FriendlyByteBuf buf, T obj);
	
	T read(FriendlyByteBuf buf);
	
	default void writePosSafe(FriendlyByteBuf buf, BlockPos pos) {
		boolean valid = pos != null;
		buf.writeBoolean(valid);
		if (valid) {
			buf.writeBlockPos(pos);
		}
	}
	
	default BlockPos readPosSafe(FriendlyByteBuf buf) {
		boolean valid = buf.readBoolean();
		return valid ? buf.readBlockPos() : null;
	}
	
}
