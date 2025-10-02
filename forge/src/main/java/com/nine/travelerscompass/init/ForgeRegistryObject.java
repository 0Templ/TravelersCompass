package com.nine.travelerscompass.init;


import net.minecraftforge.registries.RegistryObject;

public class ForgeRegistryObject <T> implements RegistryProvider<T> {

    private final RegistryObject<T> value;

    public ForgeRegistryObject(RegistryObject<T> value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value.get();
    }

}
