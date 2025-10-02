package com.nine.travelerscompass;

import com.nine.travelerscompass.common.search.SearchManager;
import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.init.CreativeTabRegistry;
import com.nine.travelerscompass.init.ItemRegistry;
import com.nine.travelerscompass.init.MenuRegistry;
import com.nine.travelerscompass.network.c2s.*;
import com.nine.travelerscompass.platform.FabricPlatformConfigHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class TCFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        loadConfig();

        TCConfig.init();
        ItemRegistry.init();
        MenuRegistry.init();
        CreativeTabRegistry.init();

        TCCommon.updateCache();

        serverNetwork();
        serverEvents();
    }

    public void loadConfig(){
        FabricPlatformConfigHelper.COMMON.load();
        FabricPlatformConfigHelper.COST.load();
    }

    public void serverNetwork(){
        ServerPlayNetworking.registerGlobalReceiver(CompassDataPacket.ID, CompassDataPacket::onMessage);
        ServerPlayNetworking.registerGlobalReceiver(PausePacket.ID, PausePacket::onMessage);
        ServerPlayNetworking.registerGlobalReceiver(WideSearchPacket.ID, WideSearchPacket::onMessage);
        ServerPlayNetworking.registerGlobalReceiver(GhostTargetPacket.ID, GhostTargetPacket::onMessage);
        ServerPlayNetworking.registerGlobalReceiver(HandshakeResponsePacket.ID, HandshakeResponsePacket::onMessage);
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
