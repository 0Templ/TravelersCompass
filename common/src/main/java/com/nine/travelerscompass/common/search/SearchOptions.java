package com.nine.travelerscompass.common.search;

import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.data.DataStorage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SearchOptions {
	
	private final Map<DataStorage<?>, Object> options = new HashMap<>();
	
	private final int entitiesSearchRange;
	
	private final int chunksSearchRange;
	
	private final boolean wideSearch;
	
	private final UUID playerUUID;
	
	private final ServerPlayer player;
	
	public SearchOptions(ItemStack stack, ServerPlayer player, boolean wideSearch) {
		for (DataStorage<?> property : CompassProperties.SEARCH_OPTIONS) {
			options.put(property, CompassProperties.get(stack, property));
		}
		this.entitiesSearchRange = CompassProperties.ENTITIES_SEARCH_RANGE.get(stack);
		this.chunksSearchRange = wideSearch ? CompassProperties.WIDE_SEARCH_RANGE.get(stack) : CompassProperties.BLOCK_SEARCH_CHUNK_RANGE.get(stack);
		this.playerUUID = player.getUUID();
		this.player = player;
		this.wideSearch = wideSearch;
	}
	
	public int getEntitiesSearchRange() {
		return entitiesSearchRange;
	}
	
	public int getChunksSearchRange() {
		return chunksSearchRange;
	}
	
	public UUID getPlayerUUID() {
		return playerUUID;
	}
	
	public ServerPlayer player() {
		return player;
	}
	
	public boolean wideSearch() {
		return wideSearch;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(DataStorage<T> option) {
		return (T) options.get(option);
	}
	
}
