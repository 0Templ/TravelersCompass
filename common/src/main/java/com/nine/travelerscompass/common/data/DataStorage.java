package com.nine.travelerscompass.common.data;

import com.nine.travelerscompass.common.data.codec.DataCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;
import java.util.function.Function;

public class DataStorage<T> {

    private final ResourceLocation id;
    private final Function<CompoundTag, String> keyResolver;
    private final Function<CompoundTag, T> defaultValue;
    private final Function<T, T> validator;
    private final DataCodec<T> codec;

    public DataStorage(ResourceLocation id, Function<CompoundTag, String> keyResolver, Function<CompoundTag, T> defaultValue, Function<T, T> validator, DataCodec<T> codec) {
        this.id = id;
        this.keyResolver = keyResolver;
        this.defaultValue = defaultValue;
        this.validator = validator;
        this.codec = codec;
    }

    public DataStorage(ResourceLocation id, String key, T defaultValue, DataCodec<T> codec) {
        this(id, (tag -> key), (tag -> defaultValue), (t -> t), codec);
    }

    public DataStorage(ResourceLocation id, Function<CompoundTag, String> keyResolver, T defaultValue, DataCodec<T> codec) {
        this(id, keyResolver, (tag -> defaultValue), (t -> t), codec);
    }

    public String getKey(CompoundTag tag){
        return keyResolver.apply(tag);
    }

    public String getKey(){
        return getKey(null);
    }

    public DataCodec<T> getCodec(){
        return codec;
    }

    public ResourceLocation getId(){
        return id;
    }

    private static <T> boolean areSame(T a, T b) {
        return Objects.equals(a, b);
    }

    public void put(ItemStack stack, T value) {
        put(stack.getOrCreateTag(), value);
    }

    public void put(CompoundTag tag, T value) {
        CompassProperties.put(tag, this, value);
    }

    public T get(ItemStack stack) {
        return get(stack.getOrCreateTag());
    }

    public T get(CompoundTag tag) {
        return CompassProperties.get(tag, this);
    }

    public T read(ItemStack stack){
        CompoundTag tag = stack.getOrCreateTag();
        return read(tag);
    }

    public T read(CompoundTag tag){
        if (!codec.exists(tag, getKey(tag))){
            write(tag, getDefault(tag));
        }
        return codec.read(tag, getKey(tag));
    }

    public void write(CompoundTag tag, T value){
//        if (areSame(value, get(tag))){
//            return;
//        }
        codec.write(tag, getKey(tag), value);
    }

    public T getDefault(ItemStack stack){
        return getDefault(stack.getOrCreateTag());
    }

    public T getDefault(CompoundTag tag){
        return defaultValue.apply(tag);
    }

    public T validate(T value){
        return validator.apply(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DataStorage<?> other && this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}