package com.nine.travelerscompass.common.search.criterion;

import net.minecraft.world.item.ItemStack;

import java.util.List;

@FunctionalInterface
public interface CriterionExtractor {

    List<ISearchCriterion> extract(ItemStack stack, boolean priority, int slot);

}
