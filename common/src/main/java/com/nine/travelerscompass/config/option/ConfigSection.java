package com.nine.travelerscompass.config.option;

import com.nine.travelerscompass.config.ConfigSpec;

public enum ConfigSection implements ConfigOption {

    NONE(""),
    GENERAL("general"),
    BEHAVIOR("behavior"),
    SEARCH_COST("search-cost"),
    FILTERS("search-filters"),
    HUD("hud"),
    COMPAT("compatibility"),

    ;

    @Override
    public void apply(ConfigSpec spec) {
        spec.section = this;
    }

    public final String key;
    public final ConfigSection parent;

    ConfigSection(String key) {
        this(null, key);
    }

    ConfigSection(ConfigSection parent, String key) {
        this.parent = parent;
        this.key = key;
    }

    public String getFullKey() {
        return key;
    }

    public String getKey() {
        return key;
    }

    public String getDisplayName() {
        return key;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public ConfigSection getParent() {
        return parent;
    }
}
