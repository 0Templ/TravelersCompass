package com.nine.travelerscompass.config;

import com.nine.travelerscompass.platform.Platform;

public class ConfigValue<T> {

    private final T defaultValue;
    private final Section section;
    public final String key;
    public final Class<T> clazz;
    public final ConfigType type;
    private final boolean shouldSync;

    public ConfigValue(String key, T defaultValue, Section section, ConfigType type, Class<T> clazz, boolean shouldSync) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.section = section;
        this.type = type;
        this.clazz = clazz;
        this.shouldSync = shouldSync;
    }

    public Class<T> clazz(){
        return clazz;
    }

    public String path() {
        return section.key + "." + key;
    }

    public T defaultValue() {
        return defaultValue;
    }

    public T get() {
        return Platform.PLATFORM_CONFIG.get(this);
    }

    public boolean shouldSync(){
        return shouldSync;
    }

    public enum Section {
        GENERAL("general"),
        COMPAT("compatibility"),
        BEHAVIOR("behavior"),
        SEARCH_COST("search-cost"),
        FILTERS("search-filters"),
        HUD("hud"),
        ;

        public final String key;

        Section(String key) {
            this.key = key;
        }
    }
}