package com.nine.travelerscompass.common.data.codec;

import net.minecraft.nbt.CompoundTag;

public class FloatCodec implements DataCodec<Float> {

    public FloatCodec() {
    }

    @Override
    public void write(CompoundTag tag, String key, Float value) {
        tag.putFloat(key, value);
    }

    @Override
    public Float read(CompoundTag tag, String key) {
        return tag.getFloat(key);
    }

    @Override
    public boolean exists(CompoundTag tag, String key) {
        return tag.contains(key);
    }
}