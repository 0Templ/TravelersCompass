package com.nine.travelerscompass.init;

public class FabricRegistryProvider<T> implements RegistryProvider<T> {
	
	private final T value;
	
	public FabricRegistryProvider(T value) {
		this.value = value;
	}
	
	@Override
	public T get() {
		return value;
	}
	
}
