package com.nine.travelerscompass.common.search.criterion;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public record ItemCriterion(Item item, boolean priority, int slot) implements ISearchCriterion {
	
	public boolean check(Item toCheck) {
		return toCheck.equals(item) && !toCheck.equals(Items.AIR);
	}
	
}
