package com.nine.travelerscompass.common.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record FoundBlockPos(int x, int y, int z, boolean valid) {

    public static final Codec<FoundBlockPos> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.INT.fieldOf("x").forGetter(FoundBlockPos::x),
                            Codec.INT.fieldOf("y").forGetter(FoundBlockPos::y),
                            Codec.INT.fieldOf("z").forGetter(FoundBlockPos::z),
                            Codec.BOOL.fieldOf("valid").forGetter(FoundBlockPos::valid))
                    .apply(instance, FoundBlockPos::new));

    public static final StreamCodec<ByteBuf, FoundBlockPos> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, FoundBlockPos::x,
            ByteBufCodecs.INT, FoundBlockPos::y,
            ByteBufCodecs.INT, FoundBlockPos::z,
            ByteBufCodecs.BOOL, FoundBlockPos::valid,
            FoundBlockPos::new);

    public FoundBlockPos(int x, int y, int z, boolean valid) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.valid = valid;
    }

    public FoundBlockPos(BlockPos pos, boolean valid) {
        this(pos.getX(), pos.getY(), pos.getZ(), valid);
    }

    public FoundBlockPos() {
        this(0, 0, 0, false);
    }

    public BlockPos blockPos(){
        return valid ? new BlockPos(x, y, z) : null;
    }

    public boolean isValid(){
        return valid;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        FoundBlockPos pos = (FoundBlockPos) other;
        return x == pos.x && y == pos.y && z == pos.z && valid == pos.valid;
    }

}
