package com.nine.travelerscompass.datagen;

import com.nine.travelerscompass.init.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class TCRecipeProvider extends RecipeProvider {

    public TCRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemRegistry.TRAVELERS_COMPASS.get()).define('I', Items.IRON_INGOT).define('L', Items.LODESTONE).define('C', Items.COMPASS).pattern("III").pattern("ICI").pattern("ILI").unlockedBy("has_lodestone", VanillaRecipeProvider.has(Items.LODESTONE)).unlockedBy("has_compass", VanillaRecipeProvider.has(Items.COMPASS)).save(recipeOutput);
    }

}
