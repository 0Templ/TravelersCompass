package com.nine.travelerscompass.datagen;

import com.nine.travelerscompass.TCCommon;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = TCCommon.MODID, bus = EventBusSubscriber.Bus.MOD)
public class TCDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        BlockTagsProvider blockTagsProvider = new EmptyBlockTagProvider(packOutput, lookupProvider, TCCommon.MODID, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new TCItemTagProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter()));
        generator.addProvider(event.includeServer(), new TCRecipeProvider(packOutput, lookupProvider));
    }
}
