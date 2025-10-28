package com.nine.travelerscompass.config.cost;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public record SearchCost(int costLevel, int reqLevel) {
	
	public static SearchCost FREE = new SearchCost(0, 0);
	
	public boolean isFree() {
		return costLevel + reqLevel == 0;
	}
	
	public boolean meetsRequirement(int level) {
		return reqLevel <= level;
	}
	
	public boolean meetsAllConditions(int level) {
		return meetsRequirement(level) && costLevel <= level;
	}
	
	public MutableComponent costAsComponent() {
		String key = "tooltip.travelerscompass.config.search_cost.cost" + (costLevel > 1 ? "_plural" : "");
		return Component.translatable(key, costLevel);
	}
	
	public MutableComponent reqAsComponent() {
		String key = "tooltip.travelerscompass.config.search_cost.requirement_level";
		return Component.translatable(key, reqLevel);
	}
	
}
