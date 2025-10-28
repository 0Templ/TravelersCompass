package com.nine.travelerscompass.client.hud;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Each hud type has separate width and height parameters, so store them in an array
 */
public record HudSize(List<Integer> values) {
	
	public static final Codec<HudSize> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
							Codec.INT.listOf().fieldOf("values").forGetter(HudSize::values))
					.apply(instance, HudSize::new));
	
	public static final StreamCodec<ByteBuf, HudSize> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT.apply(ByteBufCodecs.list()), HudSize::values,
			HudSize::new);
	
	public HudSize() {
		this(defaultValues());
	}
	
	private static List<Integer> defaultValues() {
		return Arrays.stream(HudType.values())
				.flatMap(type -> Stream.of(type.defaultWidth(), type.defaultHeight()))
				.toList();
	}
	
	public HudSize withWidth(HudType type, int value) {
		var ret = new ArrayList<>(values);
		ret.set((type.ordinal() * 2), value);
		return new HudSize(ret);
	}
	
	public HudSize withHeight(HudType type, int value) {
		var ret = new ArrayList<>(values);
		ret.set((type.ordinal() * 2) + 1, value);
		return new HudSize(ret);
	}
	
	public int getWidth(HudType type) {
		return values.get(type.ordinal() * 2);
	}
	
	public int getHeight(HudType type) {
		return values.get((type.ordinal() * 2) + 1);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof HudSize other)) return false;
		return values.equals(other.values);
	}
	
}
