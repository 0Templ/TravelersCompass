package com.nine.travelerscompass.common.search.criterion;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public record BlockCriterion(Block block, boolean priority, int slot) implements ISearchCriterion, IPlaceableCriterion {
	
	public boolean check(Block check) {
		return check.equals(block) && !check.equals(Blocks.AIR);
	}
	
	@Override
	public Block asBlock() {
		return block;
	}
	
}
