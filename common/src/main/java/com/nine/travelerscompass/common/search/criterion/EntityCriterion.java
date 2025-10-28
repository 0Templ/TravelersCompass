package com.nine.travelerscompass.common.search.criterion;

import net.minecraft.world.entity.EntityType;

public record EntityCriterion(EntityType<?> type, boolean priority, int slot) implements ISearchCriterion {
	
	public boolean check(EntityType<?> check) {
		return check.equals(type);
	}
	
}