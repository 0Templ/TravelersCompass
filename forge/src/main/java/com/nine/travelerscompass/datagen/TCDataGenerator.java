package com.nine.travelerscompass.datagen;

import com.nine.travelerscompass.TCCommon;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = TCCommon.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TCDataGenerator {
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		generator.addProvider(true, new TCItemTagProvider(packOutput, lookupProvider, event.getExistingFileHelper()));
		generator.addProvider(true, new TCRecipeProvider.Runner(packOutput, lookupProvider));
	}
	
}
