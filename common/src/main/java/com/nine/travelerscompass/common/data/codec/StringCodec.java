package com.nine.travelerscompass.common.data.codec;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class StringCodec implements DataCodec<String>{


    public StringCodec() {
    }

    @Override
    public void write(CompoundTag tag, String key, String value) {
        tag.putString(key, value);
    }

    @Override
    public String read(CompoundTag tag, String key) {
        return tag.getString(key);
    }

    @Override
    public boolean exists(CompoundTag tag, String key) {
        return tag.contains(key, Tag.TAG_STRING );
    }
}
