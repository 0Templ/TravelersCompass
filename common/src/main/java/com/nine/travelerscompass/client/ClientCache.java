package com.nine.travelerscompass.client;

import com.nine.travelerscompass.client.hud.HudData;
import com.nine.travelerscompass.client.hud.HudSettings;
import com.nine.travelerscompass.client.hud.HudSize;
import com.nine.travelerscompass.client.hud.HudType;
import com.nine.travelerscompass.client.utils.SearchProgress;
import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.config.filter.FilterReason;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class ClientCache {
	
	public static final Map<UUID, HudData> HUD_DATA_CACHE = new HashMap<>();
	public static final Map<UUID, SearchProgress> PROGRESS_DATA_CACHE = new HashMap<>();
	public static final Map<FilterReason, Component> FILTER_COMPONENTS_CACHE = new HashMap<>();
	
	public static void updateHudSettings(UUID uuid, UnaryOperator<HudSettings.Builder> modifier) {
		HUD_DATA_CACHE.computeIfPresent(uuid, (id, data) -> {
			HudSettings current = data.getSettings();
			HudSettings newSettings = modifier.apply(current.toBuilder()).build();
			data.updateSettings(newSettings);
			return data;
		});
	}
	
	public static void updateAllHudSettings(ItemStack stack, boolean selected) {
		UUID uuid = CompassComponents.COMPASS_UUID.get(stack);
		HudSize hudSize = CompassComponents.HUD_SIZE.get(stack);
		HudType hudType = CompassComponents.HUD_TYPE.get(stack);
		ClientCache.HUD_DATA_CACHE.put(
				uuid, new HudData(
						new HudSettings(
								CompassComponents.HUD_RENDER_MODE.get(stack),
								hudType,
								CompassComponents.HUD_ALIGNMENT.get(stack),
								CompassComponents.HUD_X_ANCHOR.get(stack),
								CompassComponents.HUD_Y_ANCHOR.get(stack),
								CompassComponents.HUD_WITH_CHAT.get(stack),
								CompassComponents.HUD_SCALE.get(stack),
								hudSize.getWidth(hudType),
								hudSize.getHeight(hudType),
								CompassComponents.HUD_X_POS.get(stack),
								CompassComponents.HUD_Y_POS.get(stack)
						),
						CompassComponents.PAUSE.get(stack),
						selected,
						CompassComponents.SEARCH_STATE.get(stack)
				));
	}
	
	public static Component formatFilterReason(FilterReason reason) {
		return FILTER_COMPONENTS_CACHE.computeIfAbsent(reason, FilterReason::toComponent);
	}
	
	public static void clear() {
		HUD_DATA_CACHE.clear();
		PROGRESS_DATA_CACHE.clear();
		FILTER_COMPONENTS_CACHE.clear();
	}
	
}
