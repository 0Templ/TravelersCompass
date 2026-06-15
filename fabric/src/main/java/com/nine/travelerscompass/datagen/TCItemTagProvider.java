package com.nine.travelerscompass.datagen;

import com.nine.travelerscompass.init.ItemRegistry;
import com.nine.travelerscompass.init.TCItemIds;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

public class TCItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
	
	public TCItemTagProvider(
			FabricPackOutput output,
			CompletableFuture<HolderLookup.Provider> registryLookupFuture
	) {
		super(output, registryLookupFuture);
	}
	
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(ItemTags.COMPASSES).add(TCItemIds.TRAVELERS_COMPASS_ID);
		
	}


}