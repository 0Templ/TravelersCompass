package com.nine.travelerscompass.config.option;

import com.nine.travelerscompass.config.ConfigSpec;

public enum ConfigSide implements ConfigOption {

    CLIENT,
    COMMON;

    @Override
    public void apply(ConfigSpec spec) {
        spec.side = this;
    }
}
