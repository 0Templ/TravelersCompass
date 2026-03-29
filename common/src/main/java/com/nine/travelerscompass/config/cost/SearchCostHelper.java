package com.nine.travelerscompass.config.cost;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.config.TCConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class SearchCostHelper {
	
	final static int MAX_CACHE_SIZE = 500;
	
	public static final Map<Item, SearchCost> CACHED_ITEM_COSTS = new LinkedHashMap<>(MAX_CACHE_SIZE, 0.8f, true) {
		
		@Override
		public boolean removeEldestEntry(Map.Entry<Item, SearchCost> eldest) {
			return size() > MAX_CACHE_SIZE;
		}
		
	};
	
	public static final Map<Item, SearchCost> ITEM_COSTS = new HashMap<>();
	
	public static final Map<String, SearchCost> TAG_COSTS = new HashMap<>();
	
	public static SearchCost getCost(Item item) {
		SearchCost cost = CACHED_ITEM_COSTS.get(item);
		if (cost != null) {
			return cost;
		}
		SearchCost ret = ITEM_COSTS.get(item);
		if (ret != null) {
			CACHED_ITEM_COSTS.put(item, ret);
			return ret;
		}
		var tags = item.builtInRegistryHolder().tags().toList();
		for (var tag : tags) {
			SearchCost searchCost = TAG_COSTS.get(tag.location().toString());
			if (searchCost != null) {
				CACHED_ITEM_COSTS.put(item, searchCost);
				return searchCost;
			}
		}
		
		CACHED_ITEM_COSTS.put(item, SearchCost.FREE);
		return SearchCost.FREE;
	}
	
	public static Map<String, SearchCost> fromConfig(List<String> list) {
		Map<String, SearchCost> ret = new HashMap<>();
		for (String raw : list) {
			String line = raw.trim();
			if (line.isEmpty()) continue;
			String[] split = line.split("=", 2);
			if (split.length != 2) {
				continue;
			}
			String id = split[0].trim();
			String[] costData = split[1].trim().split("/", 2);
			try {
				int cost = Integer.parseInt(costData[0]);
				int req = costData.length > 1 ? Integer.parseInt(costData[1]) : 0;
				ret.put(id, new SearchCost(cost, req));
			} catch (Exception e) {
				TCCommon.LOGGER.warn("Couldn't parse: {}", raw);
			}
		}
		return ret;
	}
	
	public static void updateCosts() {
		ITEM_COSTS.clear();
		TAG_COSTS.clear();
		CACHED_ITEM_COSTS.clear();
		for (var data : fromConfig(TCConfig.ITEM_COSTS.get()).entrySet()) {
			Identifier id = Identifier.parse(data.getKey());
			var optional = BuiltInRegistries.ITEM.getOptional(id);
			optional.ifPresent(item -> ITEM_COSTS.put(item, data.getValue()));
		}
		for (var data : fromConfig(TCConfig.ITEM_TAGS_COSTS.get()).entrySet()) {
			try {
				TAG_COSTS.put(data.getKey(), data.getValue());
			} catch (NumberFormatException e) {
				TCCommon.LOGGER.warn("Couldn't parse cost-tag: {}", data);
			}
		}
		CACHED_ITEM_COSTS.putAll(ITEM_COSTS);
	}
	
	public static boolean consumeXp(Player player, Item item) {
		if (TCConfig.SEARCH_REQUIRES_XP.get() && !player.isCreative()) {
			SearchCost searchCost = SearchCostHelper.getCost(item);
			if (!searchCost.isFree()) {
				if (searchCost.meetsAllConditions(player.experienceLevel)) {
					player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.25F, 1F);
					player.giveExperienceLevels(-searchCost.costLevel());
					return true;
				}
				return false;
			}
		}
		return true;
	}
	
}
