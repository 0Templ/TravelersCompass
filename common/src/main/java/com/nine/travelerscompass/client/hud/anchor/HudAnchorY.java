package com.nine.travelerscompass.client.hud.anchor;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum HudAnchorY implements HudAnchor {
	
	TOP, BOTTOM;
	
	
	@Override
	public MutableComponent asComponent() {
		return Component.translatable("tooltip.travelerscompass.settings.anchor." + this.name().toLowerCase());
	}
}
