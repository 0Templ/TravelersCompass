package com.nine.travelerscompass.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class ConfigDraft {

    private final Map<ConfigValue<?>, Object> draftValues = new LinkedHashMap<>();

    private ConfigDraft() {
        this.resetAll();
    }

    public static ConfigDraft create() {
        return new ConfigDraft();
    }

    public void resetAll() {
        this.draftValues.clear();
        for (ConfigValue<?> configValue : ConfigImpl.registeredValues()) {
            this.draftValues.put(configValue, configValue.get());
        }
    }

    public void resetAllToDefaults() {
        this.draftValues.clear();
        for (ConfigValue<?> configValue : ConfigImpl.registeredValues()) {
            this.draftValues.put(configValue, configValue.defaultValue());
        }
    }

    public <T> T get(ConfigValue<T> configValue) {
        Object draftValue = this.draftValues.get(configValue);
        return draftValue != null ? configValue.cast(draftValue) : configValue.get();
    }

    public <T> void set(ConfigValue<T> configValue, T value) {
        this.draftValues.put(configValue, configValue.normalize(value));
    }

    public <T> void reset(ConfigValue<T> configValue) {
        this.draftValues.put(configValue, configValue.get());
    }

    public <T> boolean isDirty(ConfigValue<T> configValue) {
        return !Objects.equals(configValue.get(), this.get(configValue));
    }

    public void commit() {
        for (ConfigValue<?> configValue : ConfigImpl.registeredValues()) {
            this.commitValue(configValue);
        }
    }

    private <T> void commitValue(ConfigValue<T> configValue) {
        T draftValue = this.get(configValue);
        if (!Objects.equals(configValue.get(), draftValue)) {
            configValue.set(draftValue);
        }
    }
}
