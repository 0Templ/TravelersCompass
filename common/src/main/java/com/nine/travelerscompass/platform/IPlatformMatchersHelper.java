package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.common.search.matcher.BlockEntityMatcher;
import com.nine.travelerscompass.common.search.matcher.BlockMatcher;
import com.nine.travelerscompass.common.search.matcher.EntityMatcher;

import java.util.List;

public interface IPlatformMatchersHelper {

    List<BlockEntityMatcher> blockEntityMatchers();

    List<BlockMatcher> blockMatchers();

    List<EntityMatcher> entityMatchers();


}
