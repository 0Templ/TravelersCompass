package com.nine.travelerscompass.datagen;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.init.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class TCItemTagProvider extends IntrinsicHolderTagsProvider<Item> {
	
	public TCItemTagProvider(PackOutput output,
							 CompletableFuture<HolderLookup.Provider> lookupProvider,
							 ExistingFileHelper existingFileHelper) {
		super(output,
				Registries.ITEM,
				lookupProvider,
				item -> item.builtInRegistryHolder().key(),
				TCCommon.MODID,
				existingFileHelper);
	}
	
	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(ItemTags.COMPASSES).add(ItemRegistry.TRAVELERS_COMPASS.get());
	}
}
