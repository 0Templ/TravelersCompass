package com.nine.travelerscompass.common.data.codec;

import net.minecraft.nbt.CompoundTag;

public class IntCodec implements DataCodec<Integer> {

    public IntCodec() {
    }

    @Override
    public void write(CompoundTag tag, String key, Integer value) {
        tag.putInt(key, value);
    }

    @Override
    public Integer read(CompoundTag tag, String key) {
        return tag.getInt(key);
    }

    @Override
    public boolean exists(CompoundTag tag, String key) {
        return tag.contains(key);
    }

}