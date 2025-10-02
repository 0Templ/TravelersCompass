package com.nine.travelerscompass.common.data.codec;

import net.minecraft.nbt.CompoundTag;

public interface DataCodec<T> {

    void write(CompoundTag tag, String key, T value);

    T read(CompoundTag tag, String key);

    boolean exists(CompoundTag tag, String key);

}
