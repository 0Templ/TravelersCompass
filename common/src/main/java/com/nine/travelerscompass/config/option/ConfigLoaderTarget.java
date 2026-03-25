package com.nine.travelerscompass.config.option;

import com.nine.travelerscompass.config.ConfigSpec;
import com.nine.travelerscompass.config.LoaderTarget;

public record ConfigLoaderTarget(LoaderTarget target) implements ConfigOption {

    public static ConfigLoaderTarget of(LoaderTarget target) {
        return new ConfigLoaderTarget(target);
    }

    @Override
    public void apply(ConfigSpec spec) {
        spec.target = this.target != null ? this.target : LoaderTarget.COMMON;
    }
}
