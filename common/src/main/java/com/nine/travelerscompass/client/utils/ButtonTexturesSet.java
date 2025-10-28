package com.nine.travelerscompass.client.utils;

public record ButtonTexturesSet(TextureData normal, TextureData hovered, TextureData active, TextureData activeHovered,
								TextureData disabled) {
	
	public TextureData get(boolean isActive, boolean isHovered) {
		return get(isActive, isHovered, true);
	}
	
	public TextureData get(boolean isActive, boolean isHovered, boolean isEnabled) {
		if (!isEnabled) return disabled;
		if (isActive && isHovered) return activeHovered;
		if (isActive) return active;
		if (isHovered) return hovered;
		return normal;
	}
	
}
