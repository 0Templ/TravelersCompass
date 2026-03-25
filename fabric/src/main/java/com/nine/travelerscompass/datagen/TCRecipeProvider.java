package com.nine.travelerscompass.datagen;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.init.ItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class TCRecipeProvider extends FabricRecipeProvider {
	
	public TCRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider);
	}
	
	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
		
		return new RecipeProvider(registryLookup, exporter) {
			@Override
			public void buildRecipes() {
				TagKey<Item> netheriteIngots = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "ingots/netherite"));
				shaped(RecipeCategory.TOOLS, ItemRegistry.TRAVELERS_COMPASS.get())
						.define('I', netheriteIngots)
						.define('L', Items.LODESTONE)
						.define('C', Items.COMPASS)
						.pattern(" L ")
						.pattern("LCL")
						.pattern(" I ")
						.unlockedBy("has_netherite_ingot", has(netheriteIngots))
						.save(exporter);
			}
		};
	}
	
	@Override
	public String getName() {
		return TCCommon.MODID;
	}
	
}
