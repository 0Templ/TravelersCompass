package com.nine.travelerscompass.client.utils;

import net.minecraft.network.chat.Component;

public class RangeUtils {

    public static final RangeAdapter<Integer> INTEGER = new IntegerAdapter();
    public static final RangeAdapter<Float> FLOAT = new FloatAdapter();

    public interface RangeButtonHandler<T extends Number> {

        T current();
        void set(T value);
        T min();
        T max();

        RangeAdapter<T> adapter();

    }

    public interface RangeAdapter<T extends Number> {

        T add(T value, double delta);

        default T clamp(T value, T min, T max) {
            double v = value.doubleValue();
            double minVal = min.doubleValue();
            double maxVal = max.doubleValue();
            if (v < minVal) return min;
            if (v > maxVal) return max;
            return value;
        }

        default Component format(T value) {
            return Component.literal(String.valueOf(value));
        }
    }

    private static class IntegerAdapter implements RangeAdapter<Integer> {

        @Override
        public Integer add(Integer value, double delta) {
            return value + (int) delta;
        }

    }

    private static class FloatAdapter implements RangeAdapter<Float> {

        @Override
        public Float add(Float value, double delta) {
            return value + (float) delta;
        }

        @Override
        public Component format(Float value) {
            return Component.literal(String.format("%.2f", value));
        }
    }

    public static <T extends Number> boolean changeValue(RangeButtonHandler<T> handler, boolean sub, boolean ctrlPressed) {
        return changeValue(handler, sub, ctrlPressed, 1, 5);
    }

    public static <T extends Number> boolean changeValue(RangeButtonHandler<T> handler, boolean sub, boolean ctrlPressed, double step, double ctrlStep) {
        T current = handler.current();
        double change = (sub ? -1 : 1) * (ctrlPressed ? ctrlStep : step);
        T added = handler.adapter().add(current, change);
        T balanced = handler.adapter().clamp(added, handler.min(), handler.max());
        boolean changed = !balanced.equals(current);
        if (changed) {
            handler.set(balanced);
        }
        return changed;
    }

    public static <T extends Number> boolean isOutOfBounds(RangeButtonHandler<T> handler, T value) {
        double val = value.doubleValue();
        return val < handler.min().doubleValue() || val > handler.max().doubleValue();
    }
}
