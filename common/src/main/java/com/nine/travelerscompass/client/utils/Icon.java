package com.nine.travelerscompass.client.utils;

import java.util.List;

public record Icon(List<IconLayer> layers) {
	
	public Icon(IconLayer... layers) {
		this(List.of(layers));
	}
	
	public Icon(IconLayer layer) {
		this(List.of(layer));
	}
	
	public static Icon of(TextureData data, float xOffset, float yOffset, float scale) {
		return new Icon(new IconLayer(data, xOffset, yOffset, scale));
	}
	
	public static Icon of(TextureData data, float xOffset, float yOffset) {
		return new Icon(new IconLayer(data, xOffset, yOffset));
	}
	
	public static Icon of(TextureData data) {
		return new Icon(new IconLayer(data));
	}
	
}
