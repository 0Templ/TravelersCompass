package com.nine.travelerscompass.datagen;

import com.nine.travelerscompass.init.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TCRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public TCRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemRegistry.TRAVELERS_COMPASS.get()).define('I', Items.IRON_INGOT).define('L', Items.LODESTONE).define('C', Items.COMPASS).pattern("III").pattern("ICI").pattern("ILI").unlockedBy("has_lodestone", VanillaRecipeProvider.has(Items.LODESTONE)).unlockedBy("has_compass", VanillaRecipeProvider.has(Items.COMPASS)).save(consumer);
    }

}
