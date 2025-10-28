package com.nine.travelerscompass.init;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.platform.Platform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ItemRegistry {
	
	public static final RegistryProvider<Item> TRAVELERS_COMPASS = registerTravelersCompass();
	
	
	private static RegistryProvider<Item> registerTravelersCompass() {
		String id = "travelerscompass";
		ResourceKey<Item> key = ResourceKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, id));
		return Platform.PLATFORM_REGISTRY.registerTravelersCompassItem(
				"travelerscompass",
				new Item.Properties().stacksTo(1).setId(key));
	}
	
	public static void init() {
	
	}
	
}
