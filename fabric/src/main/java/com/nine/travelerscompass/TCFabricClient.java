package com.nine.travelerscompass;

import com.nine.travelerscompass.client.hud.HudRenderer;
import com.nine.travelerscompass.client.screen.CompassScreen;
import com.nine.travelerscompass.init.MenuRegistry;
import com.nine.travelerscompass.init.PropertiesRegistry;
import com.nine.travelerscompass.network.FabricNetworkHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class TCFabricClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        FabricNetworkHandler.clientSetup();
        MenuScreens.register(MenuRegistry.COMPASS_MENU.get(), CompassScreen::new);
        TCClient.init();
        PropertiesRegistry.init();
        clientEvents();
    }

    public void clientEvents(){
        HudRenderCallback.EVENT.register(HudRenderer::renderTick);
        ClientPlayConnectionEvents.DISCONNECT.register((listener, minecraft) -> {
            LocalPlayer player = minecraft.player;
            TCClient.onClientPlayerLogout(player);
        });
    }

}
