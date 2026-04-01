package com.nine.travelerscompass;

import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.common.search.SearchManager;
import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.init.CreativeTabRegistry;
import com.nine.travelerscompass.init.ItemRegistry;
import com.nine.travelerscompass.init.MenuRegistry;
import com.nine.travelerscompass.network.FabricNetworkHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;

public class TCFabric implements ModInitializer {
	
	@Override
	public void onInitialize() {
		TCConfig.init();
		
		MenuRegistry.init();
		ItemRegistry.init();
		CompassComponents.init();
		CreativeTabRegistry.init();
		
		FabricNetworkHandler.init();
		
		TCCommon.updateCache();
		
		commonEvents();
	}

	
	public void commonEvents() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			SearchManager.tick();
		});
		
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayer player = handler.getPlayer();
			TCCommon.onServerPlayerLogin(player);
		});
	}
	
}
