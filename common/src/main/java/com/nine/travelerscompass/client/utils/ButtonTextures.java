package com.nine.travelerscompass.client.utils;

public class ButtonTextures {

    public final TextureData normal;
    public final TextureData hovered;
    public final TextureData active;
    public final TextureData activeHovered;
    public final TextureData disabled;

    public ButtonTextures(TextureData normal, TextureData hovered, TextureData active, TextureData activeHovered, TextureData disabled) {
        this.normal = normal;
        this.hovered = hovered;
        this.active = active;
        this.activeHovered = activeHovered;
        this.disabled = disabled;
    }

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
