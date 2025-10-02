package com.nine.travelerscompass.common.data.codec;

import com.nine.travelerscompass.common.utils.FoundBlockPos;
import net.minecraft.nbt.CompoundTag;

public class FoundBlockPosCodec implements DataCodec<FoundBlockPos> {


    public FoundBlockPosCodec() {
    }

    @Override
    public void write(CompoundTag tag, String key, FoundBlockPos value) {
        value.writeData(tag);
    }

    @Override
    public FoundBlockPos read(CompoundTag tag, String key) {
        return FoundBlockPos.readData(tag);
    }

    @Override
    public boolean exists(CompoundTag tag, String key) {
        return tag.contains(FoundBlockPos.VALID_TAG);
    }

}
