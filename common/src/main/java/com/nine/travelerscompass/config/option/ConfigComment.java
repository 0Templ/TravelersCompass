package com.nine.travelerscompass.config.option;

import com.nine.travelerscompass.config.ConfigSpec;

public record ConfigComment(String value) implements ConfigOption {

    public static final ConfigComment EMPTY = of("");

    public static ConfigComment of() {
        return new ConfigComment("");
    }

    public static ConfigComment of(String comment) {
        return new ConfigComment(comment);
    }

    public ConfigComment emptyLine() {
        return line("");
    }

    public ConfigComment line(String comment) {
        if (this.value == null) {
            return new ConfigComment(comment);
        }
        return new ConfigComment(value + "\n" + comment);
    }

    @Override
    public void apply(ConfigSpec spec) {
        spec.comment = this;
    }
}
