package com.nine.travelerscompass.config.filter;


import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.config.TCConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FilterManager {
	
	private static final Map<Item, FilterReason> FILTER_CACHE = new HashMap<>();
	
	private static final Map<Identifier, FilterReason> FILTER_ENTITIES_CACHE = new HashMap<>();
	
	public static FilterData filterData;
	
	public static FilterReason getFilterReason(EntityType<?> type) {
		if (type == null) {
			return new FilterReason.Allowed();
		}
		Identifier location = BuiltInRegistries.ENTITY_TYPE.getKey(type);
		FilterReason answer;
		answer = FILTER_ENTITIES_CACHE.get(location);
		if (answer == null) {
			boolean inList = filterData.entitiesFilterSet.contains(location);
			if ((filterData.entitiesFilterType == FilterType.BLACKLIST && inList)
					|| (filterData.entitiesFilterType == FilterType.WHITELIST && !inList)
			) {
				answer = new FilterReason.ByEntityType(type);
			} else {
				answer = new FilterReason.Allowed();
			}
			FILTER_ENTITIES_CACHE.put(location, answer);
		}
		return answer;
	}
	
	public static FilterReason getFilterReason(Item item) {
		if (item == Items.AIR) {
			return new FilterReason.Allowed();
		}
		FilterReason answer;
		answer = FILTER_CACHE.get(item);
		if (answer == null) {
			answer = computeItemFilterReason(item);
			FILTER_CACHE.put(item, answer);
		}
		return answer;
	}
	
	
	private static FilterReason computeItemFilterReason(Item item) {
		Identifier itemId = BuiltInRegistries.ITEM.getKey(item);
		String modId = itemId.getNamespace();
		
		boolean modMatch = filterData.modsFilterSet.contains(modId);
		if (filterData.modsFilterType == FilterType.BLACKLIST && modMatch) return new FilterReason.ByModId(modId);
		if (filterData.modsFilterType == FilterType.WHITELIST && !modMatch) return new FilterReason.ByModId(modId);
		
		boolean itemMatch = filterData.itemsFilterSet.contains(itemId);
		if (filterData.itemsFilterType == FilterType.BLACKLIST && itemMatch) return new FilterReason.ByItemId(itemId);
		if (filterData.itemsFilterType == FilterType.WHITELIST && !itemMatch) return new FilterReason.ByItemId(itemId);
		
		boolean hasWhitelistedTag = false;
		for (TagKey<Item> tag : filterData.tagsFilterSet) {
			boolean hasTag = item.builtInRegistryHolder().is(tag);
			if (filterData.tagsFilterType == FilterType.BLACKLIST && hasTag) return new FilterReason.ByTag(tag);
			if (hasTag) {
				hasWhitelistedTag = true;
			}
		}
		if (filterData.tagsFilterType == FilterType.WHITELIST && !hasWhitelistedTag) {
			return new FilterReason.ByTag(filterData.tagsFilterSet.iterator().next());
		}
		
		return new FilterReason.Allowed();
	}
	
	public static void reload() {
		FILTER_CACHE.clear();
		FILTER_ENTITIES_CACHE.clear();
		filterData = new FilterData(TCConfig.MODS_FILTER_TYPE.get(),
				new HashSet<>(TCConfig.MODS_FILTER.get()),
				TCConfig.ITEM_TAGS_FILTER_TYPE.get(),
				TCConfig.ITEM_TAGS_FILTER.get().stream().map(s -> TagKey.create(Registries.ITEM, Identifier.parse(s))).collect(Collectors.toSet()),
				TCConfig.ITEMS_FILTER_TYPE.get(), TCConfig.ITEMS_FILTER.get().stream().map(Identifier::parse).collect(Collectors.toSet()),
				TCConfig.ENTITIES_FILTER_TYPE.get(), TCConfig.ENTITIES_FILTER.get().stream().map(Identifier::parse).collect(Collectors.toSet()))
		;
	}
	
	public static boolean passesFilters(ItemStack stack) {
		if (stack.isEmpty() || stack.getItem() instanceof TravelersCompassItem) return false;
		return getFilterReason(stack.getItem()).isAllowed();
	}
	
	public record FilterData(
			FilterType modsFilterType, Set<String> modsFilterSet,
			FilterType tagsFilterType, Set<TagKey<Item>> tagsFilterSet,
			FilterType itemsFilterType, Set<Identifier> itemsFilterSet,
			FilterType entitiesFilterType, Set<Identifier> entitiesFilterSet
	) {
	
	}
	
}
