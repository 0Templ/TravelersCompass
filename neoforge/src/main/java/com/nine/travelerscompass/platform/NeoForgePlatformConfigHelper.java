package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.config.*;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class NeoForgePlatformConfigHelper extends IPlatformConfigHelper {

    public static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.Builder COST_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec COMMON_CONFIG;
    public static final ModConfigSpec COST_CONFIG;

    static {
        TCConfig.init();
        NeoForgeTCConfig.init();
        COMMON_CONFIG = COMMON_BUILDER.build();
        COST_CONFIG = COST_BUILDER.build();
    }

    @Override
    public ConfigValue<Integer> registerInt(String key, int defaultValue, int min, int max, ConfigValue.Section section,
                                            ConfigType type, boolean shouldSync, String comment) {
        String path = section.key + "." + key;
        var spec = getBuilder(type).comment(comment).defineInRange(path, defaultValue, min, max);
        var ret = new ForgeConfigValue<>(key, defaultValue, section, type, Integer.class, shouldSync, spec);
        ConfigSyncManager.checkSyncable(ret);
        return ret;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ConfigValue<T> register(String key, T defaultValue, ConfigValue.Section section, ConfigType type, Class<T> clazz, boolean shouldSync, String comment) {
        String fullPath = section.key + "." + key;
        ModConfigSpec.ConfigValue<T> spec;
        ModConfigSpec.Builder builder = getBuilder(type);
        if (defaultValue instanceof Boolean){
            spec = (ModConfigSpec.ConfigValue<T>) builder.comment(comment).define(fullPath, (Boolean) defaultValue);
        } else if (defaultValue instanceof Integer) {
            spec = (ModConfigSpec.ConfigValue<T>) builder.comment(comment).defineInRange(fullPath, (Integer) defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
        } else if (defaultValue instanceof Double) {
            spec = (ModConfigSpec.ConfigValue<T>) builder.comment(comment).defineInRange(fullPath, (Double) defaultValue, -Double.MAX_VALUE, Double.MAX_VALUE);
        } else if (defaultValue instanceof String) {
            spec = (ModConfigSpec.ConfigValue<T>) builder.comment(comment).define(fullPath, (String) defaultValue);
        } else if (defaultValue instanceof Enum<?>) {
            spec = (ModConfigSpec.ConfigValue<T>) builder.comment(comment).defineEnum(fullPath, (Enum) defaultValue);
        } else if (defaultValue instanceof List<?>) {
            spec = (ModConfigSpec.ConfigValue<T>) builder.comment(comment)
                    .defineList(fullPath, new ArrayList<>((List<?>) defaultValue), el -> el instanceof String);
        }
        else {
            throw new UnsupportedOperationException("Unsupported config type: " + defaultValue.getClass().getName());
        }
        var ret = new ForgeConfigValue<>(key, defaultValue, section, type, clazz, shouldSync, spec);
        ConfigSyncManager.checkSyncable(ret);
        return ret;
    }

    public static class ForgeConfigValue<T> extends ConfigValue<T> {

        private final ModConfigSpec.ConfigValue<T> forgeValue;

        public ForgeConfigValue(String key, T defaultValue, Section section, ConfigType type,
                                Class<T> clazz, boolean shouldSync, ModConfigSpec.ConfigValue<T> forgeValue) {
            super(key, defaultValue, section, type, clazz, shouldSync);
            this.forgeValue = forgeValue;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T get() {
            if (ConfigSyncManager.SYNCED_VALUES.containsKey(path())){
                return (T) ConfigSyncManager.SYNCED_VALUES.get(path());
            }
            return forgeValue.get();
        }
    }

    private ModConfigSpec.Builder getBuilder(ConfigType type){
        return switch (type){
            case COST -> COST_BUILDER;
            case COMMON -> COMMON_BUILDER;
        };
    }
}
