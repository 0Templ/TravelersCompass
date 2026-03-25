package com.nine.travelerscompass.common.search.location.codec;

import com.nine.travelerscompass.common.search.location.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public class LocationCodecs {
	
	private static final Map<Identifier, LocationCodec<?>> CODECS = new HashMap<>();
	
	static {
		register(EntityLocationObject.TYPE, EntityLocationObject.CODEC);
		register(BlockLocationObject.TYPE, BlockLocationObject.CODEC);
		register(ContainerLocationObject.TYPE, ContainerLocationObject.CODEC);
		register(ContainerEntityLocationObject.TYPE, ContainerEntityLocationObject.CODEC);
		register(SpawnerLocationObject.TYPE, SpawnerLocationObject.CODEC);
		
		register(LootrContainerLocationObject.TYPE, LootrContainerLocationObject.CODEC);
		register(LootrMinecartLocationObject.TYPE, LootrMinecartLocationObject.CODEC);
	}
	
	private static void register(Identifier id, LocationCodec<?> codec) {
		CODECS.put(id, codec);
	}
	
	@SuppressWarnings("unchecked")
	public static void encode(FriendlyByteBuf buf, ILocationObject object) {
		Identifier typeId = object.type();
		buf.writeIdentifier(typeId);
		LocationCodec<ILocationObject> codec = (LocationCodec<ILocationObject>) CODECS.get(typeId);
		codec.write(buf, object);
	}
	
	public static ILocationObject read(FriendlyByteBuf buf) {
		Identifier type = buf.readIdentifier();
		LocationCodec<?> codec = CODECS.get(type);
		if (codec == null) {
			throw new IllegalArgumentException("No such codec: " + type);
		}
		return codec.read(buf);
	}
	
}
