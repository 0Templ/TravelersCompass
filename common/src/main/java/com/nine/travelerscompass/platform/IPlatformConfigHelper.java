package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.config.ConfigType;
import com.nine.travelerscompass.config.ConfigValue;

public abstract class IPlatformConfigHelper {

    public <T> ConfigValue<T> register(String key, T defaultValue, ConfigValue.Section section, ConfigType type, String comment){
        return register(key, defaultValue, section, type, false, comment);
    }

    @SuppressWarnings("unchecked")
    public <T> ConfigValue<T> register(String key, T defaultValue, ConfigValue.Section section, ConfigType type, boolean shouldSync, String comment){
        return register(key, defaultValue, section, type, (Class<T>) defaultValue.getClass(), shouldSync, comment);
    }

    public <T> ConfigValue<T> register(String key, T defaultValue, ConfigValue.Section section, ConfigType type, Class<T> clazz, boolean shouldSync, String comment){
        return null;
    }

    public ConfigValue<Integer> registerInt(String key, int def, int min, int max, ConfigValue.Section section, ConfigType type, boolean shouldSync, String comment){
        return null;
    }

    public <T> T get(ConfigValue<T> value){
        return null;
    }
}
