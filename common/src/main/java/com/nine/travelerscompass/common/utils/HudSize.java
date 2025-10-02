package com.nine.travelerscompass.common.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;

public class HudSize {

    public static final Codec<HudSize> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.INT.listOf().fieldOf("values").forGetter(HudSize::values))
                    .apply(instance, HudSize::new));

    public static final StreamCodec<ByteBuf, HudSize> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT.apply(ByteBufCodecs.list()), HudSize::values,
            HudSize::new);

    //Each hud type has separate width and height parameters, so store them in an array
    private final List<Integer> values;

    public HudSize(){
        List<Integer> tmp = new ArrayList<>(HudType.values().length * 2);
        for (HudType type : HudType.values()) {
            tmp.add(type.defaultWidth());
            tmp.add(type.defaultHeight());
        }
        this.values = List.copyOf(tmp);
    }

    public HudSize(List<Integer> values){
        this.values = values;
    }

    public List<Integer> values(){
        return values;
    }

    public HudSize withWidth(HudType type, int value){
        var ret = new ArrayList<>(values);
        ret.set((type.ordinal() * 2), value);
        return new HudSize(ret);
    }

    public HudSize withHeight(HudType type, int value){
        var ret = new ArrayList<>(values);
        ret.set((type.ordinal() * 2) + 1, value);
        return new HudSize(ret);
    }

    public int getWidth(HudType type){
        return values.get(type.ordinal() * 2);
    }

    public int getHeight(HudType type){
        return values.get((type.ordinal() * 2) + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HudSize other)) return false;
        return values.equals(other.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

}
