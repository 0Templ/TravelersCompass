package com.nine.travelerscompass.common.search.matcher;

import com.nine.travelerscompass.common.search.SearchOptions;
import com.nine.travelerscompass.common.search.criterion.TypedCriteria;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface BlockMatcher {

	List<ILocationObject> match(TypedCriteria criteria, BlockPos pos, BlockState state);

	boolean isAllowed(SearchOptions options);

}
