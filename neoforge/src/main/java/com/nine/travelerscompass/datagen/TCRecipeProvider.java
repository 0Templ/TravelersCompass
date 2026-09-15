package com.nine.travelerscompass.datagen;

import com.nine.travelerscompass.init.ItemRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

public class TCRecipeProvider extends RecipeProvider {
	
	public TCRecipeProvider(BootstrapContext<Recipe<?>> recipeContext, BootstrapContext<Advancement> advancementContext) {
		super(recipeContext, advancementContext);
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
