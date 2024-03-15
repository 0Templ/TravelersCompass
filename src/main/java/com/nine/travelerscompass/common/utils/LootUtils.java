package com.nine.travelerscompass.common.utils;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LootUtils {
    public static List<LootPool> getPools(LootTable lootTable){
        return ObfuscationReflectionHelper.getPrivateValue(LootTable.class, lootTable, "f_79109_");
    }
    public static List<LootPoolEntryContainer> getEntries(LootPool lootPool){
        return ObfuscationReflectionHelper.getPrivateValue(LootPool.class, lootPool, "f_79023_");

    }
    public static List<LootItem> getLootItems(LootTable lootTable){
        List<LootItem> dropList = new ArrayList<>();
        getPools(lootTable).forEach(
                lootPool -> {
                    getEntries(lootPool).stream()
                            .filter(entry -> entry instanceof LootItem).map(entry -> (LootItem) entry)
                            .forEach(dropList::add);
                }
        );
        dropList.removeIf(Objects::isNull);

        return dropList;
    }
    public static List<Item> getItemsFromLootTable(LootTable lootTable) {
        List<Item> dropStackList = new ArrayList<>();


        return dropStackList;
    }

}
