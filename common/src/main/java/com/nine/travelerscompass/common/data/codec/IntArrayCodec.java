package com.nine.travelerscompass.common.data.codec;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class IntArrayCodec implements DataCodec<int[]> {

    public IntArrayCodec() {
    }

    @Override
    public void write(CompoundTag tag, String key, int[] value) {
        tag.putIntArray(key, value);
    }

    @Override
    public int[] read(CompoundTag tag, String key) {
        return tag.getIntArray(key);
    }

    @Override
    public boolean exists(CompoundTag tag, String key) {
        return tag.contains(key, Tag.TAG_INT_ARRAY);
    }
}
