package com.nine.travelerscompass.config.option;

import com.nine.travelerscompass.config.ConfigSpec;

public enum ConfigFlag implements ConfigOption {

    SYNC {
        @Override
        public void apply(ConfigSpec spec) {
            spec.shouldSync = true;
        }
    },
    HIDE_CONSTRAINTS {
        @Override
        public void apply(ConfigSpec spec) {
            spec.hideConstraints = true;
        }
    }
}
