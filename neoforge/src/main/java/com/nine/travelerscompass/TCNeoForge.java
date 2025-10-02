package com.nine.travelerscompass;


import com.nine.travelerscompass.client.hud.HudRenderer;
import com.nine.travelerscompass.client.screen.CompassScreen;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.search.SearchManager;
import com.nine.travelerscompass.compat.top.TheOneProbeSetup;
import com.nine.travelerscompass.config.filter.FilterManager;
import com.nine.travelerscompass.init.CreativeTabRegistry;
import com.nine.travelerscompass.init.ItemRegistry;
import com.nine.travelerscompass.init.MenuRegistry;
import com.nine.travelerscompass.init.PropertiesRegistry;
import com.nine.travelerscompass.network.NeoForgeNetworkHandler;
import com.nine.travelerscompass.platform.NeoForgePlatformConfigHelper;
import com.nine.travelerscompass.platform.NeoForgePlatformRegistryHelper;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(TCCommon.MODID)
public class TCNeoForge {

    public TCNeoForge(IEventBus eventBus) {
        loadConfig(ModLoadingContext.get().getActiveContainer());

        eventBus.addListener(this::setupNetwork);
        eventBus.addListener(this::setupClient);
        eventBus.addListener(this::setupScreen);
        eventBus.addListener(this::setupCommon);
        eventBus.addListener(this::imeSetup);
        eventBus.addListener((ModConfigEvent.Reloading event) -> TCCommon.updateCache());

        ItemRegistry.init();
        CompassProperties.init();
        CreativeTabRegistry.init();
        MenuRegistry.init();

        NeoForgePlatformRegistryHelper.ITEMS.register(eventBus);
        NeoForgePlatformRegistryHelper.DATA_COMPONENTS.register(eventBus);
        NeoForgePlatformRegistryHelper.TAB.register(eventBus);
        NeoForgePlatformRegistryHelper.MENUS.register(eventBus);

    }

    public void loadConfig(ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, NeoForgePlatformConfigHelper.COMMON_CONFIG);
        container.registerConfig(ModConfig.Type.COMMON, NeoForgePlatformConfigHelper.COST_CONFIG, "travelerscompass-cost.toml");
    }

    public void setupCommon(FMLCommonSetupEvent setupEvent){
        TCCommon.updateCache();
        NeoForge.EVENT_BUS.addListener((ServerTickEvent.Pre event) -> {
            SearchManager.tick();
        });
        NeoForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedInEvent event) -> {
            if (event.getEntity() instanceof ServerPlayer player) {
                TCCommon.onServerPlayerLogin(player);
            }
        });
    }

    public void setupScreen(RegisterMenuScreensEvent event) {
        event.register(MenuRegistry.COMPASS_MENU.get(), CompassScreen::new);
    }

    public void setupClient(FMLClientSetupEvent setupEvent) {
        PropertiesRegistry.init();
        NeoForge.EVENT_BUS.addListener((RenderGuiEvent.Post event) -> {
            HudRenderer.renderTick(event.getGuiGraphics(), event.getPartialTick());
        });
        NeoForge.EVENT_BUS.addListener((ClientPlayerNetworkEvent.LoggingOut event) -> {
            TCClient.onClientPlayerLogout(event.getPlayer());
        });
        NeoForge.EVENT_BUS.addListener((RenderGuiEvent.Post event) -> {
            HudRenderer.renderTick(event.getGuiGraphics(), event.getPartialTick());
        });

        NeoForge.EVENT_BUS.addListener((ClientPlayerNetworkEvent.LoggingOut event) -> {
            TCClient.onClientPlayerLogout(event.getPlayer());
        });
    }

    public void setupNetwork(RegisterPayloadHandlersEvent event) {
        NeoForgeNetworkHandler.init(event);
    }

    public void imeSetup(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TheOneProbeSetup::new);
        }
    }


}