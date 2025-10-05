package com.nine.travelerscompass.common.data;

import com.nine.travelerscompass.init.RegistryProvider;
import com.nine.travelerscompass.network.packet.c2s.CompassDataPacket;
import com.nine.travelerscompass.platform.Platform;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class DataStorage<T> {

    private final String id;
    private final byte networkId;
    private final Supplier<T> defaultSupplier;
    private final Function<T, T> validator;
    private final RegistryProvider<DataComponentType<T>> componentProvider;

    public DataStorage(String id, byte networkId, Supplier<T> defaultSupplier, Function<T, T> validator, RegistryProvider<DataComponentType<T>> componentProvider){
        this.id = id;
        this.networkId = networkId;
        this.defaultSupplier = defaultSupplier;
        this.validator = validator;
        this.componentProvider = componentProvider;
    }

    public DataComponentType<T> dataComponent(){
        return componentProvider.get();
    }

    public String id(){
        return id;
    }

    public byte networkId(){
        return networkId;
    }

    public T get(ItemStack stack){
        return stack.has(dataComponent()) ? stack.get(dataComponent()) : defaultValue();
    }

    public T defaultValue(){
        return defaultSupplier.get();
    }

    private T validate(T value){
        return validator.apply(value);
    }

    public void set(ItemStack stack, T value){
        stack.set(dataComponent(), validate(value));
    }

    public void sendToServer(ItemStack stack, T value) {
        set(stack, value);
        UUID compassUUID = CompassProperties.COMPASS_UUID.get(stack);
        Platform.PLATFORM_NETWORK.sendToServer(new CompassDataPacket<>(compassUUID, this, value));
    }

}
