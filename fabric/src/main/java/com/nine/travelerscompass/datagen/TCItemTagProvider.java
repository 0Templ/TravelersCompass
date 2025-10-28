package com.nine.travelerscompass.datagen;

import com.nine.travelerscompass.init.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class TCItemTagProvider extends IntrinsicHolderTagsProvider<Item> {
	
	public TCItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, Registries.ITEM, registriesFuture, (item) -> item.builtInRegistryHolder().key());
	}
	
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(ItemTags.COMPASSES).add(ItemRegistry.TRAVELERS_COMPASS.get());
	}
	
}