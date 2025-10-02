package com.nine.travelerscompass;

import com.nine.travelerscompass.client.hud.HudRenderer;
import com.nine.travelerscompass.client.screen.CompassScreen;
import com.nine.travelerscompass.init.MenuRegistry;
import com.nine.travelerscompass.init.PropertiesRegistry;
import com.nine.travelerscompass.network.s2c.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.player.LocalPlayer;


public class TCFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(MenuRegistry.COMPASS_MENU.get(), CompassScreen::new);

        TCClient.init();

        PropertiesRegistry.register();

        clientNetwork();
        clientEvents();
    }

    public void clientNetwork(){
        ClientPlayNetworking.registerGlobalReceiver(SearchProgressSyncPacket.ID, SearchProgressSyncPacket::onMessage);
        ClientPlayNetworking.registerGlobalReceiver(HudDataPacket.ID, HudDataPacket::onMessage);
        ClientPlayNetworking.registerGlobalReceiver(SyncDataPacket.ID, SyncDataPacket::onMessage);
        ClientPlayNetworking.registerGlobalReceiver(ConfigSyncPacket.ID, ConfigSyncPacket::onMessage);
        ClientPlayNetworking.registerGlobalReceiver(HandshakeRequestPacket.ID, HandshakeRequestPacket::onMessage);
        ClientPlayNetworking.registerGlobalReceiver(CompletePacket.ID, CompletePacket::onMessage);
    }

    public void clientEvents(){
        HudRenderCallback.EVENT.register(HudRenderer::renderTick);
        ClientPlayConnectionEvents.DISCONNECT.register((listener, minecraft) -> {
            LocalPlayer player = minecraft.player;
            TCClient.onClientPlayerLogout(player);
        });
    }
}
