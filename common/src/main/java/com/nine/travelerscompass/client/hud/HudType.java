package com.nine.travelerscompass.client.hud;

import com.nine.travelerscompass.client.hud.type.CompactHudTypeRenderer;
import com.nine.travelerscompass.client.hud.type.ExtendedHudTypeRenderer;
import com.nine.travelerscompass.client.hud.type.IHudTypeRenderer;

public enum HudType {
	
	COMPACT(80, 40, 280, 200, 180, 60, new CompactHudTypeRenderer()),
	EXTENDED(80, 80, 320, 200, 140, 100, new ExtendedHudTypeRenderer());
	
	private final int minWidth, minHeight;
	private final int maxWidth, maxHeight;
	private final int defaultWidth, defaultHeight;
	public final IHudTypeRenderer renderer;
	
	HudType(int minWidth, int minHeight, int maxWidth, int maxHeight, int defaultWidth, int defaultHeight, IHudTypeRenderer renderer) {
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.defaultWidth = defaultWidth;
		this.defaultHeight = defaultHeight;
		this.renderer = renderer;
	}
	
	public int minWidth() {
		return minWidth;
	}
	
	public int minHeight() {
		return minHeight;
	}
	
	public int maxWidth() {
		return maxWidth;
	}
	
	public int maxHeight() {
		return maxHeight;
	}
	
	public int defaultWidth() {
		return defaultWidth;
	}
	
	public int defaultHeight() {
		return defaultHeight;
	}
	
}