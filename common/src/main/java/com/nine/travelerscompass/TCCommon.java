package com.nine.travelerscompass;

import com.nine.travelerscompass.config.cost.SearchCostHelper;
import com.nine.travelerscompass.config.filter.FilterManager;
import com.nine.travelerscompass.platform.Platform;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TCCommon {

    public static final String MODID = "travelerscompass";

    public static final String MOD_VERSION = Platform.PLATFORM.getModVersion(MODID);

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static final int NETWORK_PROTOCOL_VERSION = 4;

    public static void onServerPlayerLogin(ServerPlayer player){
        Platform.PLATFORM_NETWORK.sendS2CHandshakePacket(player);
        Platform.PLATFORM_NETWORK.sendS2ConfigSyncPacket(player);
        Platform.PLATFORM_NETWORK.sendS2CCompletePacket(player);
    }

    public static void updateCache(){
        SearchCostHelper.updateCosts();
        FilterManager.reload();
    }

}
