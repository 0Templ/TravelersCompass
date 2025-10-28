package com.nine.travelerscompass;

import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.config.cost.SearchCostHelper;
import com.nine.travelerscompass.config.filter.FilterManager;
import com.nine.travelerscompass.network.packet.s2c.CompletePacket;
import com.nine.travelerscompass.network.packet.s2c.ConfigSyncPacket;
import com.nine.travelerscompass.network.packet.s2c.NetworkSyncPacket;
import com.nine.travelerscompass.platform.Platform;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.Collectors;

public class TCCommon {
	
	public static final String MODID = "travelerscompass";
	
	public static final String MOD_VERSION = Platform.PLATFORM.getModVersion(MODID);
	
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	
	public static final int NETWORK_PROTOCOL_VERSION = 4;
	
	public static void onServerPlayerLogin(ServerPlayer player) {
		Platform.PLATFORM_NETWORK.sendToClient(player, new NetworkSyncPacket(
				CompassProperties.NETWORK_REGISTRY.entrySet()
						.stream()
						.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().id()))));
		Platform.PLATFORM_NETWORK.sendToClient(player, new ConfigSyncPacket());
		Platform.PLATFORM_NETWORK.sendToClient(player, new CompletePacket());
		
	}
	
	public static void updateCache() {
		SearchCostHelper.updateCosts();
		FilterManager.reload();
	}
	
}
