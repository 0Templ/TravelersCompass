package com.nine.travelerscompass.client.hud.anchor;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum HudAnchorX implements HudAnchor {
	
	LEFT, RIGHT;
	
	
	@Override
	public MutableComponent asComponent() {
		return Component.translatable("tooltip.travelerscompass.settings.anchor." + this.name().toLowerCase());
	}
}
