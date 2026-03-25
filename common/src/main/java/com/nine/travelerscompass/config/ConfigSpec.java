package com.nine.travelerscompass.config;

import com.nine.travelerscompass.config.option.ConfigComment;
import com.nine.travelerscompass.config.option.ConfigRange;
import com.nine.travelerscompass.config.option.ConfigSection;
import com.nine.travelerscompass.config.option.ConfigSide;

public class ConfigSpec {

    public boolean shouldSync = false;
    public ConfigSide side = ConfigSide.COMMON;
    public LoaderTarget target = LoaderTarget.COMMON;

    public ConfigComment comment = ConfigComment.EMPTY;
    public ConfigSection section = ConfigSection.NONE;
    public boolean hideConstraints = false;

    public ConfigRange<? extends Number> range = null;
}
