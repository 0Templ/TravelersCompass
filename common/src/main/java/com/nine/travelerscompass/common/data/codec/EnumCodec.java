package com.nine.travelerscompass.common.data.codec;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class EnumCodec<T extends Enum<T>> implements DataCodec<T> {

    private final Class<T> enumClass;

    public EnumCodec(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    public Class<T> getEnumClass(){
        return enumClass;
    }

    @Override
    public void write(CompoundTag tag, String key, T value) {
        tag.putString(key, value.name());
    }

    @Override
    public T read(CompoundTag tag, String key) {
        return Enum.valueOf(enumClass, tag.getString(key));
    }

    @Override
    public boolean exists(CompoundTag tag, String key) {
        return tag.contains(key, Tag.TAG_STRING);
    }
}
