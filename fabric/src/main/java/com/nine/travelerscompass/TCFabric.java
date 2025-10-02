package com.nine.travelerscompass;

import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.search.SearchManager;
import com.nine.travelerscompass.init.CreativeTabRegistry;
import com.nine.travelerscompass.init.ItemRegistry;
import com.nine.travelerscompass.init.MenuRegistry;
import com.nine.travelerscompass.network.FabricNetworkHandler;
import com.nine.travelerscompass.platform.FabricPlatformConfigHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;

public class TCFabric implements ModInitializer {

    public static final String UPDATE_JSON_URL = "https://raw.githubusercontent.com/0Templ/ModVersions/refs/heads/main/fabric/travelers-compass.json";

    @Override
    public void onInitialize() {
        loadConfig();

        MenuRegistry.init();
        ItemRegistry.init();
        CompassProperties.init();
        CreativeTabRegistry.init();

        FabricNetworkHandler.init();

        TCCommon.updateCache();

        serverEvents();
    }

    public void loadConfig(){
        FabricPlatformConfigHelper.COMMON.load();
        FabricPlatformConfigHelper.COST.load();
    }

    public void serverEvents(){
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            SearchManager.tick();
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.getPlayer();
            TCCommon.onServerPlayerLogin(player);
        });
    }

}
