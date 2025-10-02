package com.nine.travelerscompass.datagen;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.init.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class TCItemTagProvider extends ItemTagsProvider {

    public TCItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags) {
        super(pOutput, pLookupProvider, pBlockTags, TCCommon.MODID, null);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ItemTags.COMPASSES).add(ItemRegistry.TRAVELERS_COMPASS.get());
    }


}
