package com.nine.travelerscompass.platform;

import java.util.ServiceLoader;

public class Platform {

	public static final IPlatformCoreHelper PLATFORM = load(IPlatformCoreHelper.class);

	public static final IPlatformMatchersHelper PLATFORM_MATCHERS = load(IPlatformMatchersHelper.class);

	public static final IPlatformNetworkHelper PLATFORM_NETWORK = load(IPlatformNetworkHelper.class);

	public static final IPlatformRegistryHelper PLATFORM_REGISTRY = load(IPlatformRegistryHelper.class);

	public static <T> T load(Class<T> clazz) {
		return ServiceLoader.load(clazz)
				.findFirst()
				.orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
	}

}
