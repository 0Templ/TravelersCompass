package com.nine.travelerscompass.common.search.location.codec;

import com.nine.travelerscompass.common.search.location.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class LocationCodecs {

    private static final Map<ResourceLocation, LocationCodec<?>> CODECS = new HashMap<>();

    static {
        register(EntityLocationObject.TYPE, EntityLocationObject.CODEC);
        register(BlockLocationObject.TYPE, BlockLocationObject.CODEC);
        register(ContainerLocationObject.TYPE, ContainerLocationObject.CODEC);
        register(ContainerEntityLocationObject.TYPE, ContainerEntityLocationObject.CODEC);
        register(SpawnerLocationObject.TYPE, SpawnerLocationObject.CODEC);

        register(LootrContainerLocationObject.TYPE, LootrContainerLocationObject.CODEC);
        register(LootrMinecartLocationObject.TYPE, LootrMinecartLocationObject.CODEC);
    }

    private static void register(ResourceLocation id, LocationCodec<?> codec) {
        CODECS.put(id, codec);
    }

    @SuppressWarnings("unchecked")
    public static void write(FriendlyByteBuf buf, ILocationObject object) {
        ResourceLocation typeId = object.type();
        buf.writeResourceLocation(typeId);
        LocationCodec<ILocationObject> codec = (LocationCodec<ILocationObject>) CODECS.get(typeId);
        codec.write(buf, object);
    }

    public static ILocationObject read(FriendlyByteBuf buf) {
        ResourceLocation type = buf.readResourceLocation();
        LocationCodec<?> codec = CODECS.get(type);
        if (codec == null) {
            throw new IllegalArgumentException("No such codec: " + type);
        }
        return codec.read(buf);
    }

}
