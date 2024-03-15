package com.nine.travelerscompass.client;

import com.nine.travelerscompass.TravelersCompass;
import com.nine.travelerscompass.client.screen.CompassScreen;
import com.nine.travelerscompass.init.MenuRegistry;
import com.nine.travelerscompass.init.PropertiesRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TravelersCompass.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    @SubscribeEvent
    public static void registerScreen(FMLClientSetupEvent event) {
        MenuScreens.register(MenuRegistry.COMPASS_MENU.get(), CompassScreen::new);
    }
    @SubscribeEvent
    public static void registerProperties(FMLClientSetupEvent event) {
        PropertiesRegistry.register();
    }
}
