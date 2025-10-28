package com.nine.travelerscompass.datagen;

import com.nine.travelerscompass.TCCommon;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

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
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		generator.addProvider(true, new TCItemTagProvider(packOutput, lookupProvider));
		generator.addProvider(true, new TCRecipeProvider.Runner(packOutput, lookupProvider));
	}
	
}
