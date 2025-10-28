package com.nine.travelerscompass.common.search.matcher;

import com.nine.travelerscompass.common.search.SearchOptions;
import com.nine.travelerscompass.common.search.criterion.TypedCriteria;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockMatcher {
	
	ILocationObject match(TypedCriteria criteria, BlockPos pos, BlockState state);
	
	boolean isAllowed(SearchOptions options);
	
}
