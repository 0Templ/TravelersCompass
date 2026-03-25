package com.nine.travelerscompass.common.data.codec;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class MoreCodecs {
	
	public static <E extends Enum<E>> Codec<E> enumCodec(Class<E> clazz) {
		return Codec.STRING.xmap(
				name -> Enum.valueOf(clazz, name.toUpperCase()),
				Enum::name
		);
	}
	
	public static <E extends Enum<E>> StreamCodec<FriendlyByteBuf, E> enumStreamCodec(Class<E> clazz) {
		return new StreamCodec<>() {
			
			@Override
			public void encode(FriendlyByteBuf buf, E value) {
				buf.writeVarInt(value.ordinal());
			}
			
			@Override
			public E decode(FriendlyByteBuf buf) {
				int index = buf.readVarInt();
				var values = clazz.getEnumConstants();
				if (index < 0 || values.length <= index) {
					throw new IllegalArgumentException("Wrong index " + index + " for enum class " + clazz.getName());
				}
				return values[index];
			}
		};
	}
	
}
