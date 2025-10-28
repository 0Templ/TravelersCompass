package com.nine.travelerscompass.common.search.matcher;

import com.nine.travelerscompass.common.search.SearchOptions;
import com.nine.travelerscompass.common.search.criterion.TypedCriteria;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.List;

public interface EntityMatcher {
	
	List<ILocationObject> match(TypedCriteria criteria, SearchOptions options, Entity entity, Level level, BlockPos pos);
	
	boolean isAllowed(SearchOptions options);
	
}
