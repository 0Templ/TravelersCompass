package com.nine.travelerscompass.client.utils;

public record IconLayer(TextureData textureData, float xOffset, float yOffset, float scale) {
	
	public IconLayer(TextureData data) {
		this(data, 0, 0);
	}
	
	public IconLayer(TextureData data, float xOffset, float yOffset) {
		this(data, xOffset, yOffset, 1.0f);
	}
	
	public static IconLayer withProperties(IconLayer layer, TextureData data) {
		return new IconLayer(data, layer.xOffset, layer.yOffset, layer.scale);
	}
	
}
