package com.nine.travelerscompass.datagen;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.init.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class TCRecipeProvider extends RecipeProvider {
	
	public TCRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
		super(registries, output);
	}
	
	public static class Runner extends RecipeProvider.Runner {
		
		public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
			super(packOutput, provider);
		}
		
		@Override
		protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
			return new TCRecipeProvider(provider, recipeOutput);
		}
		
		@Override
		public String getName() {
			return TCCommon.MODID;
		}
		
	}
	
	@Override
	protected void buildRecipes() {
		TagKey<Item> netheriteIngots = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "ingots/netherite"));
		shaped(RecipeCategory.TOOLS, ItemRegistry.TRAVELERS_COMPASS.get())
				.define('I', netheriteIngots)
				.define('L', Items.LODESTONE)
				.define('C', Items.COMPASS)
				.pattern(" L ")
				.pattern("LCL")
				.pattern(" I ")
				.unlockedBy("has_netherite_ingot", has(netheriteIngots))
				.save(output);
	}
	
}
