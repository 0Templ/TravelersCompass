package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.common.search.matcher.BlockEntityMatcher;
import com.nine.travelerscompass.common.search.matcher.EntityMatcher;

public interface IPlatformMatchersHelper {

    BlockEntityMatcher containerMatcher();

    BlockEntityMatcher lootrContainerMatcher();

    EntityMatcher lootrMinecartMatcher();

}
