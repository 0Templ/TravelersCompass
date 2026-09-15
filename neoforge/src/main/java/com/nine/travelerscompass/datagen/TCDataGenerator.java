package com.nine.travelerscompass.datagen;

import com.nine.travelerscompass.TCCommon;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.recipes.RecipeProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;

@EventBusSubscriber(modid = TCCommon.MODID)
public class TCDataGenerator {
	
	@SubscribeEvent
	public static void gatherClientData(GatherDataEvent.Client event) {
		gatherData(event);
	}
	
	@SubscribeEvent
	public static void gatherServerData(GatherDataEvent.Server event) {
		gatherData(event);
	}
	
	public static void gatherData(GatherDataEvent event) {
		event.createReloadableRegistryObjects(new RegistrySetBuilder()
				.add(RecipeProvider.asBootstrap(TCRecipeProvider::new)),
				Set.of(TCCommon.MODID),
				"reloadable_" + TCCommon.MODID);
		event.createProvider(TCItemTagProvider::new);
	}
	
}
