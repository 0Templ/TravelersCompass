package com.nine.travelerscompass.common.utils;

import com.nine.travelerscompass.mixin.accessor.LootItemAccessor;
import com.nine.travelerscompass.mixin.accessor.LootPoolAccessor;
import com.nine.travelerscompass.platform.accessor.LootTableAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

import java.util.*;

public class LootUtils {

    private static final Map<ResourceLocation, Set<Item>> LOOT_CACHE = new HashMap<>();

    public static List<LootPool> getPools(LootTable lootTable) {
        return ((LootTableAccessor) lootTable).travelerscompass$getPools();
    }

    public static List<LootPoolEntryContainer> getEntries(LootPool lootPool) {
        return List.of(((LootPoolAccessor) lootPool).travelerscompass$entries());
    }

    public static List<LootItem> getLootItems(LootTable lootTable) {
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

    public static Set<Item> getItemsFromLootTable(ResourceLocation location, Level level) {
        if (LOOT_CACHE.containsKey(location)){
            return LOOT_CACHE.get(location);
        }
        else {
            MinecraftServer server = level.getServer();
            if (server != null){
                LootTable lootTable = server.getLootData().getLootTable(location);
                Set<Item> dropStackList = new HashSet<>();
                getLootItems(lootTable).stream()
                        .map(lootItem -> ((LootItemAccessor) lootItem).travelerscompass$item())
                        .forEach(dropStackList::add);
                LOOT_CACHE.put(location, dropStackList);
            }
        }
        return LOOT_CACHE.getOrDefault(location, Set.of());
    }

}
