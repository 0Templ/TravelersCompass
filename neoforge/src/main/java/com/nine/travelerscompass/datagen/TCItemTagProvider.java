package com.nine.travelerscompass.datagen;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.init.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class TCItemTagProvider extends ItemTagsProvider {
	
	public TCItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, TCCommon.MODID);
	}
	
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(ItemTags.COMPASSES).add(ItemRegistry.TRAVELERS_COMPASS.get());
	}
	
	
}
