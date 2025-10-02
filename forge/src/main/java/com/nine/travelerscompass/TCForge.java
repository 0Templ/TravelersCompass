package com.nine.travelerscompass;

import com.nine.travelerscompass.client.hud.HudRenderer;
import com.nine.travelerscompass.client.screen.CompassScreen;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.search.SearchManager;
import com.nine.travelerscompass.init.CreativeTabRegistry;
import com.nine.travelerscompass.init.ItemRegistry;
import com.nine.travelerscompass.init.MenuRegistry;
import com.nine.travelerscompass.init.PropertiesRegistry;
import com.nine.travelerscompass.network.ForgeNetworkHandler;
import com.nine.travelerscompass.platform.ForgePlatformConfigHelper;
import com.nine.travelerscompass.platform.ForgePlatformRegistryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TCCommon.MODID)
public class TCForge {

    public TCForge(FMLJavaModLoadingContext context) {
        loadConfig(context);

        IEventBus eventBus = context.getModEventBus();

        eventBus.addListener(this::setupNetwork);
        eventBus.addListener(this::setupClient);
        eventBus.addListener(this::commonSetup);
        eventBus.addListener((ModConfigEvent.Reloading event) -> TCCommon.updateCache());

        ItemRegistry.init();
        CompassProperties.init();
        CreativeTabRegistry.init();
        MenuRegistry.init();

        ForgePlatformRegistryHelper.ITEMS.register(eventBus);
        ForgePlatformRegistryHelper.DATA_COMPONENTS.register(eventBus);
        ForgePlatformRegistryHelper.TAB.register(eventBus);
        ForgePlatformRegistryHelper.MENUS.register(eventBus);

    }

    public void commonSetup(FMLCommonSetupEvent setupEvent){
        TCCommon.updateCache();
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ServerTickEvent.Pre event) -> {
            SearchManager.tick();
        });
        MinecraftForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedInEvent event) -> {
            if (event.getEntity() instanceof ServerPlayer player) {
                TCCommon.onServerPlayerLogin(player);
            }
        });
    }

    public void setupClient(FMLClientSetupEvent setupEvent) {
        MenuScreens.register(MenuRegistry.COMPASS_MENU.get(), CompassScreen::new);
        PropertiesRegistry.init();
        MinecraftForge.EVENT_BUS.addListener((ClientPlayerNetworkEvent.LoggingOut event) -> {
            TCClient.onClientPlayerLogout(event.getPlayer());
        });
        //Hud renders in GuiMixin
//        MinecraftForge.EVENT_BUS.addListener((RenderGuiOverlayEvent.Post event) -> {
//            HudRenderer.renderTick(event.getGuiGraphics(), event.getPartialTick());
//        });
    }

    public void loadConfig(FMLJavaModLoadingContext context){
        context.registerConfig(ModConfig.Type.COMMON, ForgePlatformConfigHelper.COMMON_CONFIG, "travelerscompass-common.toml");
        context.registerConfig(ModConfig.Type.COMMON, ForgePlatformConfigHelper.COST_CONFIG, "travelerscompass-cost.toml");
    }


    private void setupNetwork(FMLCommonSetupEvent event) {
        ForgeNetworkHandler.init();
    }

}