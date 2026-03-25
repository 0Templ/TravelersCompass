package com.nine.travelerscompass.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.nine.travelerscompass.config.option.ConfigRange;
import com.nine.travelerscompass.config.option.ConfigSection;
import com.nine.travelerscompass.config.option.ConfigSide;
import net.minecraft.util.Mth;

public class ConfigValue<T> {

    private final CommentedFileConfig config;
    private final T defaultValue;
    private final ConfigSection section;
    public final String key;
    public final Class<T> clazz;
    public final ConfigSide side;
    public final LoaderTarget target;
    private final boolean shouldSync;
    private final Class<?> elementClass;
    private final ConfigRange<?> range;

    public ConfigValue(
            CommentedFileConfig config,
            String key,
            T defaultValue,
            ConfigRange<?> range,
            ConfigSection section,
            ConfigSide side,
            LoaderTarget target,
            boolean shouldSync,
            Class<T> clazz,
            Class<?> elementClass
    ) {
        this.config = config;
        this.key = key;
        this.defaultValue = defaultValue;
        this.range = range;
        this.section = section;
        this.side = side;
        this.target = target;
        this.shouldSync = shouldSync;
        this.clazz = clazz;
        this.elementClass = elementClass;
    }

    public ConfigValue(
            String key,
            T defaultValue,
            ConfigRange<?> range,
            ConfigSection section,
            ConfigSide side,
            LoaderTarget target,
            boolean shouldSync,
            Class<T> clazz,
            Class<?> elementClass
    ) {
        this(null, key, defaultValue, range, section, side, target, shouldSync, clazz, elementClass);
    }

    public Class<T> clazz() {
        return clazz;
    }

    public String path() {
        String sectionKey = section.getFullKey();
        return sectionKey.isBlank() ? key : sectionKey + "." + key;
    }

    public T defaultValue() {
        return defaultValue;
    }

    @SuppressWarnings("unchecked")
    public T get() {
        Object synced = ConfigSyncManager.SYNCED_VALUES.get(path());
        if (synced != null) {
            return this.cast(synced);
        }

        if (config == null) {
            return defaultValue;
        }

        Object value = config.get(path());
        if (value == null) {
            return defaultValue;
        }

        return this.cast(value);
    }

    @SuppressWarnings("unchecked")
    public T cast(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number number) {
            if (clazz == Double.class) {
                return (T) Double.valueOf(number.doubleValue());
            }
            if (clazz == Float.class) {
                return (T) Float.valueOf(number.floatValue());
            }
            if (clazz == Integer.class) {
                return (T) Integer.valueOf(number.intValue());
            }
            if (clazz == Long.class) {
                return (T) Long.valueOf(number.longValue());
            }
        }

        if (clazz != null && clazz.isEnum() && value instanceof String str) {
            return (T) Enum.valueOf((Class<Enum>) clazz, str);
        }

        if (clazz == Boolean.class && value instanceof Boolean boolValue) {
            return (T) boolValue;
        }

        return (T) value;
    }

    public void set(T value) {
        if (config == null) {
            return;
        }

        Object toSet = this.normalize(value);
        config.set(path(), serialize(toSet));
        config.save();
    }

    public T normalize(T value) {
        return this.cast(tryClamp(value));
    }

    public boolean shouldSync() {
        return shouldSync;
    }

    public boolean isAvailable() {
        return config != null;
    }

    public Number min() {
        return range != null ? range.min() : null;
    }

    public Number max() {
        return range != null ? range.max() : null;
    }

    public Class<?> elementClass() {
        return elementClass;
    }

    private Object serialize(Object value) {
        if (value == null) {
            return null;
        }
        if (clazz != null && clazz.isEnum()) {
            return ((Enum<?>) value).name();
        }
        return value;
    }

    private Object tryClamp(Object input) {
        if (input == null) {
            return null;
        }

        Number min = min();
        Number max = max();
        if (min == null && max == null) {
            return input;
        }

        if (input instanceof Integer intValue) {
            return Mth.clamp(
                    intValue,
                    min != null ? min.intValue() : Integer.MIN_VALUE,
                    max != null ? max.intValue() : Integer.MAX_VALUE
            );
        }
        if (input instanceof Long longValue) {
            return Mth.clamp(
                    longValue,
                    min != null ? min.longValue() : Long.MIN_VALUE,
                    max != null ? max.longValue() : Long.MAX_VALUE
            );
        }
        if (input instanceof Double doubleValue) {
            return Mth.clamp(
                    doubleValue,
                    min != null ? min.doubleValue() : -Double.MAX_VALUE,
                    max != null ? max.doubleValue() : Double.MAX_VALUE
            );
        }
        if (input instanceof Float floatValue) {
            return Mth.clamp(
                    floatValue,
                    min != null ? min.floatValue() : -Float.MAX_VALUE,
                    max != null ? max.floatValue() : Float.MAX_VALUE
            );
        }
        return input;
    }
}
