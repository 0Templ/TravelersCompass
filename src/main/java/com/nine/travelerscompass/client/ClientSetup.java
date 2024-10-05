package com.nine.travelerscompass.client;

import com.nine.travelerscompass.TravelersCompass;
import com.nine.travelerscompass.client.screen.CompassScreen;
import com.nine.travelerscompass.init.MenuRegistry;
import com.nine.travelerscompass.init.PropertiesRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TravelersCompass.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void clientRegistries(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        MenuScreens.register(MenuRegistry.COMPASS_MENU.get(), CompassScreen::new);
        PropertiesRegistry.register();
    }
}
