package com.nine.travelerscompass.datagen;

import com.nine.travelerscompass.TravelersCompass;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = TravelersCompass.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TCDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        BlockTagsProvider blockTagsProvider = new EmptyBlockTagProvider(packOutput, lookupProvider, TravelersCompass.MODID, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new TCItemTagProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter()));
        generator.addProvider(event.includeServer(), new TCRecipeProvider(packOutput));
    }

}
