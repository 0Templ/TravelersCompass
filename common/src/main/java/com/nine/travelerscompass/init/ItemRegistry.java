package com.nine.travelerscompass.init;

import com.nine.travelerscompass.platform.Platform;
import net.minecraft.world.item.Item;

public class ItemRegistry {
	
	public static final RegistryProvider<Item> TRAVELERS_COMPASS = Platform.PLATFORM_REGISTRY.registerTravelersCompassItem(
			TCItemIds.TRAVELERS_COMPASS_ID,
			new Item.Properties().stacksTo(1)
	);
	
	public static void init() {
	
	}
	
}
