package com.nine.travelerscompass.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.config.option.ConfigComment;
import com.nine.travelerscompass.config.option.ConfigOption;
import com.nine.travelerscompass.config.option.ConfigRange;
import com.nine.travelerscompass.config.option.ConfigSide;
import net.minecraft.util.Mth;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ConfigImpl {

    private static final File COMMON_FILE = getConfigPath("travelerscompass-common");
    private static final File COST_FILE = getConfigPath("travelerscompass-cost");
    private static final List<ConfigValue<?>> REGISTERED_VALUES = new ArrayList<>();

    public static final CommentedFileConfig COMMON = CommentedFileConfig.builder(COMMON_FILE)
            .autosave()
            .sync()
            .preserveInsertionOrder()
            .build();

    public static final CommentedFileConfig COST = CommentedFileConfig.builder(COST_FILE)
            .autosave()
            .sync()
            .preserveInsertionOrder()
            .build();

    static {
        COMMON.load();
        COST.load();
    }

    public static <T> ConfigValue<T> register(String key, T defaultValue, ConfigOption... options) {
        return register(key, defaultValue, tryGetClass(defaultValue), tryGetListElementClass(defaultValue), options);
    }

    private static <T> ConfigValue<T> register(
            String key,
            T defaultValue,
            Class<T> clazz,
            Class<?> elementClass,
            ConfigOption... options
    ) {
        ConfigSpec spec = new ConfigSpec();
        for (var option : options) {
            option.apply(spec);
        }

        if (!matchesLoaderTarget(spec.target)) {
            return new ConfigValue<>(
                    key,
                    defaultValue,
                    spec.range,
                    spec.section,
                    spec.side,
                    spec.target,
                    spec.shouldSync,
                    clazz,
                    elementClass
            );
        }

        CommentedFileConfig config = getConfig(spec.side);
        ConfigValue<T> value = new ConfigValue<>(
                config,
                key,
                defaultValue,
                spec.range,
                spec.section,
                spec.side,
                spec.target,
                spec.shouldSync,
                clazz,
                elementClass
        );

        if (config.get(value.path()) == null) {
            config.set(value.path(), serialize(value.defaultValue(), value.clazz));
        }

        clampStored(config, value);

        String comment = buildComment(spec.comment, spec.range, spec.hideConstraints, clazz);
        if (!comment.isBlank()) {
            config.setComment(value.path(), comment);
        }
        normalizeTomlSpacing(spec.side);
        REGISTERED_VALUES.add(value);
        ConfigSyncManager.checkSyncable(value);

        return value;
    }

    public static List<ConfigValue<?>> registeredValues() {
        return List.copyOf(REGISTERED_VALUES);
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> tryGetClass(T defaultValue) {
        return (Class<T>) defaultValue.getClass();
    }

    private static Class<?> tryGetListElementClass(Object defaultValue) {
        if (defaultValue instanceof List<?> list && !list.isEmpty()) {
            return list.get(0).getClass();
        }
        return null;
    }

    private static boolean matchesLoaderTarget(LoaderTarget target) {
        return target == null || target.matchesCurrent();
    }

    private static CommentedFileConfig getConfig(ConfigSide side) {
        return COMMON;
    }

    private static String buildComment(
            ConfigComment comment,
            ConfigRange<?> range,
            boolean hideConstraints,
            Class<?> clazz
    ) {
        List<String> lines = new ArrayList<>();
        if (comment != null && comment.value() != null && !comment.value().isBlank()) {
            lines.add(comment.value());
        }

        if (range != null && !hideConstraints) {
            lines.add("Range: " + range.min() + " .. " + range.max());
        }

        if (clazz != null && clazz.isEnum()) {
            Object[] values = clazz.getEnumConstants();
            String[] names = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                names[i] = ((Enum<?>) values[i]).name();
            }
            lines.add("Available values: " + String.join(", ", names));
        }

        return String.join("\n", lines);
    }

    private static void clampStored(CommentedFileConfig config, ConfigValue<?> value) {
        Object current = config.get(value.path());
        Object clamped = tryClamp(value, current);
        if (clamped != null && !clamped.equals(current)) {
            config.set(value.path(), clamped);
        }
    }

    private static Object tryClamp(ConfigValue<?> value, Object input) {
        if (input == null) {
            return null;
        }

        Number min = value.min();
        Number max = value.max();
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

    private static Object serialize(Object value, Class<?> clazz) {
        if (value == null) {
            return null;
        }
        if (clazz != null && clazz.isEnum()) {
            return ((Enum<?>) value).name();
        }
        return value;
    }

    private static File getConfigPath(String id) {
        File configDir = new File("config");
        if (!configDir.exists() && !configDir.mkdirs()) {
            TCCommon.LOGGER.warn("Could not create config directory at {}", configDir.getAbsolutePath());
        }
        return new File(configDir, id + ".toml");
    }

    private static void normalizeTomlSpacing(ConfigSide side) {
        File file = switch (side) {
            default -> COMMON_FILE;
        };

        if (!file.exists()) {
            return;
        }

        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            String lineSeparator = content.contains("\r\n") ? "\r\n" : "\n";
            String[] lines = content.split("\\R", -1);
            boolean changed = false;

            for (int i = 0; i < lines.length; i++) {
                if (lines[i].trim().equals("#")) {
                    lines[i] = "";
                    changed = true;
                }
            }

            if (changed) {
                Files.writeString(file.toPath(), String.join(lineSeparator, lines), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            TCCommon.LOGGER.warn("Failed to normalize: {}", file.getName(), e);
        }
    }
}
