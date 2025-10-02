package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.config.*;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = TCCommon.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgePlatformConfigHelper extends IPlatformConfigHelper {

    public static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.Builder COST_BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec COST_CONFIG;

    static {
        TCConfig.init();
        ForgeTCConfig.init();
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
        ForgeConfigSpec.ConfigValue<T> spec;
        ForgeConfigSpec.Builder builder = getBuilder(type);
        if (defaultValue instanceof Boolean){
            spec = (ForgeConfigSpec.ConfigValue<T>) builder.comment(comment).define(fullPath, (Boolean) defaultValue);
        } else if (defaultValue instanceof Integer) {
            spec = (ForgeConfigSpec.ConfigValue<T>) builder.comment(comment).defineInRange(fullPath, (Integer) defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
        } else if (defaultValue instanceof Double) {
            spec = (ForgeConfigSpec.ConfigValue<T>) builder.comment(comment).defineInRange(fullPath, (Double) defaultValue, -Double.MAX_VALUE, Double.MAX_VALUE);
        } else if (defaultValue instanceof String) {
            spec = (ForgeConfigSpec.ConfigValue<T>) builder.comment(comment).define(fullPath, (String) defaultValue);
        } else if (defaultValue instanceof Enum<?>) {
            spec = (ForgeConfigSpec.ConfigValue<T>) builder.comment(comment).defineEnum(fullPath, (Enum) defaultValue);
        } else if (defaultValue instanceof List<?>) {
            spec = (ForgeConfigSpec.ConfigValue<T>) builder.comment(comment)
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

        private final ForgeConfigSpec.ConfigValue<T> forgeValue;

        public ForgeConfigValue(String key, T defaultValue, Section section, ConfigType type,
                                Class<T> clazz, boolean shouldSync, ForgeConfigSpec.ConfigValue<T> forgeValue) {
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

    private ForgeConfigSpec.Builder getBuilder(ConfigType type){
        return switch (type){
            case COST -> COST_BUILDER;
            case COMMON -> COMMON_BUILDER;
        };
    }
}
