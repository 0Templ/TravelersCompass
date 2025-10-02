package com.nine.travelerscompass.init;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

public class NeoForgeRegistryObject<I, T extends I> implements RegistryProvider<T> {

    private final DeferredHolder<I, T> value;

    public NeoForgeRegistryObject(DeferredHolder<I, T> value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value.get();
    }
}