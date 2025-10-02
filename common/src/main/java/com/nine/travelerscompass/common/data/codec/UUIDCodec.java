package com.nine.travelerscompass.common.data.codec;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class UUIDCodec implements DataCodec<UUID> {

    public UUIDCodec() {
    }

    @Override
    public void write(CompoundTag tag, String key, UUID value) {
        tag.putUUID(key, value);
    }

    @Override
    public UUID read(CompoundTag tag, String key) {
        return tag.getUUID(key);
    }

    @Override
    public boolean exists(CompoundTag tag, String key) {
        return tag.contains(key);
    }

}
