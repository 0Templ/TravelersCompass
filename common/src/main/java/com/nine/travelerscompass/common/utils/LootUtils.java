package com.nine.travelerscompass.common.utils;

import com.nine.travelerscompass.mixin.accessor.LootItemAccessor;
import com.nine.travelerscompass.mixin.accessor.LootPoolAccessor;
import com.nine.travelerscompass.mixin.accessor.LootTableAccessor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

import java.util.*;

public class LootUtils {
	
	private static final Map<Identifier, Set<Item>> LOOT_CACHE = new HashMap<>();
	
	public static List<LootPool> getPools(LootTable lootTable) {
		return ((LootTableAccessor) lootTable).travelerscompass$pools();
	}
	
	public static List<LootPoolEntryContainer> getEntries(LootPool lootPool) {
		return ((LootPoolAccessor) lootPool).travelerscompass$entries();
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
	
	public static Set<Item> getItemsFromLootTable(ResourceKey<LootTable> resourceKey, Level level) {
		Identifier location = resourceKey.identifier();
		if (LOOT_CACHE.containsKey(location)) {
			return LOOT_CACHE.get(location);
		} else {
			MinecraftServer server = level.getServer();
			if (server != null) {
				LootTable lootTable = server.reloadableRegistries().getLootTable(resourceKey);
				Set<Item> dropStackList = new HashSet<>();
				getLootItems(lootTable).stream()
						.map(lootItem -> ((LootItemAccessor) lootItem).travelerscompass$item())
						.forEach((itemHolder -> dropStackList.add(itemHolder.value())));
				LOOT_CACHE.put(location, dropStackList);
			}
		}
		return LOOT_CACHE.getOrDefault(location, Set.of());
	}
	
}
