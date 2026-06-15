package com.nine.travelerscompass.init;

import com.nine.travelerscompass.TCCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class TCItemIds {
	
	public static final ResourceKey<Item> TRAVELERS_COMPASS_ID = create("travelerscompass");
	
	
	private static ResourceKey<Item> create(final String name) {
		return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(TCCommon.MODID, name));
	}
	
	

}
