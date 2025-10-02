package com.nine.travelerscompass.client.event;

import com.nine.travelerscompass.TCClient;
import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.client.hud.HudRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TCCommon.MODID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public void onRenderTick(RenderGuiOverlayEvent.Post event) {
        HudRenderer.renderTick(event.getGuiGraphics(), event.getPartialTick());
    }

    @SubscribeEvent
    public static void onPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        TCClient.onClientPlayerLogout(event.getPlayer());
    }

}