package com.nine.travelerscompass.common.data.codec;

import net.minecraft.nbt.CompoundTag;

public class BooleanCodec implements DataCodec<Boolean> {

    public BooleanCodec() {
    }

    @Override
    public void write(CompoundTag tag, String key, Boolean value) {
        tag.putBoolean(key, value);
    }

    @Override
    public Boolean read(CompoundTag tag, String key) {
        return tag.getBoolean(key);
    }

    @Override
    public boolean exists(CompoundTag tag, String key) {
        return tag.contains(key);
    }
}